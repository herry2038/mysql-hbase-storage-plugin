package org.apache.hadoop.hbase.swift;

import static org.apache.hadoop.hbase.util.Bytes.getBytes;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HConstants;
import org.apache.hadoop.hbase.HRegionInfo;
import org.apache.hadoop.hbase.HRegionLocation;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.KeyValue;
import org.apache.hadoop.hbase.ServerName;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.TableNotFoundException;
import org.apache.hadoop.hbase.client.Admin;
import org.apache.hadoop.hbase.client.Append;
import org.apache.hadoop.hbase.client.Delete;
import org.apache.hadoop.hbase.client.Durability;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.HBaseAdmin;
import org.apache.hadoop.hbase.client.Increment;
import org.apache.hadoop.hbase.client.OperationWithAttributes;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.RegionLocator;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.client.Table;
import org.apache.hadoop.hbase.filter.Filter;
import org.apache.hadoop.hbase.filter.ParseFilter;
import org.apache.hadoop.hbase.filter.PrefixFilter;
import org.apache.hadoop.hbase.filter.WhileMatchFilter;
import org.apache.hadoop.hbase.security.UserProvider;
import org.apache.hadoop.hbase.swift.generated.Hbase;
import org.apache.hadoop.hbase.swift.IncrementCoalescer;
import org.apache.hadoop.hbase.swift.ThriftUtilities;
import org.apache.hadoop.hbase.swift.generated.AlreadyExists;
import org.apache.hadoop.hbase.swift.generated.BatchMutation;
import org.apache.hadoop.hbase.swift.generated.ColumnDescriptor;
import org.apache.hadoop.hbase.swift.generated.IOError;
import org.apache.hadoop.hbase.swift.generated.IllegalArgument;
import org.apache.hadoop.hbase.swift.generated.Mutation;
import org.apache.hadoop.hbase.swift.generated.TAppend;
import org.apache.hadoop.hbase.swift.generated.TCell;
import org.apache.hadoop.hbase.swift.generated.TIncrement;
import org.apache.hadoop.hbase.swift.generated.TRegionInfo;
import org.apache.hadoop.hbase.swift.generated.TRowResult;
import org.apache.hadoop.hbase.swift.generated.TScan;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.hbase.util.ConnectionCache;
import org.apache.thrift.TException;

import com.google.common.base.Throwables;

public class SwiftHbaseService implements Hbase {
	static final String COALESCE_INC_KEY = "hbase.regionserver.thrift.coalesceIncrement";
	
	protected static class ResultScannerWrapper {

		private final ResultScanner scanner;
		private final boolean sortColumns;
		public ResultScannerWrapper(ResultScanner resultScanner,
				boolean sortResultColumns) {
			scanner = resultScanner;
			sortColumns = sortResultColumns;
		}

		public ResultScanner getScanner() {
			return scanner;
		}

		public boolean isColumnSorted() {
			return sortColumns;
		}
	}

	  
    protected Configuration conf;
    protected static final Log LOG = LogFactory.getLog(SwiftHbaseService.class);

    // nextScannerId and scannerMap are used to manage scanner state
    protected int nextScannerId = 0;
    protected HashMap<Integer, ResultScannerWrapper> scannerMap = null;
    

    private final ConnectionCache connectionCache;
    IncrementCoalescer coalescer = null;

    static final String CLEANUP_INTERVAL = "hbase.thrift.connection.cleanup-interval";
    static final String MAX_IDLETIME = "hbase.thrift.connection.max-idletime";
	
	protected SwiftHbaseService(final Configuration c,
			final UserProvider userProvider) throws IOException {
		this.conf = c;
		scannerMap = new HashMap<Integer, ResultScannerWrapper>();
		this.coalescer = new IncrementCoalescer(this);

		int cleanInterval = conf.getInt(CLEANUP_INTERVAL, 10 * 1000);
		int maxIdleTime = conf.getInt(MAX_IDLETIME, 10 * 60 * 1000);
		connectionCache = new ConnectionCache(
				conf, userProvider, cleanInterval, maxIdleTime);
	}

    public Table getTable(final byte[] tableName) throws
    IOException {
    	String table = Bytes.toString(tableName);
    	return connectionCache.getTable(table);
    }
    
    /**
     * Assigns a unique ID to the scanner and adds the mapping to an internal
     * hash-map.
     *
     * @param scanner
     * @return integer scanner id
     */
    protected synchronized int addScanner(ResultScanner scanner,boolean sortColumns) {
      int id = nextScannerId++;
      ResultScannerWrapper resultScannerWrapper = new ResultScannerWrapper(scanner, sortColumns);
      scannerMap.put(id, resultScannerWrapper);
      return id;
    }

    /**
     * Returns the scanner associated with the specified ID.
     *
     * @param id
     * @return a Scanner, or null if ID was invalid.
     */
    protected synchronized ResultScannerWrapper getScanner(int id) {
      return scannerMap.get(id);
    }

    /**
     * Removes the scanner associated with the specified ID from the internal
     * id-&gt;scanner hash-map.
     *
     * @param id
     * @return a Scanner, or null if ID was invalid.
     */
    protected synchronized ResultScannerWrapper removeScanner(int id) {
      return scannerMap.remove(id);
    }
    
	/**
	 * Obtain HBaseAdmin. Creates the instance if it is not already created.
	 */
	private Admin getAdmin() throws IOException {
		return connectionCache.getAdmin();
	}

	void setEffectiveUser(String effectiveUser) {
		connectionCache.setEffectiveUser(effectiveUser);
	}

	@Override
	public void enableTable(byte[] tableName) throws IOError {
		try{
			getAdmin().enableTable(getTableName(tableName));
		} catch (IOException e) {
			LOG.warn(e.getMessage(), e);
			throw new IOError(Throwables.getStackTraceAsString(e));
		}
	}

	@Override
	public void disableTable(byte[] tableName) throws IOError{
		try{
			getAdmin().disableTable(getTableName(tableName));
		} catch (IOException e) {
			LOG.warn(e.getMessage(), e);
			throw new IOError(Throwables.getStackTraceAsString(e));
		}
	}

	@Override
	public boolean isTableEnabled(byte[] tableName) throws IOError {
		try {
			return this.connectionCache.getAdmin().isTableEnabled(getTableName(tableName));
		} catch (IOException e) {
			LOG.warn(e.getMessage(), e);
			throw new IOError(Throwables.getStackTraceAsString(e));
		}
	}

	@Override
	public void compact(byte[] tableNameOrRegionName) throws IOError {
		try {
			// TODO: HBaseAdmin.compact(byte[]) deprecated and not trivial to replace here.
			// ThriftServerRunner.compact should be deprecated and replaced with methods specific to
			// table and region.
			((HBaseAdmin) getAdmin()).compact(tableNameOrRegionName);
		} catch (IOException e) {
			LOG.warn(e.getMessage(), e);
			throw new IOError(Throwables.getStackTraceAsString(e));
		}
	}

	@Override
	public void majorCompact(byte[] tableNameOrRegionName) throws IOError {
		try {
			// TODO: HBaseAdmin.majorCompact(byte[]) deprecated and not trivial to replace here.
			// ThriftServerRunner.majorCompact should be deprecated and replaced with methods specific
			// to table and region.
			((HBaseAdmin) getAdmin()).majorCompact(tableNameOrRegionName);
		} catch (IOException e) {
			LOG.warn(e.getMessage(), e);
			throw new IOError(Throwables.getStackTraceAsString(e));
		}
	}

	@Override
	public List<byte[]> getTableNames() throws IOError {
		try {
			TableName[] tableNames = this.getAdmin().listTableNames();
			ArrayList<byte[]> list = new ArrayList<byte[]>(tableNames.length);
			for (int i = 0; i < tableNames.length; i++) {
				list.add(tableNames[i].getName());
			}
			return list;
		} catch (IOException e) {
			LOG.warn(e.getMessage(), e);
			throw new IOError(Throwables.getStackTraceAsString(e));
		}
	}

	/**
	 * @return the list of regions in the given table, or an empty list if the table does not exist
	 */
	@Override
	public List<TRegionInfo> getTableRegions(byte[] tableName)
			throws IOError {
		try (RegionLocator locator = connectionCache.getRegionLocator(tableName)) {
			List<HRegionLocation> regionLocations = locator.getAllRegionLocations();
			List<TRegionInfo> results = new ArrayList<TRegionInfo>();
			for (HRegionLocation regionLocation : regionLocations) {
				HRegionInfo info = regionLocation.getRegionInfo();
				ServerName serverName = regionLocation.getServerName();
				TRegionInfo.Builder region = new TRegionInfo.Builder();
				region.setServerName(Bytes.toBytes(serverName.getHostname()))
					.setPort(serverName.getPort())
					.setStartKey(info.getStartKey())
					.setEndKey(info.getEndKey())
					.setId(info.getRegionId())
					.setName(info.getRegionName())
					.setVersion(info.getVersion()) ;
				results.add(region.build()) ;					
			}
			return results;
		} catch (TableNotFoundException e) {
			// Return empty list for non-existing table
			return Collections.emptyList();
		} catch (IOException e){
			LOG.warn(e.getMessage(), e);
			throw new IOError(Throwables.getStackTraceAsString(e));
		}
	}

	@Override
	public List<TCell> get(
			byte[] tableName, byte[] row, byte[] column,
			Map<byte[], byte[]> attributes)
					throws IOError {
		byte [][] famAndQf = KeyValue.parseColumn(column);
		if (famAndQf.length == 1) {
			return get(tableName, row, famAndQf[0], null, attributes);
		}
		if (famAndQf.length == 2) {
			return get(tableName, row, famAndQf[0], famAndQf[1], attributes);
		}
		throw new IllegalArgumentException("Invalid familyAndQualifier provided.");
	}

	/**
	 * Note: this internal interface is slightly different from public APIs in regard to handling
	 * of the qualifier. Here we differ from the public Java API in that null != byte[0]. Rather,
	 * we respect qual == null as a request for the entire column family. The caller (
	 * {@link #get(byte[], byte[], byte[], Map)}) interface IS consistent in that the
	 * column is parse like normal.
	 */
	protected List<TCell> get(byte[] tableName,
			byte[] row,
			byte[] family,
			byte[] qualifier,
			Map<byte[], byte[]> attributes) throws IOError {
		Table table = null;
		try {
			table = getTable(tableName);
			Get get = new Get(row);
			addAttributes(get, attributes);
			if (qualifier == null) {
				get.addFamily(family);
			} else {
				get.addColumn(family, qualifier);
			}
			Result result = table.get(get);
			return ThriftUtilities.cellFromHBase(result.rawCells());
		} catch (IOException e) {
			LOG.warn(e.getMessage(), e);
			throw new IOError(Throwables.getStackTraceAsString(e));
		} finally {
			closeTable(table);
		}
	}

	@Override
	public List<TCell> getVer(byte[] tableName, byte[] row, byte[] column,
			int numVersions, Map<byte[], byte[]> attributes) throws IOError {
		byte [][] famAndQf = KeyValue.parseColumn(column);
		if(famAndQf.length == 1) {
			return getVer(tableName, row, famAndQf[0], null, numVersions, attributes);
		}
		if (famAndQf.length == 2) {
			return getVer(tableName, row, famAndQf[0], famAndQf[1], numVersions, attributes);
		}
		throw new IllegalArgumentException("Invalid familyAndQualifier provided.");

	}

	/**
	 * Note: this public interface is slightly different from public Java APIs in regard to
	 * handling of the qualifier. Here we differ from the public Java API in that null != byte[0].
	 * Rather, we respect qual == null as a request for the entire column family. If you want to
	 * access the entire column family, use
	 * {@link #getVer(byte[], byte[], byte[], int, Map)} with a {@code column} value
	 * that lacks a {@code ':'}.
	 */
	public List<TCell> getVer(byte[] tableName, byte[] row, byte[] family,
			byte[] qualifier, int numVersions, Map<byte[], byte[]> attributes) throws IOError {

		Table table = null;
		try {
			table = getTable(tableName);
			Get get = new Get(row);
			addAttributes(get, attributes);
			if (null == qualifier) {
				get.addFamily(family);
			} else {
				get.addColumn(family, qualifier);
			}
			get.setMaxVersions(numVersions);
			Result result = table.get(get);
			return ThriftUtilities.cellFromHBase(result.rawCells());
		} catch (IOException e) {
			LOG.warn(e.getMessage(), e);
			throw new IOError(Throwables.getStackTraceAsString(e));
		} finally{
			closeTable(table);
		}
	}

	@Override
	public List<TCell> getVerTs(byte[] tableName, byte[] row, byte[] column,
			long timestamp, int numVersions, Map<byte[], byte[]> attributes) throws IOError {
		byte [][] famAndQf = KeyValue.parseColumn(column);
		if (famAndQf.length == 1) {
			return getVerTs(tableName, row, famAndQf[0], null, timestamp, numVersions, attributes);
		}
		if (famAndQf.length == 2) {
			return getVerTs(tableName, row, famAndQf[0], famAndQf[1], timestamp, numVersions,
					attributes);
		}
		throw new IllegalArgumentException("Invalid familyAndQualifier provided.");
	}

	/**
	 * Note: this internal interface is slightly different from public APIs in regard to handling
	 * of the qualifier. Here we differ from the public Java API in that null != byte[0]. Rather,
	 * we respect qual == null as a request for the entire column family. The caller (
	 * {@link #getVerTs(byte[], byte[], byte[], long, int, Map)}) interface IS
	 * consistent in that the column is parse like normal.
	 */
	protected List<TCell> getVerTs(byte[] tableName, byte[] row, byte[] family,
			byte[] qualifier, long timestamp, int numVersions, Map<byte[], byte[]> attributes)
					throws IOError {

		Table table = null;
		try {
			table = getTable(tableName);
			Get get = new Get(row);
			addAttributes(get, attributes);
			if (null == qualifier) {
				get.addFamily(family);
			} else {
				get.addColumn(family, qualifier);
			}
			get.setTimeRange(0, timestamp);
			get.setMaxVersions(numVersions);
			Result result = table.get(get);
			return ThriftUtilities.cellFromHBase(result.rawCells());
		} catch (IOException e) {
			LOG.warn(e.getMessage(), e);
			throw new IOError(Throwables.getStackTraceAsString(e));
		} finally{
			closeTable(table);
		}
	}

	@Override
	public List<TRowResult> getRow(byte[] tableName, byte[] row,
			Map<byte[], byte[]> attributes) throws IOError {
		return getRowWithColumnsTs(tableName, row, null,
				HConstants.LATEST_TIMESTAMP,
				attributes);
	}

	@Override
	public List<TRowResult> getRowWithColumns(byte[] tableName,
			byte[] row,
			List<byte[]> columns,
			Map<byte[], byte[]> attributes) throws IOError {
		return getRowWithColumnsTs(tableName, row, columns,
				HConstants.LATEST_TIMESTAMP,
				attributes);
	}

	@Override
	public List<TRowResult> getRowTs(byte[] tableName, byte[] row,
			long timestamp, Map<byte[], byte[]> attributes) throws IOError {
		return getRowWithColumnsTs(tableName, row, null,
				timestamp, attributes);
	}

	@Override
	public List<TRowResult> getRowWithColumnsTs(
			byte[] tableName, byte[] row, List<byte[]> columns,
			long timestamp, Map<byte[], byte[]> attributes) throws IOError {

		Table table = null;
		try {
			table = getTable(tableName);
			if (columns == null) {
				Get get = new Get(row);
				addAttributes(get, attributes);
				get.setTimeRange(0, timestamp);
				Result result = table.get(get);
				return ThriftUtilities.rowResultFromHBase(result);
			}
			Get get = new Get(row);
			addAttributes(get, attributes);
			for(byte[] column : columns) {
				byte [][] famAndQf = KeyValue.parseColumn(column);
				if (famAndQf.length == 1) {
					get.addFamily(famAndQf[0]);
				} else {
					get.addColumn(famAndQf[0], famAndQf[1]);
				}
			}
			get.setTimeRange(0, timestamp);
			Result result = table.get(get);
			return ThriftUtilities.rowResultFromHBase(result);
		} catch (IOException e) {
			LOG.warn(e.getMessage(), e);
			throw new IOError(Throwables.getStackTraceAsString(e));
		} finally{
			closeTable(table);
		}
	}

	@Override
	public List<TRowResult> getRows(byte[] tableName,
			List<byte[]> rows,
			Map<byte[], byte[]> attributes)
					throws IOError {
		return getRowsWithColumnsTs(tableName, rows, null,
				HConstants.LATEST_TIMESTAMP,
				attributes);
	}

	@Override
	public List<TRowResult> getRowsWithColumns(byte[] tableName,
			List<byte[]> rows,
			List<byte[]> columns,
			Map<byte[], byte[]> attributes) throws IOError {
		return getRowsWithColumnsTs(tableName, rows, columns,
				HConstants.LATEST_TIMESTAMP,
				attributes);
	}

	@Override
	public List<TRowResult> getRowsTs(byte[] tableName,
			List<byte[]> rows,
			long timestamp,
			Map<byte[], byte[]> attributes) throws IOError {
		return getRowsWithColumnsTs(tableName, rows, null,
				timestamp, attributes);
	}

	@Override
	public List<TRowResult> getRowsWithColumnsTs(byte[] tableName,
			List<byte[]> rows,
			List<byte[]> columns, long timestamp,
			Map<byte[], byte[]> attributes) throws IOError {

		Table table= null;
		try {
			List<Get> gets = new ArrayList<Get>(rows.size());
			table = getTable(tableName);
			
			for (byte[] row : rows) {
				Get get = new Get(row);
				addAttributes(get, attributes);
				if (columns != null) {

					for(byte[] column : columns) {
						byte [][] famAndQf = KeyValue.parseColumn(column);
						if (famAndQf.length == 1) {
							get.addFamily(famAndQf[0]);
						} else {
							get.addColumn(famAndQf[0], famAndQf[1]);
						}
					}
				}
				get.setTimeRange(0, timestamp);
				gets.add(get);
			}
			Result[] result = table.get(gets);
			return ThriftUtilities.rowResultFromHBase(result);
		} catch (IOException e) {
			LOG.warn(e.getMessage(), e);
			throw new IOError(Throwables.getStackTraceAsString(e));
		} finally{
			closeTable(table);
		}
	}

	@Override
	public void deleteAll(
			byte[] tableName, byte[] row, byte[] column,
			Map<byte[], byte[]> attributes)
					throws IOError {
		deleteAllTs(tableName, row, column, HConstants.LATEST_TIMESTAMP,
				attributes);
	}

	@Override
	public void deleteAllTs(byte[] tableName,
			byte[] row,
			byte[] column,
			long timestamp, Map<byte[], byte[]> attributes) throws IOError {
		Table table = null;
		try {
			table = getTable(tableName);
			Delete delete  = new Delete(row);
			addAttributes(delete, attributes);
			byte [][] famAndQf = KeyValue.parseColumn(column);
			if (famAndQf.length == 1) {
				delete.deleteFamily(famAndQf[0], timestamp);
			} else {
				delete.deleteColumns(famAndQf[0], famAndQf[1], timestamp);
			}
			table.delete(delete);

		} catch (IOException e) {
			LOG.warn(e.getMessage(), e);
			throw new IOError(Throwables.getStackTraceAsString(e));
		} finally {
			closeTable(table);
		}
	}

	@Override
	public void deleteAllRow(
			byte[] tableName, byte[] row,
			Map<byte[], byte[]> attributes) throws IOError {
		deleteAllRowTs(tableName, row, HConstants.LATEST_TIMESTAMP, attributes);
	}

	@Override
	public void deleteAllRowTs(
			byte[] tableName, byte[] row, long timestamp,
			Map<byte[], byte[]> attributes) throws IOError {
		Table table = null;
		try {
			table = getTable(tableName);
			Delete delete  = new Delete(row, timestamp);
			addAttributes(delete, attributes);
			table.delete(delete);		
		} catch (IOException e) {
			LOG.warn(e.getMessage(), e);
			throw new IOError(Throwables.getStackTraceAsString(e));
		} finally {
			closeTable(table);
		}
	}

	@Override
	public void createTable(byte[] in_tableName,
			List<ColumnDescriptor> columnFamilies) throws IOError,
	IllegalArgument, AlreadyExists {
		TableName tableName = getTableName(in_tableName);
		try {
			if (getAdmin().tableExists(tableName)) {
				throw new AlreadyExists("table name already in use");
			}
			HTableDescriptor desc = new HTableDescriptor(tableName);
			for (ColumnDescriptor col : columnFamilies) {
				HColumnDescriptor colDesc = ThriftUtilities.colDescFromThrift(col);
				desc.addFamily(colDesc);
			}
			getAdmin().createTable(desc);
		} catch (IOException e) {
			LOG.warn(e.getMessage(), e);
			throw new IOError(Throwables.getStackTraceAsString(e));
		} catch (IllegalArgumentException e) {
			LOG.warn(e.getMessage(), e);
			throw new IllegalArgument(Throwables.getStackTraceAsString(e));
		}
	}

	private static TableName getTableName(byte[] buffer) {
		return TableName.valueOf(buffer);
	}

	@Override
	public void deleteTable(byte[] in_tableName) throws IOError {
		TableName tableName = getTableName(in_tableName);
		if (LOG.isDebugEnabled()) {
			LOG.debug("deleteTable: table=" + tableName);
		}
		try {
			if (!getAdmin().tableExists(tableName)) {
				throw new IOException("table does not exist");
			}
			getAdmin().deleteTable(tableName);			
		} catch (IOException e) {
			LOG.warn(e.getMessage(), e);
			throw new IOError(Throwables.getStackTraceAsString(e));
		}
	}

	@Override
	public void mutateRow(byte[] tableName, byte[] row,
			List<Mutation> mutations, Map<byte[], byte[]> attributes)
					throws IOError, IllegalArgument {
		mutateRowTs(tableName, row, mutations, HConstants.LATEST_TIMESTAMP,
				attributes);
	}

	@Override
	public void mutateRowTs(byte[] tableName, byte[] row,
			List<Mutation> mutations, long timestamp,
			Map<byte[], byte[]> attributes)
					throws IOError, IllegalArgument {
		Table table = null;
		try {
			table = getTable(tableName);
			Put put = new Put(row, timestamp);
			addAttributes(put, attributes);

			Delete delete = new Delete(row);
			addAttributes(delete, attributes);


			// I apologize for all this mess :)
			for (Mutation m : mutations) {
				byte[][] famAndQf = KeyValue.parseColumn(m.getColumn());
				if (m.isIsDelete()) {
					if (famAndQf.length == 1) {
						delete.deleteFamily(famAndQf[0], timestamp);
					} else {
						delete.deleteColumns(famAndQf[0], famAndQf[1], timestamp);
					}
					delete.setDurability(m.isWriteToWAL() ? Durability.SYNC_WAL
							: Durability.SKIP_WAL);
				} else {
					if(famAndQf.length == 1) {
						LOG.warn("No column qualifier specified. Delete is the only mutation supported "
								+ "over the whole column family.");
					} else {
						put.addImmutable(famAndQf[0], famAndQf[1],
								m.getValue() != null ? m.getValue()
										: HConstants.EMPTY_BYTE_ARRAY);
					}
					put.setDurability(m.isWriteToWAL() ? Durability.SYNC_WAL : Durability.SKIP_WAL);
				}
			}
			if (!delete.isEmpty())
				table.delete(delete);
			if (!put.isEmpty())
				table.put(put);
		} catch (IOException e) {
			LOG.warn(e.getMessage(), e);
			throw new IOError(Throwables.getStackTraceAsString(e));
		} catch (IllegalArgumentException e) {
			LOG.warn(e.getMessage(), e);
			throw new IllegalArgument(Throwables.getStackTraceAsString(e));
		} finally{
			closeTable(table);
		}
	}

	@Override
	public void mutateRows(byte[] tableName, List<BatchMutation> rowBatches,
			Map<byte[], byte[]> attributes)
					throws IOError, IllegalArgument {
		mutateRowsTs(tableName, rowBatches, HConstants.LATEST_TIMESTAMP, attributes);
	}

	@Override
	public void mutateRowsTs(
			byte[] tableName, List<BatchMutation> rowBatches, long timestamp,
			Map<byte[], byte[]> attributes)
					throws IOError, IllegalArgument {
		List<Put> puts = new ArrayList<Put>();
		List<Delete> deletes = new ArrayList<Delete>();

		for (BatchMutation batch : rowBatches) {
			byte[] row = batch.getRow();
			List<Mutation> mutations = batch.getMutations();
			Delete delete = new Delete(row);
			addAttributes(delete, attributes);
			Put put = new Put(row, timestamp);
			addAttributes(put, attributes);
			for (Mutation m : mutations) {
				byte[][] famAndQf = KeyValue.parseColumn(m.getColumn());
				if (m.isIsDelete()) {
					// no qualifier, family only.
					if (famAndQf.length == 1) {
						delete.deleteFamily(famAndQf[0], timestamp);
					} else {
						delete.deleteColumns(famAndQf[0], famAndQf[1], timestamp);
					}
					delete.setDurability(m.isWriteToWAL() ? Durability.SYNC_WAL
							: Durability.SKIP_WAL);
				} else {
					if (famAndQf.length == 1) {
						LOG.warn("No column qualifier specified. Delete is the only mutation supported "
								+ "over the whole column family.");
					}
					if (famAndQf.length == 2) {
						put.addImmutable(famAndQf[0], famAndQf[1],
								m.getValue() != null ? m.getValue()
										: HConstants.EMPTY_BYTE_ARRAY);
					} else {
						throw new IllegalArgumentException("Invalid famAndQf provided.");
					}
					put.setDurability(m.isWriteToWAL() ? Durability.SYNC_WAL : Durability.SKIP_WAL);
				}
			}
			if (!delete.isEmpty())
				deletes.add(delete);
			if (!put.isEmpty())
				puts.add(put);
		}

		Table table = null;
		try {
			table = getTable(tableName);
			if (!puts.isEmpty())
				table.put(puts);
			if (!deletes.isEmpty())
				table.delete(deletes);

		} catch (IOException e) {
			LOG.warn(e.getMessage(), e);
			throw new IOError(Throwables.getStackTraceAsString(e));
		} catch (IllegalArgumentException e) {
			LOG.warn(e.getMessage(), e);
			throw new IllegalArgument(Throwables.getStackTraceAsString(e));
		} finally{
			closeTable(table);
		}
	}

	@Override
	public long atomicIncrement(
			byte[] tableName, byte[] row, byte[] column, long amount)
					throws IOError, IllegalArgument {
		byte [][] famAndQf = KeyValue.parseColumn(column);
		if(famAndQf.length == 1) {
			return atomicIncrement(tableName, row, famAndQf[0], HConstants.EMPTY_BYTE_ARRAY, amount);
		}
		return atomicIncrement(tableName, row, famAndQf[0], famAndQf[1], amount);
	}

	protected long atomicIncrement(byte[] tableName, byte[] row,
			byte [] family, byte [] qualifier, long amount)
					throws IOError, IllegalArgument {
		Table table = null;
		try {
			table = getTable(tableName);
			return table.incrementColumnValue(
					row, family, qualifier, amount);
		} catch (IOException e) {
			LOG.warn(e.getMessage(), e);
			throw new IOError(Throwables.getStackTraceAsString(e));
		} finally {
			closeTable(table);
		}
	}

	@Override
	public void scannerClose(int id) throws IOError, IllegalArgument {
		LOG.debug("scannerClose: id=" + id);
		ResultScannerWrapper resultScannerWrapper = getScanner(id);
		if (resultScannerWrapper == null) {
			String message = "scanner ID is invalid";
			LOG.warn(message);
			throw new IllegalArgument("scanner ID is invalid");
		}
		resultScannerWrapper.getScanner().close();
		removeScanner(id);
	}

	@Override
	public List<TRowResult> scannerGetList(int id,int nbRows)
			throws IllegalArgument, IOError {
		LOG.debug("scannerGetList: id=" + id);
		ResultScannerWrapper resultScannerWrapper = getScanner(id);
		if (null == resultScannerWrapper) {
			String message = "scanner ID is invalid";
			LOG.warn(message);
			throw new IllegalArgument("scanner ID is invalid");
		}

		Result [] results = null;
		try {
			results = resultScannerWrapper.getScanner().next(nbRows);
			if (null == results) {
				return new ArrayList<TRowResult>();
			}
		} catch (IOException e) {
			LOG.warn(e.getMessage(), e);
			throw new IOError(Throwables.getStackTraceAsString(e));
		}
		return ThriftUtilities.rowResultFromHBase(results, resultScannerWrapper.isColumnSorted());
	}

	@Override
	public List<TRowResult> scannerGet(int id) throws IllegalArgument, IOError {
		return scannerGetList(id,1);
	}

	@Override
	public int scannerOpenWithScan(byte[] tableName, TScan tScan,
			Map<byte[], byte[]> attributes)
					throws IOError {

		Table table = null;
		try {
			table = getTable(tableName);
			Scan scan = new Scan();
			addAttributes(scan, attributes);
			if (tScan.getStartRow() != null ) {
				scan.setStartRow(tScan.getStartRow());
			}
			if (tScan.getStopRow() != null ) {
				scan.setStopRow(tScan.getStopRow());
			}
			if (tScan.getTimestamp() != null ) {
				scan.setTimeRange(0, tScan.getTimestamp());
			}
			if (tScan.getCaching() != null ) {
				scan.setCaching(tScan.getCaching());
			}
			if (tScan.getBatchSize() != null ) {
				scan.setBatch(tScan.getBatchSize());
			}
			if (tScan.getColumns() != null && tScan.getColumns().size() != 0) {
				for(byte[] column : tScan.getColumns()) {
					byte [][] famQf = KeyValue.parseColumn(column);
					if(famQf.length == 1) {
						scan.addFamily(famQf[0]);
					} else {
						scan.addColumn(famQf[0], famQf[1]);
					}
				}
			}
			if (tScan.getFilterString() != null) {
				ParseFilter parseFilter = new ParseFilter();
				scan.setFilter(
						parseFilter.parseFilterString(tScan.getFilterString()));
			}
			if (tScan.isReversed() != null) {
				scan.setReversed(tScan.isReversed());
			} else {
				scan.setReversed(false) ;
			}
			
			return addScanner(table.getScanner(scan), tScan.isSortColumns() == null ? false : tScan.isSortColumns());
		} catch (IOException e) {
			LOG.warn(e.getMessage(), e);
			throw new IOError(Throwables.getStackTraceAsString(e));
		} finally{
			closeTable(table);
		}
	}

	@Override
	public int scannerOpen(byte[] tableName, byte[] startRow,
			List<byte[]> columns,
			Map<byte[], byte[]> attributes) throws IOError {

		Table table = null;
		try {
			table = getTable(tableName);
			Scan scan = new Scan(startRow);
			addAttributes(scan, attributes);
			if(columns != null && columns.size() != 0) {
				for(byte[] column : columns) {
					byte [][] famQf = KeyValue.parseColumn(column);
					if(famQf.length == 1) {
						scan.addFamily(famQf[0]);
					} else {
						scan.addColumn(famQf[0], famQf[1]);
					}
				}
			}
			return addScanner(table.getScanner(scan), false);
		} catch (IOException e) {
			LOG.warn(e.getMessage(), e);
			throw new IOError(Throwables.getStackTraceAsString(e));
		} finally{
			closeTable(table);
		}
	}

	@Override
	public int scannerOpenWithStop(byte[] tableName, byte[] startRow,
			byte[] stopRow, List<byte[]> columns,
			Map<byte[], byte[]> attributes)
					throws IOError {

		Table table = null;
		try {
			table = getTable(tableName);
			Scan scan = new Scan(startRow, stopRow);
			addAttributes(scan, attributes);
			if(columns != null && columns.size() != 0) {
				for(byte[] column : columns) {
					byte [][] famQf = KeyValue.parseColumn(column);
					if(famQf.length == 1) {
						scan.addFamily(famQf[0]);
					} else {
						scan.addColumn(famQf[0], famQf[1]);
					}
				}
			}
			return addScanner(table.getScanner(scan), false);
		} catch (IOException e) {
			LOG.warn(e.getMessage(), e);
			throw new IOError(Throwables.getStackTraceAsString(e));
		} finally{
			closeTable(table);
		}
	}

	@Override
	public int scannerOpenWithPrefix(byte[] tableName,
			byte[] startAndPrefix,
			List<byte[]> columns,
			Map<byte[], byte[]> attributes)
					throws IOError {

		Table table = null;
		try {
			table = getTable(tableName);
			Scan scan = new Scan(startAndPrefix);
			addAttributes(scan, attributes);
			Filter f = new WhileMatchFilter(
					new PrefixFilter(startAndPrefix));
			scan.setFilter(f);
			if (columns != null && columns.size() != 0) {
				for(byte[] column : columns) {
					byte [][] famQf = KeyValue.parseColumn(column);
					if(famQf.length == 1) {
						scan.addFamily(famQf[0]);
					} else {
						scan.addColumn(famQf[0], famQf[1]);
					}
				}
			}
			return addScanner(table.getScanner(scan), false);
		} catch (IOException e) {
			LOG.warn(e.getMessage(), e);
			throw new IOError(Throwables.getStackTraceAsString(e));
		} finally{
			closeTable(table);
		}
	}

	@Override
	public int scannerOpenTs(byte[] tableName, byte[] startRow,
			List<byte[]> columns, long timestamp,
			Map<byte[], byte[]> attributes) throws IOError {

		Table table = null;
		try {
			table = getTable(tableName);
			Scan scan = new Scan(startRow);
			addAttributes(scan, attributes);
			scan.setTimeRange(0, timestamp);
			if (columns != null && columns.size() != 0) {
				for (byte[] column : columns) {
					byte [][] famQf = KeyValue.parseColumn(column);
					if(famQf.length == 1) {
						scan.addFamily(famQf[0]);
					} else {
						scan.addColumn(famQf[0], famQf[1]);
					}
				}
			}
			return addScanner(table.getScanner(scan), false);
		} catch (IOException e) {
			LOG.warn(e.getMessage(), e);
			throw new IOError(Throwables.getStackTraceAsString(e));
		} finally{
			closeTable(table);
		}
	}

	@Override
	public int scannerOpenWithStopTs(byte[] tableName, byte[] startRow,
			byte[] stopRow, List<byte[]> columns, long timestamp,
			Map<byte[], byte[]> attributes)
					throws IOError {

		Table table = null;
		try {
			table = getTable(tableName);
			Scan scan = new Scan(startRow, stopRow);
			addAttributes(scan, attributes);
			scan.setTimeRange(0, timestamp);
			if (columns != null && columns.size() != 0) {
				for (byte[] column : columns) {
					byte [][] famQf = KeyValue.parseColumn(column);
					if(famQf.length == 1) {
						scan.addFamily(famQf[0]);
					} else {
						scan.addColumn(famQf[0], famQf[1]);
					}
				}
			}
			scan.setTimeRange(0, timestamp);
			return addScanner(table.getScanner(scan), false);
		} catch (IOException e) {
			LOG.warn(e.getMessage(), e);
			throw new IOError(Throwables.getStackTraceAsString(e));
		} finally{
			closeTable(table);
		}
	}

	@Override
	public Map<byte[], ColumnDescriptor> getColumnDescriptors(
			byte[] tableName) throws IOError {

		Table table = null;
		try {
			TreeMap<byte[], ColumnDescriptor> columns =
					new TreeMap<byte[], ColumnDescriptor>();

			table = getTable(tableName);
			HTableDescriptor desc = table.getTableDescriptor();

			for (HColumnDescriptor e : desc.getFamilies()) {
				ColumnDescriptor col = ThriftUtilities.colDescFromHbase(e);
				columns.put(col.getName(), col);
			}
			return columns;
		} catch (IOException e) {
			LOG.warn(e.getMessage(), e);
			throw new IOError(Throwables.getStackTraceAsString(e));
		} finally {
			closeTable(table);
		}
	}

	@Deprecated
	@Override
	public List<TCell> getRowOrBefore(byte[] tableName, byte[] row,
			byte[] family) throws IOError {
		try {
			Result result = getRowOrBeforeResult(tableName, row, family);
			return ThriftUtilities.cellFromHBase(result.rawCells());
		} catch (IOException e) {
			LOG.warn(e.getMessage(), e);
			throw new IOError(Throwables.getStackTraceAsString(e));
		}
	}

	@Override
	public TRegionInfo getRegionInfo(byte[] searchRow) throws IOError {
		try {
			byte[] row = searchRow;
			Result startRowResult =
					getRowOrBeforeResult(TableName.META_TABLE_NAME.getName(), row, HConstants.CATALOG_FAMILY);

			if (startRowResult == null) {
				throw new IOException("Cannot find row in "+ TableName.META_TABLE_NAME+", row="
						+ Bytes.toStringBinary(row));
			}

			// find region start and end keys
			HRegionInfo regionInfo = HRegionInfo.getHRegionInfo(startRowResult);
			if (regionInfo == null) {
				throw new IOException("HRegionInfo REGIONINFO was null or " +
						" empty in Meta for row="
						+ Bytes.toStringBinary(row));
			}
			TRegionInfo.Builder region = new TRegionInfo.Builder();
			region.setStartKey(regionInfo.getStartKey());
			region.setEndKey(regionInfo.getEndKey());
			region.setId(regionInfo.getRegionId());
			region.setName(regionInfo.getRegionName());
			region.setVersion(regionInfo.getVersion());

			// find region assignment to server
			ServerName serverName = HRegionInfo.getServerName(startRowResult);
			if (serverName != null) {
				region.setServerName(Bytes.toBytes(serverName.getHostname()));
				region.setPort(serverName.getPort());
			}
			return region.build();
		} catch (IOException e) {
			LOG.warn(e.getMessage(), e);
			throw new IOError(Throwables.getStackTraceAsString(e));
		}
	}

	private void closeTable(Table table) throws IOError
	{
		try{
			if(table != null){
				table.close();
			}
		} catch (IOException e){
			LOG.error(e.getMessage(), e);
			throw new IOError(Throwables.getStackTraceAsString(e));
		}
	}
	
	private Result getRowOrBeforeResult(byte[] tableName, byte[] row, byte[] family) throws IOException {
		Scan scan = new Scan(row);
		scan.setReversed(true);
		scan.addFamily(family);
		scan.setStartRow(row);
		Table table = getTable(tableName);
		try (ResultScanner scanner = table.getScanner(scan)) {
			return scanner.next();
		} finally{
			if(table != null){
				table.close();
			}
		}
	}


	@Override
	public void increment(TIncrement tincrement) throws IOError {

		if (tincrement.getRow().length == 0 || tincrement.getTable().length == 0) {
			throw new IOError("Must supply a table and a row key; can't increment");
		}

		Table table = null;
		try {
			if (conf.getBoolean(COALESCE_INC_KEY, false)) {
				this.coalescer.queueIncrement(tincrement);
				return;
			}
		
			table = getTable(tincrement.getTable());
			Increment inc = ThriftUtilities.incrementFromThrift(tincrement);
			table.increment(inc);
		} catch (IOException e) {
			LOG.warn(e.getMessage(), e);
			throw new IOError(Throwables.getStackTraceAsString(e));
		} catch ( TException e) {
			LOG.warn(e.getMessage(), e);
			throw new IOError(Throwables.getStackTraceAsString(e));
		} finally{
			closeTable(table);
		}
	}

	@Override
	public void incrementRows(List<TIncrement> tincrements) throws IOError {
		try {
			if (conf.getBoolean(COALESCE_INC_KEY, false)) {
				this.coalescer.queueIncrements(tincrements);
				return;
			}
		} catch ( TException e) {
			LOG.warn(e.getMessage(), e);
			throw new IOError(Throwables.getStackTraceAsString(e));
		}
		for (TIncrement tinc : tincrements) {
			increment(tinc);
		}
	}

	@Override
	public List<TCell> append(TAppend tappend) throws IOError {
		if (tappend.getRow().length == 0 || tappend.getTable().length == 0) {
			throw new IOError("Must supply a table and a row key; can't append");
		}

		Table table = null;
		try {
			table = getTable(tappend.getTable());
			Append append = ThriftUtilities.appendFromThrift(tappend);
			Result result = table.append(append);
			return ThriftUtilities.cellFromHBase(result.rawCells());
		} catch (IOException e) {
			LOG.warn(e.getMessage(), e);
			throw new IOError(Throwables.getStackTraceAsString(e));
		} finally{
			closeTable(table);
		}
	}

	@Override
	public boolean checkAndPut(byte[] tableName, byte[] row, byte[] column,
			byte[] value, Mutation mput, Map<byte[], byte[]> attributes) throws IOError,
	IllegalArgument {
		Put put;
		try {
			put = new Put(row, HConstants.LATEST_TIMESTAMP);
			addAttributes(put, attributes);

			byte[][] famAndQf = KeyValue.parseColumn(mput.getColumn());

			put.addImmutable(famAndQf[0], famAndQf[1], mput.getValue() != null ? mput.getValue()
					: HConstants.EMPTY_BYTE_ARRAY);

			put.setDurability(mput.isWriteToWAL() ? Durability.SYNC_WAL : Durability.SKIP_WAL);
		} catch (IllegalArgumentException e) {
			LOG.warn(e.getMessage(), e);
			throw new IllegalArgument(Throwables.getStackTraceAsString(e));
		}

		Table table = null;
		try {
			table = getTable(tableName);
			byte[][] famAndQf = KeyValue.parseColumn(column);
			return table.checkAndPut(row, famAndQf[0], famAndQf[1],
					value != null ? value : HConstants.EMPTY_BYTE_ARRAY, put);
		} catch (IOException e) {
			LOG.warn(e.getMessage(), e);
			throw new IOError(Throwables.getStackTraceAsString(e));
		} catch (IllegalArgumentException e) {
			LOG.warn(e.getMessage(), e);
			throw new IllegalArgument(Throwables.getStackTraceAsString(e));
		} finally {
			closeTable(table);
		}
	}
	
	
	@Override
	public void truncateTable(byte[] in_tableName) throws IOError {
		TableName tableName = getTableName(in_tableName);
		if (LOG.isDebugEnabled()) {
			LOG.debug("deleteTable: table=" + tableName);
		}
		try {
			if (!getAdmin().tableExists(tableName)) {
				throw new IOException("table does not exist");
			}
			getAdmin().truncateTable(tableName, false);			
		} catch (IOException e) {
			LOG.warn(e.getMessage(), e);
			throw new IOError(Throwables.getStackTraceAsString(e));
		}
	}
	
	
	@Override
	public void renameTable(byte[] in_tableName, byte[] in_newTableName) throws IOError {
		TableName tableName = getTableName(in_tableName);
		TableName newTableName = getTableName(in_newTableName) ;
		if (LOG.isDebugEnabled()) {
			LOG.debug("deleteTable: table=" + tableName);
		}
		
		try {
			Admin admin = getAdmin() ;
			if (! admin.tableExists(tableName)) {
				throw new IOException("table does not exist");
			}
			if ( admin.tableExists(newTableName)) {
				throw new IOException("new table already exists");
			}
						
			String snapshotName = "tmp___" + newTableName.getNameAsString() ;			
			admin.disableTable(tableName);
			admin.snapshot(snapshotName, tableName);
			admin.cloneSnapshot(snapshotName, newTableName);
			admin.deleteSnapshot(snapshotName);
			admin.deleteTable(tableName);			
		} catch (IOException e) {
			LOG.warn(e.getMessage(), e);
			throw new IOError(Throwables.getStackTraceAsString(e));
		}
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

	public static void registerFilters(Configuration conf) {
		String[] filters = conf.getStrings("hbase.thrift.filters");
		if(filters != null) {
			for(String filterClass: filters) {
				String[] filterPart = filterClass.split(":");
				if(filterPart.length != 2) {
					LOG.warn("Invalid filter specification " + filterClass + " - skipping");
				} else {
					ParseFilter.registerFilter(filterPart[0], filterPart[1]);
				}
			}
		}
	}

	@Override
	public void deleteRows(byte[] tableName, List<byte[]> rows) throws IOError {
		Table table = null;
		try {
			table = getTable(tableName);
			List<Delete> deletes = new ArrayList<Delete>(); 
			for ( byte[] row: rows ) {
				Delete delete  = new Delete(row, HConstants.LATEST_TIMESTAMP);
				deletes.add(delete) ;
			}
			table.delete(deletes);		
		} catch (IOException e) {
			LOG.warn(e.getMessage(), e);
			throw new IOError(Throwables.getStackTraceAsString(e));
		} finally {
			closeTable(table);
		}
	}
}
