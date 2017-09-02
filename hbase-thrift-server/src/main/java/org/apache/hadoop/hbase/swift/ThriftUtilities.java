/**
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

package org.apache.hadoop.hbase.swift;

import static org.apache.hadoop.hbase.util.Bytes.getBytes;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.hadoop.hbase.classification.InterfaceAudience;
import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.CellUtil;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.KeyValue;
import org.apache.hadoop.hbase.client.Append;
import org.apache.hadoop.hbase.client.Increment;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.io.compress.Compression;
import org.apache.hadoop.hbase.regionserver.BloomType;
import org.apache.hadoop.hbase.swift.generated.ColumnDescriptor;
import org.apache.hadoop.hbase.swift.generated.IllegalArgument;
import org.apache.hadoop.hbase.swift.generated.TAppend;
import org.apache.hadoop.hbase.swift.generated.TCell;
import org.apache.hadoop.hbase.swift.generated.TColumn;
import org.apache.hadoop.hbase.swift.generated.TIncrement;
import org.apache.hadoop.hbase.swift.generated.TRowResult;
import org.apache.hadoop.hbase.util.Bytes;

@InterfaceAudience.Private
public class ThriftUtilities {

  /**
   * This utility method creates a new Hbase HColumnDescriptor object based on a
   * Thrift ColumnDescriptor "struct".
   *
   * @param in
   *          Thrift ColumnDescriptor object
   * @return HColumnDescriptor
   * @throws IllegalArgument
   */
  static public HColumnDescriptor colDescFromThrift(ColumnDescriptor in)
      throws IllegalArgument {
    Compression.Algorithm comp =
      Compression.getCompressionAlgorithmByName(in.getCompression().toLowerCase());
    BloomType bt =
      BloomType.valueOf(in.getBloomFilterType());

    if (in.getName() == null || in.getName().length == 0 ) {
      throw new IllegalArgument("column name is empty");
    }
    byte [] parsedName = KeyValue.parseColumn(in.getName())[0];
    HColumnDescriptor col = new HColumnDescriptor(parsedName)
        .setMaxVersions(in.getMaxVersions())
        .setCompressionType(comp)
        .setInMemory(in.isInMemory())
        .setBlockCacheEnabled(in.isBlockCacheEnabled())
        .setTimeToLive(in.getTimeToLive() > 0 ? in.getTimeToLive() : Integer.MAX_VALUE)
        .setBloomFilterType(bt);
    return col;
  }

  /**
   * This utility method creates a new Thrift ColumnDescriptor "struct" based on
   * an Hbase HColumnDescriptor object.
   *
   * @param in
   *          Hbase HColumnDescriptor object
   * @return Thrift ColumnDescriptor
   */
  static public ColumnDescriptor colDescFromHbase(HColumnDescriptor in) {
    ColumnDescriptor.Builder col = new ColumnDescriptor.Builder();
    col.setName(Bytes.add(in.getName(), KeyValue.COLUMN_FAMILY_DELIM_ARRAY))
    	.setMaxVersions(in.getMaxVersions())
    	.setCompression(in.getCompression().toString())
    	.setInMemory(in.isInMemory())
    	.setBlockCacheEnabled(in.isBlockCacheEnabled())
    	.setBloomFilterType(in.getBloomFilterType().toString()) ;
    
    return col.build() ;
  }

  /**
   * This utility method creates a list of Thrift TCell "struct" based on
   * an Hbase Cell object. The empty list is returned if the input is null.
   *
   * @param in
   *          Hbase Cell object
   * @return Thrift TCell array
   */
  static public List<TCell> cellFromHBase(Cell in) {
    List<TCell> list = new ArrayList<TCell>(1);
    if (in != null) {
      list.add(new TCell(CellUtil.cloneValue(in), in.getTimestamp()));
    }
    return list;
  }

  /**
   * This utility method creates a list of Thrift TCell "struct" based on
   * an Hbase Cell array. The empty list is returned if the input is null.
   * @param in Hbase Cell array
   * @return Thrift TCell array
   */
  static public List<TCell> cellFromHBase(Cell[] in) {
    List<TCell> list = null;
    if (in != null) {
      list = new ArrayList<TCell>(in.length);
      for (int i = 0; i < in.length; i++) {
        list.add(new TCell(CellUtil.cloneValue(in[i]), in[i].getTimestamp()));
      }
    } else {
      list = new ArrayList<TCell>(0);
    }
    return list;
  }

  /**
   * This utility method creates a list of Thrift TRowResult "struct" based on
   * an Hbase RowResult object. The empty list is returned if the input is
   * null.
   *
   * @param in
   *          Hbase RowResult object
   * @param sortColumns
   *          This boolean dictates if row data is returned in a sorted order
   *          sortColumns = True will set TRowResult's sortedColumns member
   *                        which is an ArrayList of TColumn struct
   *          sortColumns = False will set TRowResult's columns member which is
   *                        a map of columnName and TCell struct
   * @return Thrift TRowResult array
   */
  static public List<TRowResult> rowResultFromHBase(Result[] in, boolean sortColumns) {
    List<TRowResult> results = new ArrayList<TRowResult>();
    for ( Result result_ : in) {
        if(result_ == null || result_.isEmpty()) {
            continue;
        }
        TRowResult.Builder result = new TRowResult.Builder();
        result.setRow(result_.getRow()) ;
        
        if (sortColumns) {
        	List<TColumn> sortedColumns = new ArrayList<TColumn>(); 
        	result.setSortedColumns(sortedColumns) ;
        	for (Cell kv : result_.rawCells()) {
        		sortedColumns.add(new TColumn(
        				KeyValue.makeColumn(CellUtil.cloneFamily(kv),
        						CellUtil.cloneQualifier(kv)),
        				new TCell(CellUtil.cloneValue(kv), kv.getTimestamp())));
        	}
        	
        } else {
        	Map<byte[], TCell> columns = new TreeMap<byte[], TCell>(new Bytes.ByteArrayComparator()) ;
        	/*
        		new Comparator<byte[]>() {
        		@Override public int compare(byte[] s1, byte[] s2) {
        			
        	    }
        	};) ;
        	*/
        	result.setColumns(columns) ;
        	
        	
        	for (Cell kv : result_.rawCells()) {
        		columns.put(
        				KeyValue.makeColumn(CellUtil.cloneFamily(kv),
        						CellUtil.cloneQualifier(kv)),
        				new TCell(CellUtil.cloneValue(kv), kv.getTimestamp()));
        	}
        }
        results.add(result.build());
    }
    return results;
  }

  /**
   * This utility method creates a list of Thrift TRowResult "struct" based on
   * an array of Hbase RowResult objects. The empty list is returned if the input is
   * null.
   *
   * @param in
   *          Array of Hbase RowResult objects
   * @return Thrift TRowResult array
   */
  static public List<TRowResult> rowResultFromHBase(Result[] in) {
    return rowResultFromHBase(in, false);
  }

  static public List<TRowResult> rowResultFromHBase(Result in) {
    Result [] result = { in };
    return rowResultFromHBase(result);
  }

  /**
   * From a {@link TIncrement} create an {@link Increment}.
   * @param tincrement the Thrift version of an increment
   * @return an increment that the {@link TIncrement} represented.
   */
  public static Increment incrementFromThrift(TIncrement tincrement) {
    Increment inc = new Increment(tincrement.getRow());
    byte[][] famAndQf = KeyValue.parseColumn(tincrement.getColumn());
    if (famAndQf.length != 2) return null;
    inc.addColumn(famAndQf[0], famAndQf[1], tincrement.getAmmount());
    return inc;
  }

  /**
   * From a {@link TAppend} create an {@link Append}.
   * @param tappend the Thrift version of an append.
   * @return an increment that the {@link TAppend} represented.
   */
  public static Append appendFromThrift(TAppend tappend) {
    Append append = new Append(tappend.getRow());
    List<byte[]> columns = tappend.getColumns();
    List<byte[]> values = tappend.getValues();

    if (columns.size() != values.size()) {
      throw new IllegalArgumentException(
          "Sizes of columns and values in tappend object are not matching");
    }

    int length = columns.size();

    for (int i = 0; i < length; i++) {
      byte[][] famAndQf = KeyValue.parseColumn(columns.get(i));
      append.add(famAndQf[0], famAndQf[1], values.get(i));
    }
    return append;
  }
  

}
