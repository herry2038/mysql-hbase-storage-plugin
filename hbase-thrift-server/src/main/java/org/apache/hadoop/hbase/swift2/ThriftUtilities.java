/**
 *
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.hadoop.hbase.swift2;

import static org.apache.hadoop.hbase.util.Bytes.getBytes;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.hadoop.hbase.HRegionInfo;
import org.apache.hadoop.hbase.HRegionLocation;
import org.apache.hadoop.hbase.ServerName;
import org.apache.hadoop.hbase.classification.InterfaceAudience;
import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.CellUtil;
import org.apache.hadoop.hbase.HConstants;
import org.apache.hadoop.hbase.client.Append;
import org.apache.hadoop.hbase.client.Delete;
import org.apache.hadoop.hbase.client.Durability;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.HRegionLocator;
import org.apache.hadoop.hbase.client.Increment;
import org.apache.hadoop.hbase.client.OperationWithAttributes;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.RowMutations;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.filter.ParseFilter;
import org.apache.hadoop.hbase.security.visibility.Authorizations;
import org.apache.hadoop.hbase.security.visibility.CellVisibility;
import org.apache.hadoop.hbase.swift2.generated.TAppend;
import org.apache.hadoop.hbase.swift2.generated.TColumn;
import org.apache.hadoop.hbase.swift2.generated.TColumnIncrement;
import org.apache.hadoop.hbase.swift2.generated.TColumnValue;
import org.apache.hadoop.hbase.swift2.generated.TDelete;
import org.apache.hadoop.hbase.swift2.generated.TDeleteType;
import org.apache.hadoop.hbase.swift2.generated.TDurability;
import org.apache.hadoop.hbase.swift2.generated.TGet;
import org.apache.hadoop.hbase.swift2.generated.THRegionInfo;
import org.apache.hadoop.hbase.swift2.generated.THRegionLocation;
import org.apache.hadoop.hbase.swift2.generated.TIncrement;
import org.apache.hadoop.hbase.swift2.generated.TMutation;
import org.apache.hadoop.hbase.swift2.generated.TPut;
import org.apache.hadoop.hbase.swift2.generated.TResult;
import org.apache.hadoop.hbase.swift2.generated.TRowMutations;
import org.apache.hadoop.hbase.swift2.generated.TScan;
import org.apache.hadoop.hbase.swift2.generated.TServerName;
import org.apache.hadoop.hbase.swift2.generated.TTimeRange;
import org.apache.hadoop.hbase.util.Bytes;

@InterfaceAudience.Private
public class ThriftUtilities {

  private ThriftUtilities() {
    throw new UnsupportedOperationException("Can't initialize class");
  }

  /**
   * Creates a {@link Get} (HBase) from a {@link TGet} (Thrift).
   *
   * This ignores any timestamps set on {@link TColumn} objects.
   *
   * @param in the <code>TGet</code> to convert
   *
   * @return <code>Get</code> object
   *
   * @throws IOException if an invalid time range or max version parameter is given
   */
  public static Get getFromThrift(TGet in) throws IOException {
    Get out = new Get(in.getRow());

    // Timestamp overwrites time range if both are set
    if (in.getTimestamp() != null) {
      out.setTimeStamp(in.getTimestamp());
    } else if (in.getTimeRange() != null ) {
      out.setTimeRange(in.getTimeRange().getMinStamp(), in.getTimeRange().getMaxStamp());
    }

    if (in.getMaxVersions() != null ) {
      out.setMaxVersions(in.getMaxVersions());
    }

    if (in.getFilterString() != null ) {
      ParseFilter parseFilter = new ParseFilter();
      out.setFilter(parseFilter.parseFilterString(in.getFilterString()));
    }

    if (in.getAttributes() != null) {
      addAttributes(out,in.getAttributes());
    }

    if (in.getAuthorizations() != null) {
      out.setAuthorizations(new Authorizations(in.getAuthorizations().getLabels()));
    }
    
    if (in.getColumns() == null) {
      return out;
    }

    for (TColumn column : in.getColumns()) {
      if (column.getQualifier() != null ) {
        out.addColumn(column.getFamily(), column.getQualifier());
      } else {
        out.addFamily(column.getFamily());
      }
    }

    return out;
  }

  /**
   * Converts multiple {@link TGet}s (Thrift) into a list of {@link Get}s (HBase).
   *
   * @param in list of <code>TGet</code>s to convert
   *
   * @return list of <code>Get</code> objects
   *
   * @throws IOException if an invalid time range or max version parameter is given
   * @see #getFromThrift(TGet)
   */
  public static List<Get> getsFromThrift(List<TGet> in) throws IOException {
    List<Get> out = new ArrayList<Get>(in.size());
    for (TGet get : in) {
      out.add(getFromThrift(get));
    }
    return out;
  }

  /**
   * Creates a {@link TResult} (Thrift) from a {@link Result} (HBase).
   *
   * @param in the <code>Result</code> to convert
   *
   * @return converted result, returns an empty result if the input is <code>null</code>
   */
  public static TResult resultFromHBase(Result in) {
    Cell[] raw = in.rawCells();
    


    List<TColumnValue> columnValues = new ArrayList<TColumnValue>();
    for (Cell kv : raw) {
      TColumnValue col = new TColumnValue(CellUtil.cloneFamily(kv), 
    		  CellUtil.cloneQualifier(kv),
    		  CellUtil.cloneValue(kv),
    		  kv.getTimestamp(),    		  
    		  kv.getTagsLength() > 0 ? CellUtil.getTagArray(kv) : null    		  
    		  );
      columnValues.add(col) ;      
    }
    TResult out = new TResult(in.getRow(), columnValues);    
    return out;
  }

  /**
   * Converts multiple {@link Result}s (HBase) into a list of {@link TResult}s (Thrift).
   *
   * @param in array of <code>Result</code>s to convert
   *
   * @return list of converted <code>TResult</code>s
   *
   * @see #resultFromHBase(Result)
   */
  public static List<TResult> resultsFromHBase(Result[] in) {
    List<TResult> out = new ArrayList<TResult>(in.length);
    for (Result result : in) {
      out.add(resultFromHBase(result));
    }
    return out;
  }

  /**
   * Creates a {@link Put} (HBase) from a {@link TPut} (Thrift)
   *
   * @param in the <code>TPut</code> to convert
   *
   * @return converted <code>Put</code>
   */
  public static Put putFromThrift(TPut in) {
    Put out;

    if (in.getTimestamp() != null ) {
      out = new Put(in.getRow(), in.getTimestamp());
    } else {
      out = new Put(in.getRow());
    }

    if (in.getDurability() != null ) {
      out.setDurability(durabilityFromThrift(in.getDurability()));
    }

    for (TColumnValue columnValue : in.getColumnValues()) {
      if (columnValue.getTimestamp() != null ) {
        out.addImmutable(
            columnValue.getFamily(), columnValue.getQualifier(), columnValue.getTimestamp(),
            columnValue.getValue());
      } else {
        out.addImmutable(
            columnValue.getFamily(), columnValue.getQualifier(), columnValue.getValue());
      }
    }

    if (in.getAttributes() != null ) {
      addAttributes(out,in.getAttributes());
    }
    
    if (in.getCellVisibility() != null) {
      out.setCellVisibility(new CellVisibility(in.getCellVisibility().getExpression()));
    }

    return out;
  }

  /**
   * Converts multiple {@link TPut}s (Thrift) into a list of {@link Put}s (HBase).
   *
   * @param in list of <code>TPut</code>s to convert
   *
   * @return list of converted <code>Put</code>s
   *
   * @see #putFromThrift(TPut)
   */
  public static List<Put> putsFromThrift(List<TPut> in) {
    List<Put> out = new ArrayList<Put>(in.size());
    for (TPut put : in) {
      out.add(putFromThrift(put));
    }
    return out;
  }

  /**
   * Creates a {@link Delete} (HBase) from a {@link TDelete} (Thrift).
   *
   * @param in the <code>TDelete</code> to convert
   *
   * @return converted <code>Delete</code>
   */
  public static Delete deleteFromThrift(TDelete in) {
    Delete out;

    if (in.getColumns() != null ) {
      out = new Delete(in.getRow());
      for (TColumn column : in.getColumns()) {
        if (column.getQualifier() != null ) {
          if (column.getTimestamp() != null ) {
            if (in.getDeleteType() != null &&
                in.getDeleteType().equals(TDeleteType.DELETE_COLUMNS))
              out.deleteColumns(column.getFamily(), column.getQualifier(), column.getTimestamp());
            else
              out.deleteColumn(column.getFamily(), column.getQualifier(), column.getTimestamp());
          } else {
            if (in.getDeleteType() != null &&
                in.getDeleteType().equals(TDeleteType.DELETE_COLUMNS))
              out.deleteColumns(column.getFamily(), column.getQualifier());
            else
              out.deleteColumn(column.getFamily(), column.getQualifier());
          }

        } else {
          if (column.getTimestamp() != null ) {
            out.deleteFamily(column.getFamily(), column.getTimestamp());
          } else {
            out.deleteFamily(column.getFamily());
          }
        }
      }
    } else {
      if (in.getTimestamp() != null ) {
        out = new Delete(in.getRow(), in.getTimestamp());
      } else {
        out = new Delete(in.getRow());
      }
    }

    if (in.getAttributes() != null ) {
      addAttributes(out,in.getAttributes());
    }

    if (in.getDurability() != null ) {
      out.setDurability(durabilityFromThrift(in.getDurability()));
    }

    return out;
  }

  /**
   * Converts multiple {@link TDelete}s (Thrift) into a list of {@link Delete}s (HBase).
   *
   * @param in list of <code>TDelete</code>s to convert
   *
   * @return list of converted <code>Delete</code>s
   *
   * @see #deleteFromThrift(TDelete)
   */

  public static List<Delete> deletesFromThrift(List<TDelete> in) {
    List<Delete> out = new ArrayList<Delete>(in.size());
    for (TDelete delete : in) {
      out.add(deleteFromThrift(delete));
    }
    return out;
  }

  public static TDelete deleteFromHBase(Delete in) {
	  
    
    Long rowTimestamp = in.getTimeStamp() != HConstants.LATEST_TIMESTAMP ? in.getTimeStamp() : null ;
    
    List<TColumn> columns = new ArrayList<TColumn>();
    // Map<family, List<KeyValue>>
    for (Map.Entry<byte[], List<org.apache.hadoop.hbase.Cell>> familyEntry:
        in.getFamilyCellMap().entrySet()) {
      
      byte[] family = familyEntry.getKey();
      byte[] qualifier = null ;
      Long timestamp = null ;
      for (org.apache.hadoop.hbase.Cell cell: familyEntry.getValue()) {
    	  if ( cell.getFamilyArray() != null ) qualifier = CellUtil.cloneFamily(cell); 
    	  if ( cell.getQualifierArray() != null ) family = CellUtil.cloneQualifier(cell);
    	  if ( cell.getTimestamp() != HConstants.LATEST_TIMESTAMP ) timestamp = cell.getTimestamp() ;        
      }
      columns.add(new TColumn(family, qualifier, timestamp));
    }
    
    return new TDelete.Builder().setColumns(columns).setTimestamp(rowTimestamp).setRow(in.getRow()).build() ;    
  }

  /**
   * Creates a {@link RowMutations} (HBase) from a {@link TRowMutations} (Thrift)
   *
   * @param in the <code>TRowMutations</code> to convert
   *
   * @return converted <code>RowMutations</code>
   */
  public static RowMutations rowMutationsFromThrift(TRowMutations in) throws IOException {
    RowMutations out = new RowMutations(in.getRow());
    List<TMutation> mutations = in.getMutations();
    for (TMutation mutation : mutations) {
      if (mutation.isSetPut()) {
        out.add(putFromThrift(mutation.getPut()));
      }
      if (mutation.isSetDeleteSingle()) {
        out.add(deleteFromThrift(mutation.getDeleteSingle()));
      }
    }
    return out;
  }

  public static Scan scanFromThrift(TScan in) throws IOException {
    Scan out = new Scan();

    if (in.getStartRow() != null )
      out.setStartRow(in.getStartRow());
    if (in.getStopRow() != null )
      out.setStopRow(in.getStopRow());
    if (in.getCaching() != null )
      out.setCaching(in.getCaching());
    if (in.getMaxVersions() != null ) {
      out.setMaxVersions(in.getMaxVersions());
    }

    if (in.getColumns() != null ) {
      for (TColumn column : in.getColumns()) {
        if (column.getQualifier() != null ) {
          out.addColumn(column.getFamily(), column.getQualifier());
        } else {
          out.addFamily(column.getFamily());
        }
      }
    }

    TTimeRange timeRange = in.getTimeRange();
    if (timeRange != null ) {
      out.setTimeRange(timeRange.getMinStamp(), timeRange.getMaxStamp());
    }

    if (in.getBatchSize() != null ) {
      out.setBatch(in.getBatchSize());
    }

    if (in.getFilterString() != null ) {
      ParseFilter parseFilter = new ParseFilter();
      out.setFilter(parseFilter.parseFilterString(in.getFilterString()));
    }

    if (in.getAttributes() != null ) {
      addAttributes(out,in.getAttributes());
    }
    
    if (in.getAuthorizations() != null ) {
      out.setAuthorizations(new Authorizations(in.getAuthorizations().getLabels()));
    }

    if (in.isReversed() != null ) {
      out.setReversed(in.isReversed());
    }

    return out;
  }

  public static Increment incrementFromThrift(TIncrement in) throws IOException {
    Increment out = new Increment(in.getRow());
    for (TColumnIncrement column : in.getColumns()) {
      out.addColumn(column.getFamily(), column.getQualifier(), column.getAmount());
    }

    if (in.getAttributes() != null ) {
      addAttributes(out,in.getAttributes());
    }

    if (in.getDurability() != null ) {
      out.setDurability(durabilityFromThrift(in.getDurability()));
    }
    
    if(in.getCellVisibility() != null) {
      out.setCellVisibility(new CellVisibility(in.getCellVisibility().getExpression()));
    }

    return out;
  }

  public static Append appendFromThrift(TAppend append) throws IOException {
    Append out = new Append(append.getRow());
    for (TColumnValue column : append.getColumns()) {
      out.add(column.getFamily(), column.getQualifier(), column.getValue());
    }

    if (append.getAttributes() != null ) {
      addAttributes(out, append.getAttributes());
    }

    if (append.getDurability() != null ) {
      out.setDurability(durabilityFromThrift(append.getDurability()));
    }
    
    if(append.getCellVisibility() != null) {
      out.setCellVisibility(new CellVisibility(append.getCellVisibility().getExpression()));
    }

    return out;
  }

  public static THRegionLocation regionLocationFromHBase(HRegionLocation hrl) {
    HRegionInfo hri = hrl.getRegionInfo();
    ServerName serverName = hrl.getServerName();

    THRegionInfo.Builder thRegionInfo = new THRegionInfo.Builder();
    THRegionLocation.Builder thRegionLocation = new THRegionLocation.Builder();
    TServerName.Builder tServerName = new TServerName.Builder();

    tServerName.setHostName(serverName.getHostname());
    tServerName.setPort(serverName.getPort());
    tServerName.setStartCode(serverName.getStartcode());

    thRegionInfo.setTableName(hri.getTable().getName());
    thRegionInfo.setEndKey(hri.getEndKey());
    thRegionInfo.setStartKey(hri.getStartKey());
    thRegionInfo.setOffline(hri.isOffline());
    thRegionInfo.setSplit(hri.isSplit());
    thRegionInfo.setReplicaId(hri.getReplicaId());

    
    thRegionLocation.setRegionInfo(thRegionInfo.build());
    thRegionLocation.setServerName(tServerName.build());

    return thRegionLocation.build() ;
  }

  public static List<THRegionLocation> regionLocationsFromHBase(List<HRegionLocation> locations) {
    List<THRegionLocation> tlocations = new ArrayList<THRegionLocation>(locations.size());
    for (HRegionLocation hrl:locations) {
      tlocations.add(regionLocationFromHBase(hrl));
    }
    return tlocations;
  }

  /**
   * Adds all the attributes into the Operation object
   */
  private static void addAttributes(OperationWithAttributes op,
                                    Map<byte[], byte[]> attributes) {
    if (attributes == null || attributes.size() == 0) {
      return;
    }
    for (Map.Entry<byte[], byte[]> entry : attributes.entrySet()) {
      String name = Bytes.toStringBinary(entry.getKey());
      byte[] value =  entry.getValue();
      op.setAttribute(name, value);
    }
  }

  private static Durability durabilityFromThrift(TDurability tDurability) {
    switch (tDurability.getValue()) {
      case 1: return Durability.SKIP_WAL;
      case 2: return Durability.ASYNC_WAL;
      case 3: return Durability.SYNC_WAL;
      case 4: return Durability.FSYNC_WAL;
      default: return null;
    }
  }
}
