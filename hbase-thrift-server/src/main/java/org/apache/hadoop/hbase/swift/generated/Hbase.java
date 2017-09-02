package org.apache.hadoop.hbase.swift.generated;

import com.facebook.swift.codec.*;
import com.facebook.swift.codec.ThriftField.Requiredness;
import com.facebook.swift.service.*;
import com.google.common.util.concurrent.ListenableFuture;
import java.io.*;
import java.util.*;

@ThriftService("Hbase")
public interface Hbase
{
    @ThriftService("Hbase")
    public interface Async
    {
        @ThriftMethod(value = "enableTable",
                      exception = {
                          @ThriftException(type=IOError.class, id=1)
                      })
        ListenableFuture<Void> enableTable(
            @ThriftField(value=1, name="tableName", requiredness=Requiredness.NONE) final byte[] tableName
        );

        @ThriftMethod(value = "disableTable",
                      exception = {
                          @ThriftException(type=IOError.class, id=1)
                      })
        ListenableFuture<Void> disableTable(
            @ThriftField(value=1, name="tableName", requiredness=Requiredness.NONE) final byte[] tableName
        );

        @ThriftMethod(value = "isTableEnabled",
                      exception = {
                          @ThriftException(type=IOError.class, id=1)
                      })
        ListenableFuture<Boolean> isTableEnabled(
            @ThriftField(value=1, name="tableName", requiredness=Requiredness.NONE) final byte[] tableName
        );

        @ThriftMethod(value = "compact",
                      exception = {
                          @ThriftException(type=IOError.class, id=1)
                      })
        ListenableFuture<Void> compact(
            @ThriftField(value=1, name="tableNameOrRegionName", requiredness=Requiredness.NONE) final byte[] tableNameOrRegionName
        );

        @ThriftMethod(value = "majorCompact",
                      exception = {
                          @ThriftException(type=IOError.class, id=1)
                      })
        ListenableFuture<Void> majorCompact(
            @ThriftField(value=1, name="tableNameOrRegionName", requiredness=Requiredness.NONE) final byte[] tableNameOrRegionName
        );

        @ThriftMethod(value = "getTableNames",
                      exception = {
                          @ThriftException(type=IOError.class, id=1)
                      })
        ListenableFuture<List<byte[]>> getTableNames();

        @ThriftMethod(value = "getColumnDescriptors",
                      exception = {
                          @ThriftException(type=IOError.class, id=1)
                      })
        ListenableFuture<Map<byte[], ColumnDescriptor>> getColumnDescriptors(
            @ThriftField(value=1, name="tableName", requiredness=Requiredness.NONE) final byte[] tableName
        );

        @ThriftMethod(value = "getTableRegions",
                      exception = {
                          @ThriftException(type=IOError.class, id=1)
                      })
        ListenableFuture<List<TRegionInfo>> getTableRegions(
            @ThriftField(value=1, name="tableName", requiredness=Requiredness.NONE) final byte[] tableName
        );

        @ThriftMethod(value = "createTable",
                      exception = {
                          @ThriftException(type=IOError.class, id=1),
                          @ThriftException(type=IllegalArgument.class, id=2),
                          @ThriftException(type=AlreadyExists.class, id=3)
                      })
        ListenableFuture<Void> createTable(
            @ThriftField(value=1, name="tableName", requiredness=Requiredness.NONE) final byte[] tableName,
            @ThriftField(value=2, name="columnFamilies", requiredness=Requiredness.NONE) final List<ColumnDescriptor> columnFamilies
        );

        @ThriftMethod(value = "deleteTable",
                      exception = {
                          @ThriftException(type=IOError.class, id=1)
                      })
        ListenableFuture<Void> deleteTable(
            @ThriftField(value=1, name="tableName", requiredness=Requiredness.NONE) final byte[] tableName
        );

        @ThriftMethod(value = "get",
                      exception = {
                          @ThriftException(type=IOError.class, id=1)
                      })
        ListenableFuture<List<TCell>> get(
            @ThriftField(value=1, name="tableName", requiredness=Requiredness.NONE) final byte[] tableName,
            @ThriftField(value=2, name="row", requiredness=Requiredness.NONE) final byte[] row,
            @ThriftField(value=3, name="column", requiredness=Requiredness.NONE) final byte[] column,
            @ThriftField(value=4, name="attributes", requiredness=Requiredness.NONE) final Map<byte[], byte[]> attributes
        );

        @ThriftMethod(value = "getVer",
                      exception = {
                          @ThriftException(type=IOError.class, id=1)
                      })
        ListenableFuture<List<TCell>> getVer(
            @ThriftField(value=1, name="tableName", requiredness=Requiredness.NONE) final byte[] tableName,
            @ThriftField(value=2, name="row", requiredness=Requiredness.NONE) final byte[] row,
            @ThriftField(value=3, name="column", requiredness=Requiredness.NONE) final byte[] column,
            @ThriftField(value=4, name="numVersions", requiredness=Requiredness.NONE) final int numVersions,
            @ThriftField(value=5, name="attributes", requiredness=Requiredness.NONE) final Map<byte[], byte[]> attributes
        );

        @ThriftMethod(value = "getVerTs",
                      exception = {
                          @ThriftException(type=IOError.class, id=1)
                      })
        ListenableFuture<List<TCell>> getVerTs(
            @ThriftField(value=1, name="tableName", requiredness=Requiredness.NONE) final byte[] tableName,
            @ThriftField(value=2, name="row", requiredness=Requiredness.NONE) final byte[] row,
            @ThriftField(value=3, name="column", requiredness=Requiredness.NONE) final byte[] column,
            @ThriftField(value=4, name="timestamp", requiredness=Requiredness.NONE) final long timestamp,
            @ThriftField(value=5, name="numVersions", requiredness=Requiredness.NONE) final int numVersions,
            @ThriftField(value=6, name="attributes", requiredness=Requiredness.NONE) final Map<byte[], byte[]> attributes
        );

        @ThriftMethod(value = "getRow",
                      exception = {
                          @ThriftException(type=IOError.class, id=1)
                      })
        ListenableFuture<List<TRowResult>> getRow(
            @ThriftField(value=1, name="tableName", requiredness=Requiredness.NONE) final byte[] tableName,
            @ThriftField(value=2, name="row", requiredness=Requiredness.NONE) final byte[] row,
            @ThriftField(value=3, name="attributes", requiredness=Requiredness.NONE) final Map<byte[], byte[]> attributes
        );

        @ThriftMethod(value = "getRowWithColumns",
                      exception = {
                          @ThriftException(type=IOError.class, id=1)
                      })
        ListenableFuture<List<TRowResult>> getRowWithColumns(
            @ThriftField(value=1, name="tableName", requiredness=Requiredness.NONE) final byte[] tableName,
            @ThriftField(value=2, name="row", requiredness=Requiredness.NONE) final byte[] row,
            @ThriftField(value=3, name="columns", requiredness=Requiredness.NONE) final List<byte[]> columns,
            @ThriftField(value=4, name="attributes", requiredness=Requiredness.NONE) final Map<byte[], byte[]> attributes
        );

        @ThriftMethod(value = "getRowTs",
                      exception = {
                          @ThriftException(type=IOError.class, id=1)
                      })
        ListenableFuture<List<TRowResult>> getRowTs(
            @ThriftField(value=1, name="tableName", requiredness=Requiredness.NONE) final byte[] tableName,
            @ThriftField(value=2, name="row", requiredness=Requiredness.NONE) final byte[] row,
            @ThriftField(value=3, name="timestamp", requiredness=Requiredness.NONE) final long timestamp,
            @ThriftField(value=4, name="attributes", requiredness=Requiredness.NONE) final Map<byte[], byte[]> attributes
        );

        @ThriftMethod(value = "getRowWithColumnsTs",
                      exception = {
                          @ThriftException(type=IOError.class, id=1)
                      })
        ListenableFuture<List<TRowResult>> getRowWithColumnsTs(
            @ThriftField(value=1, name="tableName", requiredness=Requiredness.NONE) final byte[] tableName,
            @ThriftField(value=2, name="row", requiredness=Requiredness.NONE) final byte[] row,
            @ThriftField(value=3, name="columns", requiredness=Requiredness.NONE) final List<byte[]> columns,
            @ThriftField(value=4, name="timestamp", requiredness=Requiredness.NONE) final long timestamp,
            @ThriftField(value=5, name="attributes", requiredness=Requiredness.NONE) final Map<byte[], byte[]> attributes
        );

        @ThriftMethod(value = "getRows",
                      exception = {
                          @ThriftException(type=IOError.class, id=1)
                      })
        ListenableFuture<List<TRowResult>> getRows(
            @ThriftField(value=1, name="tableName", requiredness=Requiredness.NONE) final byte[] tableName,
            @ThriftField(value=2, name="rows", requiredness=Requiredness.NONE) final List<byte[]> rows,
            @ThriftField(value=3, name="attributes", requiredness=Requiredness.NONE) final Map<byte[], byte[]> attributes
        );

        @ThriftMethod(value = "getRowsWithColumns",
                      exception = {
                          @ThriftException(type=IOError.class, id=1)
                      })
        ListenableFuture<List<TRowResult>> getRowsWithColumns(
            @ThriftField(value=1, name="tableName", requiredness=Requiredness.NONE) final byte[] tableName,
            @ThriftField(value=2, name="rows", requiredness=Requiredness.NONE) final List<byte[]> rows,
            @ThriftField(value=3, name="columns", requiredness=Requiredness.NONE) final List<byte[]> columns,
            @ThriftField(value=4, name="attributes", requiredness=Requiredness.NONE) final Map<byte[], byte[]> attributes
        );

        @ThriftMethod(value = "getRowsTs",
                      exception = {
                          @ThriftException(type=IOError.class, id=1)
                      })
        ListenableFuture<List<TRowResult>> getRowsTs(
            @ThriftField(value=1, name="tableName", requiredness=Requiredness.NONE) final byte[] tableName,
            @ThriftField(value=2, name="rows", requiredness=Requiredness.NONE) final List<byte[]> rows,
            @ThriftField(value=3, name="timestamp", requiredness=Requiredness.NONE) final long timestamp,
            @ThriftField(value=4, name="attributes", requiredness=Requiredness.NONE) final Map<byte[], byte[]> attributes
        );

        @ThriftMethod(value = "getRowsWithColumnsTs",
                      exception = {
                          @ThriftException(type=IOError.class, id=1)
                      })
        ListenableFuture<List<TRowResult>> getRowsWithColumnsTs(
            @ThriftField(value=1, name="tableName", requiredness=Requiredness.NONE) final byte[] tableName,
            @ThriftField(value=2, name="rows", requiredness=Requiredness.NONE) final List<byte[]> rows,
            @ThriftField(value=3, name="columns", requiredness=Requiredness.NONE) final List<byte[]> columns,
            @ThriftField(value=4, name="timestamp", requiredness=Requiredness.NONE) final long timestamp,
            @ThriftField(value=5, name="attributes", requiredness=Requiredness.NONE) final Map<byte[], byte[]> attributes
        );

        @ThriftMethod(value = "mutateRow",
                      exception = {
                          @ThriftException(type=IOError.class, id=1),
                          @ThriftException(type=IllegalArgument.class, id=2)
                      })
        ListenableFuture<Void> mutateRow(
            @ThriftField(value=1, name="tableName", requiredness=Requiredness.NONE) final byte[] tableName,
            @ThriftField(value=2, name="row", requiredness=Requiredness.NONE) final byte[] row,
            @ThriftField(value=3, name="mutations", requiredness=Requiredness.NONE) final List<Mutation> mutations,
            @ThriftField(value=4, name="attributes", requiredness=Requiredness.NONE) final Map<byte[], byte[]> attributes
        );

        @ThriftMethod(value = "mutateRowTs",
                      exception = {
                          @ThriftException(type=IOError.class, id=1),
                          @ThriftException(type=IllegalArgument.class, id=2)
                      })
        ListenableFuture<Void> mutateRowTs(
            @ThriftField(value=1, name="tableName", requiredness=Requiredness.NONE) final byte[] tableName,
            @ThriftField(value=2, name="row", requiredness=Requiredness.NONE) final byte[] row,
            @ThriftField(value=3, name="mutations", requiredness=Requiredness.NONE) final List<Mutation> mutations,
            @ThriftField(value=4, name="timestamp", requiredness=Requiredness.NONE) final long timestamp,
            @ThriftField(value=5, name="attributes", requiredness=Requiredness.NONE) final Map<byte[], byte[]> attributes
        );

        @ThriftMethod(value = "mutateRows",
                      exception = {
                          @ThriftException(type=IOError.class, id=1),
                          @ThriftException(type=IllegalArgument.class, id=2)
                      })
        ListenableFuture<Void> mutateRows(
            @ThriftField(value=1, name="tableName", requiredness=Requiredness.NONE) final byte[] tableName,
            @ThriftField(value=2, name="rowBatches", requiredness=Requiredness.NONE) final List<BatchMutation> rowBatches,
            @ThriftField(value=3, name="attributes", requiredness=Requiredness.NONE) final Map<byte[], byte[]> attributes
        );

        @ThriftMethod(value = "mutateRowsTs",
                      exception = {
                          @ThriftException(type=IOError.class, id=1),
                          @ThriftException(type=IllegalArgument.class, id=2)
                      })
        ListenableFuture<Void> mutateRowsTs(
            @ThriftField(value=1, name="tableName", requiredness=Requiredness.NONE) final byte[] tableName,
            @ThriftField(value=2, name="rowBatches", requiredness=Requiredness.NONE) final List<BatchMutation> rowBatches,
            @ThriftField(value=3, name="timestamp", requiredness=Requiredness.NONE) final long timestamp,
            @ThriftField(value=4, name="attributes", requiredness=Requiredness.NONE) final Map<byte[], byte[]> attributes
        );

        @ThriftMethod(value = "atomicIncrement",
                      exception = {
                          @ThriftException(type=IOError.class, id=1),
                          @ThriftException(type=IllegalArgument.class, id=2)
                      })
        ListenableFuture<Long> atomicIncrement(
            @ThriftField(value=1, name="tableName", requiredness=Requiredness.NONE) final byte[] tableName,
            @ThriftField(value=2, name="row", requiredness=Requiredness.NONE) final byte[] row,
            @ThriftField(value=3, name="column", requiredness=Requiredness.NONE) final byte[] column,
            @ThriftField(value=4, name="value", requiredness=Requiredness.NONE) final long value
        );

        @ThriftMethod(value = "deleteAll",
                      exception = {
                          @ThriftException(type=IOError.class, id=1)
                      })
        ListenableFuture<Void> deleteAll(
            @ThriftField(value=1, name="tableName", requiredness=Requiredness.NONE) final byte[] tableName,
            @ThriftField(value=2, name="row", requiredness=Requiredness.NONE) final byte[] row,
            @ThriftField(value=3, name="column", requiredness=Requiredness.NONE) final byte[] column,
            @ThriftField(value=4, name="attributes", requiredness=Requiredness.NONE) final Map<byte[], byte[]> attributes
        );

        @ThriftMethod(value = "deleteAllTs",
                      exception = {
                          @ThriftException(type=IOError.class, id=1)
                      })
        ListenableFuture<Void> deleteAllTs(
            @ThriftField(value=1, name="tableName", requiredness=Requiredness.NONE) final byte[] tableName,
            @ThriftField(value=2, name="row", requiredness=Requiredness.NONE) final byte[] row,
            @ThriftField(value=3, name="column", requiredness=Requiredness.NONE) final byte[] column,
            @ThriftField(value=4, name="timestamp", requiredness=Requiredness.NONE) final long timestamp,
            @ThriftField(value=5, name="attributes", requiredness=Requiredness.NONE) final Map<byte[], byte[]> attributes
        );

        @ThriftMethod(value = "deleteAllRow",
                      exception = {
                          @ThriftException(type=IOError.class, id=1)
                      })
        ListenableFuture<Void> deleteAllRow(
            @ThriftField(value=1, name="tableName", requiredness=Requiredness.NONE) final byte[] tableName,
            @ThriftField(value=2, name="row", requiredness=Requiredness.NONE) final byte[] row,
            @ThriftField(value=3, name="attributes", requiredness=Requiredness.NONE) final Map<byte[], byte[]> attributes
        );

        @ThriftMethod(value = "increment",
                      exception = {
                          @ThriftException(type=IOError.class, id=1)
                      })
        ListenableFuture<Void> increment(
            @ThriftField(value=1, name="increment", requiredness=Requiredness.NONE) final TIncrement increment
        );

        @ThriftMethod(value = "incrementRows",
                      exception = {
                          @ThriftException(type=IOError.class, id=1)
                      })
        ListenableFuture<Void> incrementRows(
            @ThriftField(value=1, name="increments", requiredness=Requiredness.NONE) final List<TIncrement> increments
        );

        @ThriftMethod(value = "deleteAllRowTs",
                      exception = {
                          @ThriftException(type=IOError.class, id=1)
                      })
        ListenableFuture<Void> deleteAllRowTs(
            @ThriftField(value=1, name="tableName", requiredness=Requiredness.NONE) final byte[] tableName,
            @ThriftField(value=2, name="row", requiredness=Requiredness.NONE) final byte[] row,
            @ThriftField(value=3, name="timestamp", requiredness=Requiredness.NONE) final long timestamp,
            @ThriftField(value=4, name="attributes", requiredness=Requiredness.NONE) final Map<byte[], byte[]> attributes
        );

        @ThriftMethod(value = "scannerOpenWithScan",
                      exception = {
                          @ThriftException(type=IOError.class, id=1)
                      })
        ListenableFuture<Integer> scannerOpenWithScan(
            @ThriftField(value=1, name="tableName", requiredness=Requiredness.NONE) final byte[] tableName,
            @ThriftField(value=2, name="scan", requiredness=Requiredness.NONE) final TScan scan,
            @ThriftField(value=3, name="attributes", requiredness=Requiredness.NONE) final Map<byte[], byte[]> attributes
        );

        @ThriftMethod(value = "scannerOpen",
                      exception = {
                          @ThriftException(type=IOError.class, id=1)
                      })
        ListenableFuture<Integer> scannerOpen(
            @ThriftField(value=1, name="tableName", requiredness=Requiredness.NONE) final byte[] tableName,
            @ThriftField(value=2, name="startRow", requiredness=Requiredness.NONE) final byte[] startRow,
            @ThriftField(value=3, name="columns", requiredness=Requiredness.NONE) final List<byte[]> columns,
            @ThriftField(value=4, name="attributes", requiredness=Requiredness.NONE) final Map<byte[], byte[]> attributes
        );

        @ThriftMethod(value = "scannerOpenWithStop",
                      exception = {
                          @ThriftException(type=IOError.class, id=1)
                      })
        ListenableFuture<Integer> scannerOpenWithStop(
            @ThriftField(value=1, name="tableName", requiredness=Requiredness.NONE) final byte[] tableName,
            @ThriftField(value=2, name="startRow", requiredness=Requiredness.NONE) final byte[] startRow,
            @ThriftField(value=3, name="stopRow", requiredness=Requiredness.NONE) final byte[] stopRow,
            @ThriftField(value=4, name="columns", requiredness=Requiredness.NONE) final List<byte[]> columns,
            @ThriftField(value=5, name="attributes", requiredness=Requiredness.NONE) final Map<byte[], byte[]> attributes
        );

        @ThriftMethod(value = "scannerOpenWithPrefix",
                      exception = {
                          @ThriftException(type=IOError.class, id=1)
                      })
        ListenableFuture<Integer> scannerOpenWithPrefix(
            @ThriftField(value=1, name="tableName", requiredness=Requiredness.NONE) final byte[] tableName,
            @ThriftField(value=2, name="startAndPrefix", requiredness=Requiredness.NONE) final byte[] startAndPrefix,
            @ThriftField(value=3, name="columns", requiredness=Requiredness.NONE) final List<byte[]> columns,
            @ThriftField(value=4, name="attributes", requiredness=Requiredness.NONE) final Map<byte[], byte[]> attributes
        );

        @ThriftMethod(value = "scannerOpenTs",
                      exception = {
                          @ThriftException(type=IOError.class, id=1)
                      })
        ListenableFuture<Integer> scannerOpenTs(
            @ThriftField(value=1, name="tableName", requiredness=Requiredness.NONE) final byte[] tableName,
            @ThriftField(value=2, name="startRow", requiredness=Requiredness.NONE) final byte[] startRow,
            @ThriftField(value=3, name="columns", requiredness=Requiredness.NONE) final List<byte[]> columns,
            @ThriftField(value=4, name="timestamp", requiredness=Requiredness.NONE) final long timestamp,
            @ThriftField(value=5, name="attributes", requiredness=Requiredness.NONE) final Map<byte[], byte[]> attributes
        );

        @ThriftMethod(value = "scannerOpenWithStopTs",
                      exception = {
                          @ThriftException(type=IOError.class, id=1)
                      })
        ListenableFuture<Integer> scannerOpenWithStopTs(
            @ThriftField(value=1, name="tableName", requiredness=Requiredness.NONE) final byte[] tableName,
            @ThriftField(value=2, name="startRow", requiredness=Requiredness.NONE) final byte[] startRow,
            @ThriftField(value=3, name="stopRow", requiredness=Requiredness.NONE) final byte[] stopRow,
            @ThriftField(value=4, name="columns", requiredness=Requiredness.NONE) final List<byte[]> columns,
            @ThriftField(value=5, name="timestamp", requiredness=Requiredness.NONE) final long timestamp,
            @ThriftField(value=6, name="attributes", requiredness=Requiredness.NONE) final Map<byte[], byte[]> attributes
        );

        @ThriftMethod(value = "scannerGet",
                      exception = {
                          @ThriftException(type=IOError.class, id=1),
                          @ThriftException(type=IllegalArgument.class, id=2)
                      })
        ListenableFuture<List<TRowResult>> scannerGet(
            @ThriftField(value=1, name="id", requiredness=Requiredness.NONE) final int id
        );

        @ThriftMethod(value = "scannerGetList",
                      exception = {
                          @ThriftException(type=IOError.class, id=1),
                          @ThriftException(type=IllegalArgument.class, id=2)
                      })
        ListenableFuture<List<TRowResult>> scannerGetList(
            @ThriftField(value=1, name="id", requiredness=Requiredness.NONE) final int id,
            @ThriftField(value=2, name="nbRows", requiredness=Requiredness.NONE) final int nbRows
        );

        @ThriftMethod(value = "scannerClose",
                      exception = {
                          @ThriftException(type=IOError.class, id=1),
                          @ThriftException(type=IllegalArgument.class, id=2)
                      })
        ListenableFuture<Void> scannerClose(
            @ThriftField(value=1, name="id", requiredness=Requiredness.NONE) final int id
        );

        @ThriftMethod(value = "getRowOrBefore",
                      exception = {
                          @ThriftException(type=IOError.class, id=1)
                      })
        ListenableFuture<List<TCell>> getRowOrBefore(
            @ThriftField(value=1, name="tableName", requiredness=Requiredness.NONE) final byte[] tableName,
            @ThriftField(value=2, name="row", requiredness=Requiredness.NONE) final byte[] row,
            @ThriftField(value=3, name="family", requiredness=Requiredness.NONE) final byte[] family
        );

        @ThriftMethod(value = "getRegionInfo",
                      exception = {
                          @ThriftException(type=IOError.class, id=1)
                      })
        ListenableFuture<TRegionInfo> getRegionInfo(
            @ThriftField(value=1, name="row", requiredness=Requiredness.NONE) final byte[] row
        );

        @ThriftMethod(value = "append",
                      exception = {
                          @ThriftException(type=IOError.class, id=1)
                      })
        ListenableFuture<List<TCell>> append(
            @ThriftField(value=1, name="append", requiredness=Requiredness.NONE) final TAppend append
        );

        @ThriftMethod(value = "checkAndPut",
                      exception = {
                          @ThriftException(type=IOError.class, id=1),
                          @ThriftException(type=IllegalArgument.class, id=2)
                      })
        ListenableFuture<Boolean> checkAndPut(
            @ThriftField(value=1, name="tableName", requiredness=Requiredness.NONE) final byte[] tableName,
            @ThriftField(value=2, name="row", requiredness=Requiredness.NONE) final byte[] row,
            @ThriftField(value=3, name="column", requiredness=Requiredness.NONE) final byte[] column,
            @ThriftField(value=5, name="value", requiredness=Requiredness.NONE) final byte[] value,
            @ThriftField(value=6, name="mput", requiredness=Requiredness.NONE) final Mutation mput,
            @ThriftField(value=7, name="attributes", requiredness=Requiredness.NONE) final Map<byte[], byte[]> attributes
        );

        @ThriftMethod(value = "truncateTable",
                      exception = {
                          @ThriftException(type=IOError.class, id=1)
                      })
        ListenableFuture<Void> truncateTable(
            @ThriftField(value=1, name="tableName", requiredness=Requiredness.NONE) final byte[] tableName
        );

        @ThriftMethod(value = "renameTable",
                      exception = {
                          @ThriftException(type=IOError.class, id=1)
                      })
        ListenableFuture<Void> renameTable(
            @ThriftField(value=1, name="tableName", requiredness=Requiredness.NONE) final byte[] tableName,
            @ThriftField(value=2, name="newTableName", requiredness=Requiredness.NONE) final byte[] newTableName
        );

        @ThriftMethod(value = "deleteRows",
                      exception = {
                          @ThriftException(type=IOError.class, id=1)
                      })
        ListenableFuture<Void> deleteRows(
            @ThriftField(value=1, name="tableName", requiredness=Requiredness.NONE) final byte[] tableName,
            @ThriftField(value=2, name="rows", requiredness=Requiredness.NONE) final List<byte[]> rows
        );
    }
    @ThriftMethod(value = "enableTable",
                  exception = {
                      @ThriftException(type=IOError.class, id=1)
                  })
    void enableTable(
        @ThriftField(value=1, name="tableName", requiredness=Requiredness.NONE) final byte[] tableName
    ) throws IOError;

    @ThriftMethod(value = "disableTable",
                  exception = {
                      @ThriftException(type=IOError.class, id=1)
                  })
    void disableTable(
        @ThriftField(value=1, name="tableName", requiredness=Requiredness.NONE) final byte[] tableName
    ) throws IOError;

    @ThriftMethod(value = "isTableEnabled",
                  exception = {
                      @ThriftException(type=IOError.class, id=1)
                  })
    boolean isTableEnabled(
        @ThriftField(value=1, name="tableName", requiredness=Requiredness.NONE) final byte[] tableName
    ) throws IOError;

    @ThriftMethod(value = "compact",
                  exception = {
                      @ThriftException(type=IOError.class, id=1)
                  })
    void compact(
        @ThriftField(value=1, name="tableNameOrRegionName", requiredness=Requiredness.NONE) final byte[] tableNameOrRegionName
    ) throws IOError;

    @ThriftMethod(value = "majorCompact",
                  exception = {
                      @ThriftException(type=IOError.class, id=1)
                  })
    void majorCompact(
        @ThriftField(value=1, name="tableNameOrRegionName", requiredness=Requiredness.NONE) final byte[] tableNameOrRegionName
    ) throws IOError;

    @ThriftMethod(value = "getTableNames",
                  exception = {
                      @ThriftException(type=IOError.class, id=1)
                  })
    List<byte[]> getTableNames() throws IOError;

    @ThriftMethod(value = "getColumnDescriptors",
                  exception = {
                      @ThriftException(type=IOError.class, id=1)
                  })
    Map<byte[], ColumnDescriptor> getColumnDescriptors(
        @ThriftField(value=1, name="tableName", requiredness=Requiredness.NONE) final byte[] tableName
    ) throws IOError;

    @ThriftMethod(value = "getTableRegions",
                  exception = {
                      @ThriftException(type=IOError.class, id=1)
                  })
    List<TRegionInfo> getTableRegions(
        @ThriftField(value=1, name="tableName", requiredness=Requiredness.NONE) final byte[] tableName
    ) throws IOError;

    @ThriftMethod(value = "createTable",
                  exception = {
                      @ThriftException(type=IOError.class, id=1),
                      @ThriftException(type=IllegalArgument.class, id=2),
                      @ThriftException(type=AlreadyExists.class, id=3)
                  })
    void createTable(
        @ThriftField(value=1, name="tableName", requiredness=Requiredness.NONE) final byte[] tableName,
        @ThriftField(value=2, name="columnFamilies", requiredness=Requiredness.NONE) final List<ColumnDescriptor> columnFamilies
    ) throws IOError, IllegalArgument, AlreadyExists;

    @ThriftMethod(value = "deleteTable",
                  exception = {
                      @ThriftException(type=IOError.class, id=1)
                  })
    void deleteTable(
        @ThriftField(value=1, name="tableName", requiredness=Requiredness.NONE) final byte[] tableName
    ) throws IOError;

    @ThriftMethod(value = "get",
                  exception = {
                      @ThriftException(type=IOError.class, id=1)
                  })
    List<TCell> get(
        @ThriftField(value=1, name="tableName", requiredness=Requiredness.NONE) final byte[] tableName,
        @ThriftField(value=2, name="row", requiredness=Requiredness.NONE) final byte[] row,
        @ThriftField(value=3, name="column", requiredness=Requiredness.NONE) final byte[] column,
        @ThriftField(value=4, name="attributes", requiredness=Requiredness.NONE) final Map<byte[], byte[]> attributes
    ) throws IOError;

    @ThriftMethod(value = "getVer",
                  exception = {
                      @ThriftException(type=IOError.class, id=1)
                  })
    List<TCell> getVer(
        @ThriftField(value=1, name="tableName", requiredness=Requiredness.NONE) final byte[] tableName,
        @ThriftField(value=2, name="row", requiredness=Requiredness.NONE) final byte[] row,
        @ThriftField(value=3, name="column", requiredness=Requiredness.NONE) final byte[] column,
        @ThriftField(value=4, name="numVersions", requiredness=Requiredness.NONE) final int numVersions,
        @ThriftField(value=5, name="attributes", requiredness=Requiredness.NONE) final Map<byte[], byte[]> attributes
    ) throws IOError;

    @ThriftMethod(value = "getVerTs",
                  exception = {
                      @ThriftException(type=IOError.class, id=1)
                  })
    List<TCell> getVerTs(
        @ThriftField(value=1, name="tableName", requiredness=Requiredness.NONE) final byte[] tableName,
        @ThriftField(value=2, name="row", requiredness=Requiredness.NONE) final byte[] row,
        @ThriftField(value=3, name="column", requiredness=Requiredness.NONE) final byte[] column,
        @ThriftField(value=4, name="timestamp", requiredness=Requiredness.NONE) final long timestamp,
        @ThriftField(value=5, name="numVersions", requiredness=Requiredness.NONE) final int numVersions,
        @ThriftField(value=6, name="attributes", requiredness=Requiredness.NONE) final Map<byte[], byte[]> attributes
    ) throws IOError;

    @ThriftMethod(value = "getRow",
                  exception = {
                      @ThriftException(type=IOError.class, id=1)
                  })
    List<TRowResult> getRow(
        @ThriftField(value=1, name="tableName", requiredness=Requiredness.NONE) final byte[] tableName,
        @ThriftField(value=2, name="row", requiredness=Requiredness.NONE) final byte[] row,
        @ThriftField(value=3, name="attributes", requiredness=Requiredness.NONE) final Map<byte[], byte[]> attributes
    ) throws IOError;

    @ThriftMethod(value = "getRowWithColumns",
                  exception = {
                      @ThriftException(type=IOError.class, id=1)
                  })
    List<TRowResult> getRowWithColumns(
        @ThriftField(value=1, name="tableName", requiredness=Requiredness.NONE) final byte[] tableName,
        @ThriftField(value=2, name="row", requiredness=Requiredness.NONE) final byte[] row,
        @ThriftField(value=3, name="columns", requiredness=Requiredness.NONE) final List<byte[]> columns,
        @ThriftField(value=4, name="attributes", requiredness=Requiredness.NONE) final Map<byte[], byte[]> attributes
    ) throws IOError;

    @ThriftMethod(value = "getRowTs",
                  exception = {
                      @ThriftException(type=IOError.class, id=1)
                  })
    List<TRowResult> getRowTs(
        @ThriftField(value=1, name="tableName", requiredness=Requiredness.NONE) final byte[] tableName,
        @ThriftField(value=2, name="row", requiredness=Requiredness.NONE) final byte[] row,
        @ThriftField(value=3, name="timestamp", requiredness=Requiredness.NONE) final long timestamp,
        @ThriftField(value=4, name="attributes", requiredness=Requiredness.NONE) final Map<byte[], byte[]> attributes
    ) throws IOError;

    @ThriftMethod(value = "getRowWithColumnsTs",
                  exception = {
                      @ThriftException(type=IOError.class, id=1)
                  })
    List<TRowResult> getRowWithColumnsTs(
        @ThriftField(value=1, name="tableName", requiredness=Requiredness.NONE) final byte[] tableName,
        @ThriftField(value=2, name="row", requiredness=Requiredness.NONE) final byte[] row,
        @ThriftField(value=3, name="columns", requiredness=Requiredness.NONE) final List<byte[]> columns,
        @ThriftField(value=4, name="timestamp", requiredness=Requiredness.NONE) final long timestamp,
        @ThriftField(value=5, name="attributes", requiredness=Requiredness.NONE) final Map<byte[], byte[]> attributes
    ) throws IOError;

    @ThriftMethod(value = "getRows",
                  exception = {
                      @ThriftException(type=IOError.class, id=1)
                  })
    List<TRowResult> getRows(
        @ThriftField(value=1, name="tableName", requiredness=Requiredness.NONE) final byte[] tableName,
        @ThriftField(value=2, name="rows", requiredness=Requiredness.NONE) final List<byte[]> rows,
        @ThriftField(value=3, name="attributes", requiredness=Requiredness.NONE) final Map<byte[], byte[]> attributes
    ) throws IOError;

    @ThriftMethod(value = "getRowsWithColumns",
                  exception = {
                      @ThriftException(type=IOError.class, id=1)
                  })
    List<TRowResult> getRowsWithColumns(
        @ThriftField(value=1, name="tableName", requiredness=Requiredness.NONE) final byte[] tableName,
        @ThriftField(value=2, name="rows", requiredness=Requiredness.NONE) final List<byte[]> rows,
        @ThriftField(value=3, name="columns", requiredness=Requiredness.NONE) final List<byte[]> columns,
        @ThriftField(value=4, name="attributes", requiredness=Requiredness.NONE) final Map<byte[], byte[]> attributes
    ) throws IOError;

    @ThriftMethod(value = "getRowsTs",
                  exception = {
                      @ThriftException(type=IOError.class, id=1)
                  })
    List<TRowResult> getRowsTs(
        @ThriftField(value=1, name="tableName", requiredness=Requiredness.NONE) final byte[] tableName,
        @ThriftField(value=2, name="rows", requiredness=Requiredness.NONE) final List<byte[]> rows,
        @ThriftField(value=3, name="timestamp", requiredness=Requiredness.NONE) final long timestamp,
        @ThriftField(value=4, name="attributes", requiredness=Requiredness.NONE) final Map<byte[], byte[]> attributes
    ) throws IOError;

    @ThriftMethod(value = "getRowsWithColumnsTs",
                  exception = {
                      @ThriftException(type=IOError.class, id=1)
                  })
    List<TRowResult> getRowsWithColumnsTs(
        @ThriftField(value=1, name="tableName", requiredness=Requiredness.NONE) final byte[] tableName,
        @ThriftField(value=2, name="rows", requiredness=Requiredness.NONE) final List<byte[]> rows,
        @ThriftField(value=3, name="columns", requiredness=Requiredness.NONE) final List<byte[]> columns,
        @ThriftField(value=4, name="timestamp", requiredness=Requiredness.NONE) final long timestamp,
        @ThriftField(value=5, name="attributes", requiredness=Requiredness.NONE) final Map<byte[], byte[]> attributes
    ) throws IOError;

    @ThriftMethod(value = "mutateRow",
                  exception = {
                      @ThriftException(type=IOError.class, id=1),
                      @ThriftException(type=IllegalArgument.class, id=2)
                  })
    void mutateRow(
        @ThriftField(value=1, name="tableName", requiredness=Requiredness.NONE) final byte[] tableName,
        @ThriftField(value=2, name="row", requiredness=Requiredness.NONE) final byte[] row,
        @ThriftField(value=3, name="mutations", requiredness=Requiredness.NONE) final List<Mutation> mutations,
        @ThriftField(value=4, name="attributes", requiredness=Requiredness.NONE) final Map<byte[], byte[]> attributes
    ) throws IOError, IllegalArgument;

    @ThriftMethod(value = "mutateRowTs",
                  exception = {
                      @ThriftException(type=IOError.class, id=1),
                      @ThriftException(type=IllegalArgument.class, id=2)
                  })
    void mutateRowTs(
        @ThriftField(value=1, name="tableName", requiredness=Requiredness.NONE) final byte[] tableName,
        @ThriftField(value=2, name="row", requiredness=Requiredness.NONE) final byte[] row,
        @ThriftField(value=3, name="mutations", requiredness=Requiredness.NONE) final List<Mutation> mutations,
        @ThriftField(value=4, name="timestamp", requiredness=Requiredness.NONE) final long timestamp,
        @ThriftField(value=5, name="attributes", requiredness=Requiredness.NONE) final Map<byte[], byte[]> attributes
    ) throws IOError, IllegalArgument;

    @ThriftMethod(value = "mutateRows",
                  exception = {
                      @ThriftException(type=IOError.class, id=1),
                      @ThriftException(type=IllegalArgument.class, id=2)
                  })
    void mutateRows(
        @ThriftField(value=1, name="tableName", requiredness=Requiredness.NONE) final byte[] tableName,
        @ThriftField(value=2, name="rowBatches", requiredness=Requiredness.NONE) final List<BatchMutation> rowBatches,
        @ThriftField(value=3, name="attributes", requiredness=Requiredness.NONE) final Map<byte[], byte[]> attributes
    ) throws IOError, IllegalArgument;

    @ThriftMethod(value = "mutateRowsTs",
                  exception = {
                      @ThriftException(type=IOError.class, id=1),
                      @ThriftException(type=IllegalArgument.class, id=2)
                  })
    void mutateRowsTs(
        @ThriftField(value=1, name="tableName", requiredness=Requiredness.NONE) final byte[] tableName,
        @ThriftField(value=2, name="rowBatches", requiredness=Requiredness.NONE) final List<BatchMutation> rowBatches,
        @ThriftField(value=3, name="timestamp", requiredness=Requiredness.NONE) final long timestamp,
        @ThriftField(value=4, name="attributes", requiredness=Requiredness.NONE) final Map<byte[], byte[]> attributes
    ) throws IOError, IllegalArgument;

    @ThriftMethod(value = "atomicIncrement",
                  exception = {
                      @ThriftException(type=IOError.class, id=1),
                      @ThriftException(type=IllegalArgument.class, id=2)
                  })
    long atomicIncrement(
        @ThriftField(value=1, name="tableName", requiredness=Requiredness.NONE) final byte[] tableName,
        @ThriftField(value=2, name="row", requiredness=Requiredness.NONE) final byte[] row,
        @ThriftField(value=3, name="column", requiredness=Requiredness.NONE) final byte[] column,
        @ThriftField(value=4, name="value", requiredness=Requiredness.NONE) final long value
    ) throws IOError, IllegalArgument;

    @ThriftMethod(value = "deleteAll",
                  exception = {
                      @ThriftException(type=IOError.class, id=1)
                  })
    void deleteAll(
        @ThriftField(value=1, name="tableName", requiredness=Requiredness.NONE) final byte[] tableName,
        @ThriftField(value=2, name="row", requiredness=Requiredness.NONE) final byte[] row,
        @ThriftField(value=3, name="column", requiredness=Requiredness.NONE) final byte[] column,
        @ThriftField(value=4, name="attributes", requiredness=Requiredness.NONE) final Map<byte[], byte[]> attributes
    ) throws IOError;

    @ThriftMethod(value = "deleteAllTs",
                  exception = {
                      @ThriftException(type=IOError.class, id=1)
                  })
    void deleteAllTs(
        @ThriftField(value=1, name="tableName", requiredness=Requiredness.NONE) final byte[] tableName,
        @ThriftField(value=2, name="row", requiredness=Requiredness.NONE) final byte[] row,
        @ThriftField(value=3, name="column", requiredness=Requiredness.NONE) final byte[] column,
        @ThriftField(value=4, name="timestamp", requiredness=Requiredness.NONE) final long timestamp,
        @ThriftField(value=5, name="attributes", requiredness=Requiredness.NONE) final Map<byte[], byte[]> attributes
    ) throws IOError;

    @ThriftMethod(value = "deleteAllRow",
                  exception = {
                      @ThriftException(type=IOError.class, id=1)
                  })
    void deleteAllRow(
        @ThriftField(value=1, name="tableName", requiredness=Requiredness.NONE) final byte[] tableName,
        @ThriftField(value=2, name="row", requiredness=Requiredness.NONE) final byte[] row,
        @ThriftField(value=3, name="attributes", requiredness=Requiredness.NONE) final Map<byte[], byte[]> attributes
    ) throws IOError;

    @ThriftMethod(value = "increment",
                  exception = {
                      @ThriftException(type=IOError.class, id=1)
                  })
    void increment(
        @ThriftField(value=1, name="increment", requiredness=Requiredness.NONE) final TIncrement increment
    ) throws IOError;

    @ThriftMethod(value = "incrementRows",
                  exception = {
                      @ThriftException(type=IOError.class, id=1)
                  })
    void incrementRows(
        @ThriftField(value=1, name="increments", requiredness=Requiredness.NONE) final List<TIncrement> increments
    ) throws IOError;

    @ThriftMethod(value = "deleteAllRowTs",
                  exception = {
                      @ThriftException(type=IOError.class, id=1)
                  })
    void deleteAllRowTs(
        @ThriftField(value=1, name="tableName", requiredness=Requiredness.NONE) final byte[] tableName,
        @ThriftField(value=2, name="row", requiredness=Requiredness.NONE) final byte[] row,
        @ThriftField(value=3, name="timestamp", requiredness=Requiredness.NONE) final long timestamp,
        @ThriftField(value=4, name="attributes", requiredness=Requiredness.NONE) final Map<byte[], byte[]> attributes
    ) throws IOError;

    @ThriftMethod(value = "scannerOpenWithScan",
                  exception = {
                      @ThriftException(type=IOError.class, id=1)
                  })
    int scannerOpenWithScan(
        @ThriftField(value=1, name="tableName", requiredness=Requiredness.NONE) final byte[] tableName,
        @ThriftField(value=2, name="scan", requiredness=Requiredness.NONE) final TScan scan,
        @ThriftField(value=3, name="attributes", requiredness=Requiredness.NONE) final Map<byte[], byte[]> attributes
    ) throws IOError;

    @ThriftMethod(value = "scannerOpen",
                  exception = {
                      @ThriftException(type=IOError.class, id=1)
                  })
    int scannerOpen(
        @ThriftField(value=1, name="tableName", requiredness=Requiredness.NONE) final byte[] tableName,
        @ThriftField(value=2, name="startRow", requiredness=Requiredness.NONE) final byte[] startRow,
        @ThriftField(value=3, name="columns", requiredness=Requiredness.NONE) final List<byte[]> columns,
        @ThriftField(value=4, name="attributes", requiredness=Requiredness.NONE) final Map<byte[], byte[]> attributes
    ) throws IOError;

    @ThriftMethod(value = "scannerOpenWithStop",
                  exception = {
                      @ThriftException(type=IOError.class, id=1)
                  })
    int scannerOpenWithStop(
        @ThriftField(value=1, name="tableName", requiredness=Requiredness.NONE) final byte[] tableName,
        @ThriftField(value=2, name="startRow", requiredness=Requiredness.NONE) final byte[] startRow,
        @ThriftField(value=3, name="stopRow", requiredness=Requiredness.NONE) final byte[] stopRow,
        @ThriftField(value=4, name="columns", requiredness=Requiredness.NONE) final List<byte[]> columns,
        @ThriftField(value=5, name="attributes", requiredness=Requiredness.NONE) final Map<byte[], byte[]> attributes
    ) throws IOError;

    @ThriftMethod(value = "scannerOpenWithPrefix",
                  exception = {
                      @ThriftException(type=IOError.class, id=1)
                  })
    int scannerOpenWithPrefix(
        @ThriftField(value=1, name="tableName", requiredness=Requiredness.NONE) final byte[] tableName,
        @ThriftField(value=2, name="startAndPrefix", requiredness=Requiredness.NONE) final byte[] startAndPrefix,
        @ThriftField(value=3, name="columns", requiredness=Requiredness.NONE) final List<byte[]> columns,
        @ThriftField(value=4, name="attributes", requiredness=Requiredness.NONE) final Map<byte[], byte[]> attributes
    ) throws IOError;

    @ThriftMethod(value = "scannerOpenTs",
                  exception = {
                      @ThriftException(type=IOError.class, id=1)
                  })
    int scannerOpenTs(
        @ThriftField(value=1, name="tableName", requiredness=Requiredness.NONE) final byte[] tableName,
        @ThriftField(value=2, name="startRow", requiredness=Requiredness.NONE) final byte[] startRow,
        @ThriftField(value=3, name="columns", requiredness=Requiredness.NONE) final List<byte[]> columns,
        @ThriftField(value=4, name="timestamp", requiredness=Requiredness.NONE) final long timestamp,
        @ThriftField(value=5, name="attributes", requiredness=Requiredness.NONE) final Map<byte[], byte[]> attributes
    ) throws IOError;

    @ThriftMethod(value = "scannerOpenWithStopTs",
                  exception = {
                      @ThriftException(type=IOError.class, id=1)
                  })
    int scannerOpenWithStopTs(
        @ThriftField(value=1, name="tableName", requiredness=Requiredness.NONE) final byte[] tableName,
        @ThriftField(value=2, name="startRow", requiredness=Requiredness.NONE) final byte[] startRow,
        @ThriftField(value=3, name="stopRow", requiredness=Requiredness.NONE) final byte[] stopRow,
        @ThriftField(value=4, name="columns", requiredness=Requiredness.NONE) final List<byte[]> columns,
        @ThriftField(value=5, name="timestamp", requiredness=Requiredness.NONE) final long timestamp,
        @ThriftField(value=6, name="attributes", requiredness=Requiredness.NONE) final Map<byte[], byte[]> attributes
    ) throws IOError;

    @ThriftMethod(value = "scannerGet",
                  exception = {
                      @ThriftException(type=IOError.class, id=1),
                      @ThriftException(type=IllegalArgument.class, id=2)
                  })
    List<TRowResult> scannerGet(
        @ThriftField(value=1, name="id", requiredness=Requiredness.NONE) final int id
    ) throws IOError, IllegalArgument;

    @ThriftMethod(value = "scannerGetList",
                  exception = {
                      @ThriftException(type=IOError.class, id=1),
                      @ThriftException(type=IllegalArgument.class, id=2)
                  })
    List<TRowResult> scannerGetList(
        @ThriftField(value=1, name="id", requiredness=Requiredness.NONE) final int id,
        @ThriftField(value=2, name="nbRows", requiredness=Requiredness.NONE) final int nbRows
    ) throws IOError, IllegalArgument;

    @ThriftMethod(value = "scannerClose",
                  exception = {
                      @ThriftException(type=IOError.class, id=1),
                      @ThriftException(type=IllegalArgument.class, id=2)
                  })
    void scannerClose(
        @ThriftField(value=1, name="id", requiredness=Requiredness.NONE) final int id
    ) throws IOError, IllegalArgument;

    @ThriftMethod(value = "getRowOrBefore",
                  exception = {
                      @ThriftException(type=IOError.class, id=1)
                  })
    List<TCell> getRowOrBefore(
        @ThriftField(value=1, name="tableName", requiredness=Requiredness.NONE) final byte[] tableName,
        @ThriftField(value=2, name="row", requiredness=Requiredness.NONE) final byte[] row,
        @ThriftField(value=3, name="family", requiredness=Requiredness.NONE) final byte[] family
    ) throws IOError;

    @ThriftMethod(value = "getRegionInfo",
                  exception = {
                      @ThriftException(type=IOError.class, id=1)
                  })
    TRegionInfo getRegionInfo(
        @ThriftField(value=1, name="row", requiredness=Requiredness.NONE) final byte[] row
    ) throws IOError;

    @ThriftMethod(value = "append",
                  exception = {
                      @ThriftException(type=IOError.class, id=1)
                  })
    List<TCell> append(
        @ThriftField(value=1, name="append", requiredness=Requiredness.NONE) final TAppend append
    ) throws IOError;

    @ThriftMethod(value = "checkAndPut",
                  exception = {
                      @ThriftException(type=IOError.class, id=1),
                      @ThriftException(type=IllegalArgument.class, id=2)
                  })
    boolean checkAndPut(
        @ThriftField(value=1, name="tableName", requiredness=Requiredness.NONE) final byte[] tableName,
        @ThriftField(value=2, name="row", requiredness=Requiredness.NONE) final byte[] row,
        @ThriftField(value=3, name="column", requiredness=Requiredness.NONE) final byte[] column,
        @ThriftField(value=5, name="value", requiredness=Requiredness.NONE) final byte[] value,
        @ThriftField(value=6, name="mput", requiredness=Requiredness.NONE) final Mutation mput,
        @ThriftField(value=7, name="attributes", requiredness=Requiredness.NONE) final Map<byte[], byte[]> attributes
    ) throws IOError, IllegalArgument;

    @ThriftMethod(value = "truncateTable",
                  exception = {
                      @ThriftException(type=IOError.class, id=1)
                  })
    void truncateTable(
        @ThriftField(value=1, name="tableName", requiredness=Requiredness.NONE) final byte[] tableName
    ) throws IOError;

    @ThriftMethod(value = "renameTable",
                  exception = {
                      @ThriftException(type=IOError.class, id=1)
                  })
    void renameTable(
        @ThriftField(value=1, name="tableName", requiredness=Requiredness.NONE) final byte[] tableName,
        @ThriftField(value=2, name="newTableName", requiredness=Requiredness.NONE) final byte[] newTableName
    ) throws IOError;

    @ThriftMethod(value = "deleteRows",
                  exception = {
                      @ThriftException(type=IOError.class, id=1)
                  })
    void deleteRows(
        @ThriftField(value=1, name="tableName", requiredness=Requiredness.NONE) final byte[] tableName,
        @ThriftField(value=2, name="rows", requiredness=Requiredness.NONE) final List<byte[]> rows
    ) throws IOError;
}