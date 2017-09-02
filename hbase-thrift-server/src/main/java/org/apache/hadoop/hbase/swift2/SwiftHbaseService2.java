package org.apache.hadoop.hbase.swift2;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HRegionLocation;
import org.apache.hadoop.hbase.client.RegionLocator;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.Table;
import org.apache.hadoop.hbase.security.UserProvider;
import org.apache.hadoop.hbase.swift2.generated.THBaseService;
import org.apache.hadoop.hbase.swift2.generated.TAppend;
import org.apache.hadoop.hbase.swift2.generated.TDelete;
import org.apache.hadoop.hbase.swift2.generated.TGet;
import org.apache.hadoop.hbase.swift2.generated.THRegionLocation;
import org.apache.hadoop.hbase.swift2.generated.TIOError;
import org.apache.hadoop.hbase.swift2.generated.TIllegalArgument;
import org.apache.hadoop.hbase.swift2.generated.TIncrement;
import org.apache.hadoop.hbase.swift2.generated.TPut;
import org.apache.hadoop.hbase.swift2.generated.TResult;
import org.apache.hadoop.hbase.swift2.generated.TRowMutations;
import org.apache.hadoop.hbase.swift2.generated.TScan;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.hbase.util.ConnectionCache;
import org.apache.thrift.TException;

public class SwiftHbaseService2 implements THBaseService {
	  // TODO: Size of pool configuraple
	  private static final Log LOG = LogFactory.getLog(SwiftHbaseService2.class);

	  // nextScannerId and scannerMap are used to manage scanner state
	  // TODO: Cleanup thread for Scanners, Scanner id wrap
	  private final AtomicInteger nextScannerId = new AtomicInteger(0);
	  private final Map<Integer, ResultScanner> scannerMap =
	      new ConcurrentHashMap<Integer, ResultScanner>();

	  private final ConnectionCache connectionCache;

	  static final String CLEANUP_INTERVAL = "hbase.thrift.connection.cleanup-interval";
	  static final String MAX_IDLETIME = "hbase.thrift.connection.max-idletime";

	  
	  private static long now() {
		  return System.nanoTime();
	  }
	  
	  public SwiftHbaseService2(final Configuration conf,
		      final UserProvider userProvider) throws IOException {
		    int cleanInterval = conf.getInt(CLEANUP_INTERVAL, 10 * 1000);
		    int maxIdleTime = conf.getInt(MAX_IDLETIME, 10 * 60 * 1000);
		    connectionCache = new ConnectionCache(
		      conf, userProvider, cleanInterval, maxIdleTime);
	  }
	  

	  private Table getTable(byte[] tableName) {
	    try {
	      return connectionCache.getTable(Bytes.toString(tableName));
	    } catch (IOException e) {
	      throw new RuntimeException(e);
	    }
	  }

	  private RegionLocator getLocator(byte[] tableName) {
	    try {
	      return connectionCache.getRegionLocator(tableName);
	    } catch (IOException ie) {
	      throw new RuntimeException(ie);
	    }
	  }

	  private void closeTable(Table table) throws TIOError {
	    try {
	      table.close();
	    } catch (IOException e) {
	      throw getTIOError(e);
	    }
	  }

	  private TIOError getTIOError(IOException e) {
	    TIOError err = new TIOError(e.getMessage());
	    //err.setMessage(e.getMessage());
	    return err;
	  }

	  /**
	   * Assigns a unique ID to the scanner and adds the mapping to an internal HashMap.
	   * @param scanner to add
	   * @return Id for this Scanner
	   */
	  private int addScanner(ResultScanner scanner) {
	    int id = nextScannerId.getAndIncrement();
	    scannerMap.put(id, scanner);
	    return id;
	  }

	  /**
	   * Returns the Scanner associated with the specified Id.
	   * @param id of the Scanner to get
	   * @return a Scanner, or null if the Id is invalid
	   */
	  private ResultScanner getScanner(int id) {
	    return scannerMap.get(id);
	  }

	  void setEffectiveUser(String effectiveUser) {
	    connectionCache.setEffectiveUser(effectiveUser);
	  }

	  /**
	   * Removes the scanner associated with the specified ID from the internal HashMap.
	   * @param id of the Scanner to remove
	   * @return the removed Scanner, or <code>null</code> if the Id is invalid
	   */
	  protected ResultScanner removeScanner(int id) {
	    return scannerMap.remove(id);
	  }

	  @Override
	  public boolean exists(byte[] table, TGet get) throws TIOError {
	    Table htable = getTable(table);
	    try {
	      return htable.exists(ThriftUtilities.getFromThrift(get));
	    } catch (IOException e) {
	      throw getTIOError(e);
	    } finally {
	      closeTable(htable);
	    }
	  }

	  @Override
	  public TResult get(byte[] table, TGet get) throws TIOError {
	    Table htable = getTable(table);
	    try {
	      return ThriftUtilities.resultFromHBase(htable.get(ThriftUtilities.getFromThrift(get)));
	    } catch (IOException e) {
	    	LOG.error("get a exception",e) ;
	      throw getTIOError(e);
	    } finally {
	      closeTable(htable);
	    }
	  }

	  @Override
	  public List<TResult> getMultiple(byte[] table, List<TGet> gets) throws TIOError {
	    Table htable = getTable(table);
	    try {
	      return ThriftUtilities.resultsFromHBase(htable.get(ThriftUtilities.getsFromThrift(gets)));
	    } catch (IOException e) {
	      throw getTIOError(e);
	    } finally {
	      closeTable(htable);
	    }
	  }

	  @Override
	  public void put(byte[] table, TPut put) throws TIOError {
	    Table htable = getTable(table);
	    try {
	      htable.put(ThriftUtilities.putFromThrift(put));
	    } catch (IOException e) {
	      throw getTIOError(e);
	    } finally {
	      closeTable(htable);
	    }
	  }

	  @Override
	  public boolean checkAndPut(byte[] table, byte[] row, byte[] family,
	      byte[] qualifier, byte[] value, TPut put) throws TIOError {
	    Table htable = getTable(table);
	    try {
	      return htable.checkAndPut(row, family,
	        qualifier, value,
	        ThriftUtilities.putFromThrift(put));
	    } catch (IOException e) {
	      throw getTIOError(e);
	    } finally {
	      closeTable(htable);
	    }
	  }

	  @Override
	  public void putMultiple(byte[] table, List<TPut> puts) throws TIOError {
	    Table htable = getTable(table);
	    try {
	      htable.put(ThriftUtilities.putsFromThrift(puts));
	    } catch (IOException e) {
	      throw getTIOError(e);
	    } finally {
	      closeTable(htable);
	    }
	  }

	  @Override
	  public void deleteSingle(byte[] table, TDelete deleteSingle) throws TIOError {
	    Table htable = getTable(table);
	    try {
	      htable.delete(ThriftUtilities.deleteFromThrift(deleteSingle));
	    } catch (IOException e) {
	      throw getTIOError(e);
	    } finally {
	      closeTable(htable);
	    }
	  }

	  @Override
	  public List<TDelete> deleteMultiple(byte[] table, List<TDelete> deletes) throws TIOError {
	    Table htable = getTable(table);
	    try {
	      htable.delete(ThriftUtilities.deletesFromThrift(deletes));
	    } catch (IOException e) {
	      throw getTIOError(e);
	    } finally {
	      closeTable(htable);
	    }
	    return Collections.emptyList();
	  }

	  @Override
	  public boolean checkAndDelete(byte[] table, byte[] row, byte[] family,
	      byte[] qualifier, byte[] value, TDelete deleteSingle) throws TIOError {
	    Table htable = getTable(table);

	    try {
	      if (value == null) {
	        return htable.checkAndDelete(row, family,
	          qualifier, null, ThriftUtilities.deleteFromThrift(deleteSingle));
	      } else {
	        return htable.checkAndDelete(row, family,
	          qualifier, value,
	          ThriftUtilities.deleteFromThrift(deleteSingle));
	      }
	    } catch (IOException e) {
	      throw getTIOError(e);
	    } finally {
	      closeTable(htable);
	    }
	  }

	  @Override
	  public TResult increment(byte[] table, TIncrement increment) throws TIOError {
	    Table htable = getTable(table);
	    try {
	      return ThriftUtilities.resultFromHBase(htable.increment(ThriftUtilities.incrementFromThrift(increment)));
	    } catch (IOException e) {
	      throw getTIOError(e);
	    } finally {
	      closeTable(htable);
	    }
	  }

	  @Override
	  public TResult append(byte[] table, TAppend append) throws TIOError {
	    Table htable = getTable(table);
	    try {
	      return ThriftUtilities.resultFromHBase(htable.append(ThriftUtilities.appendFromThrift(append)));
	    } catch (IOException e) {
	      throw getTIOError(e);
	    } finally {
	      closeTable(htable);
	    }
	  }

	  @Override
	  public int openScanner(byte[] table, TScan scan) throws TIOError {
	    Table htable = getTable(table);
	    ResultScanner resultScanner = null;
	    try {
	      resultScanner = htable.getScanner(ThriftUtilities.scanFromThrift(scan));
	    } catch (IOException e) {
	      throw getTIOError(e);
	    } finally {
	      closeTable(htable);
	    }
	    return addScanner(resultScanner);
	  }

	  @Override
	  public List<TResult> getScannerRows(int scannerId, int numRows) throws TIOError,
	      TIllegalArgument {
	    ResultScanner scanner = getScanner(scannerId);
	    if (scanner == null) {
	      TIllegalArgument ex = new TIllegalArgument("Invalid scanner Id");	      
	      throw ex;
	    }

	    try {
	      return ThriftUtilities.resultsFromHBase(scanner.next(numRows));
	    } catch (IOException e) {
	      throw getTIOError(e);
	    }
	  }

	  @Override
	  public List<TResult> getScannerResults(byte[] table, TScan scan, int numRows)
	      throws TIOError {
	    Table htable = getTable(table);
	    List<TResult> results = null;
	    ResultScanner scanner = null;
	    try {
	      scanner = htable.getScanner(ThriftUtilities.scanFromThrift(scan));
	      results = ThriftUtilities.resultsFromHBase(scanner.next(numRows));
	    } catch (IOException e) {
	      throw getTIOError(e);
	    } finally {
	      if (scanner != null) {
	        scanner.close();
	      }
	      closeTable(htable);
	    }
	    return results;
	  }



	  @Override
	  public void closeScanner(int scannerId) throws TIOError, TIllegalArgument {
	    LOG.debug("scannerClose: id=" + scannerId);
	    ResultScanner scanner = getScanner(scannerId);
	    if (scanner == null) {
	      String message = "scanner ID is invalid";
	      LOG.warn(message);
	      TIllegalArgument ex = new TIllegalArgument("Invalid scanner Id");
	      throw ex;
	    }
	    scanner.close();
	    removeScanner(scannerId);
	  }

	  @Override
	  public void mutateRow(byte[] table, TRowMutations rowMutations) throws TIOError {
	    Table htable = getTable(table);
	    try {
	      htable.mutateRow(ThriftUtilities.rowMutationsFromThrift(rowMutations));
	    } catch (IOException e) {
	      throw getTIOError(e);
	    } finally {
	      closeTable(htable);
	    }
	  }

	  @Override
	  public List<THRegionLocation> getAllRegionLocations(byte[] table)
	      throws TIOError {
	    RegionLocator locator = null;
	    try {
	      locator = getLocator(table);
	      return ThriftUtilities.regionLocationsFromHBase(locator.getAllRegionLocations());

	    } catch (IOException e) {
	      throw getTIOError(e);
	    } finally {
	      if (locator != null) {
	        try {
	          locator.close();
	        } catch (IOException e) {
	          LOG.warn("Couldn't close the locator.", e);
	        }
	      }
	    }
	  }

	  @Override
	  public THRegionLocation getRegionLocation(byte[] table, byte[] row, boolean reload)
	      throws TIOError {

	    RegionLocator locator = null;
	    try {
	      locator = getLocator(table);
	      
	      HRegionLocation hrl = locator.getRegionLocation(row, reload);
	      return ThriftUtilities.regionLocationFromHBase(hrl);

	    } catch (IOException e) {
	      throw getTIOError(e);
	    } finally {
	      if (locator != null) {
	        try {
	          locator.close();
	        } catch (IOException e) {
	          LOG.warn("Couldn't close the locator.", e);
	        }
	      }
	    }
	  }	  
}
