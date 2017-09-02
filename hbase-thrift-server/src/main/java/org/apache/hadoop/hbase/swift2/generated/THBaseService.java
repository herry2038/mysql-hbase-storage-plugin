package org.apache.hadoop.hbase.swift2.generated;

import com.facebook.swift.codec.*;
import com.facebook.swift.codec.ThriftField.Requiredness;
import com.facebook.swift.service.*;
import com.google.common.util.concurrent.ListenableFuture;
import java.io.*;
import java.util.*;

@ThriftService("THBaseService")
public interface THBaseService
{
    @ThriftService("THBaseService")
    public interface Async
    {
        @ThriftMethod(value = "exists",
                      exception = {
                          @ThriftException(type=TIOError.class, id=1)
                      })
        ListenableFuture<Boolean> exists(
            @ThriftField(value=1, name="table", requiredness=Requiredness.REQUIRED) final byte[] table,
            @ThriftField(value=2, name="tget", requiredness=Requiredness.REQUIRED) final TGet tget
        );

        @ThriftMethod(value = "get",
                      exception = {
                          @ThriftException(type=TIOError.class, id=1)
                      })
        ListenableFuture<TResult> get(
            @ThriftField(value=1, name="table", requiredness=Requiredness.REQUIRED) final byte[] table,
            @ThriftField(value=2, name="tget", requiredness=Requiredness.REQUIRED) final TGet tget
        );

        @ThriftMethod(value = "getMultiple",
                      exception = {
                          @ThriftException(type=TIOError.class, id=1)
                      })
        ListenableFuture<List<TResult>> getMultiple(
            @ThriftField(value=1, name="table", requiredness=Requiredness.REQUIRED) final byte[] table,
            @ThriftField(value=2, name="tgets", requiredness=Requiredness.REQUIRED) final List<TGet> tgets
        );

        @ThriftMethod(value = "put",
                      exception = {
                          @ThriftException(type=TIOError.class, id=1)
                      })
        ListenableFuture<Void> put(
            @ThriftField(value=1, name="table", requiredness=Requiredness.REQUIRED) final byte[] table,
            @ThriftField(value=2, name="tput", requiredness=Requiredness.REQUIRED) final TPut tput
        );

        @ThriftMethod(value = "checkAndPut",
                      exception = {
                          @ThriftException(type=TIOError.class, id=1)
                      })
        ListenableFuture<Boolean> checkAndPut(
            @ThriftField(value=1, name="table", requiredness=Requiredness.REQUIRED) final byte[] table,
            @ThriftField(value=2, name="row", requiredness=Requiredness.REQUIRED) final byte[] row,
            @ThriftField(value=3, name="family", requiredness=Requiredness.REQUIRED) final byte[] family,
            @ThriftField(value=4, name="qualifier", requiredness=Requiredness.REQUIRED) final byte[] qualifier,
            @ThriftField(value=5, name="value", requiredness=Requiredness.NONE) final byte[] value,
            @ThriftField(value=6, name="tput", requiredness=Requiredness.REQUIRED) final TPut tput
        );

        @ThriftMethod(value = "putMultiple",
                      exception = {
                          @ThriftException(type=TIOError.class, id=1)
                      })
        ListenableFuture<Void> putMultiple(
            @ThriftField(value=1, name="table", requiredness=Requiredness.REQUIRED) final byte[] table,
            @ThriftField(value=2, name="tputs", requiredness=Requiredness.REQUIRED) final List<TPut> tputs
        );

        @ThriftMethod(value = "deleteSingle",
                      exception = {
                          @ThriftException(type=TIOError.class, id=1)
                      })
        ListenableFuture<Void> deleteSingle(
            @ThriftField(value=1, name="table", requiredness=Requiredness.REQUIRED) final byte[] table,
            @ThriftField(value=2, name="tdelete", requiredness=Requiredness.REQUIRED) final TDelete tdelete
        );

        @ThriftMethod(value = "deleteMultiple",
                      exception = {
                          @ThriftException(type=TIOError.class, id=1)
                      })
        ListenableFuture<List<TDelete>> deleteMultiple(
            @ThriftField(value=1, name="table", requiredness=Requiredness.REQUIRED) final byte[] table,
            @ThriftField(value=2, name="tdeletes", requiredness=Requiredness.REQUIRED) final List<TDelete> tdeletes
        );

        @ThriftMethod(value = "checkAndDelete",
                      exception = {
                          @ThriftException(type=TIOError.class, id=1)
                      })
        ListenableFuture<Boolean> checkAndDelete(
            @ThriftField(value=1, name="table", requiredness=Requiredness.REQUIRED) final byte[] table,
            @ThriftField(value=2, name="row", requiredness=Requiredness.REQUIRED) final byte[] row,
            @ThriftField(value=3, name="family", requiredness=Requiredness.REQUIRED) final byte[] family,
            @ThriftField(value=4, name="qualifier", requiredness=Requiredness.REQUIRED) final byte[] qualifier,
            @ThriftField(value=5, name="value", requiredness=Requiredness.NONE) final byte[] value,
            @ThriftField(value=6, name="tdelete", requiredness=Requiredness.REQUIRED) final TDelete tdelete
        );

        @ThriftMethod(value = "increment",
                      exception = {
                          @ThriftException(type=TIOError.class, id=1)
                      })
        ListenableFuture<TResult> increment(
            @ThriftField(value=1, name="table", requiredness=Requiredness.REQUIRED) final byte[] table,
            @ThriftField(value=2, name="tincrement", requiredness=Requiredness.REQUIRED) final TIncrement tincrement
        );

        @ThriftMethod(value = "append",
                      exception = {
                          @ThriftException(type=TIOError.class, id=1)
                      })
        ListenableFuture<TResult> append(
            @ThriftField(value=1, name="table", requiredness=Requiredness.REQUIRED) final byte[] table,
            @ThriftField(value=2, name="tappend", requiredness=Requiredness.REQUIRED) final TAppend tappend
        );

        @ThriftMethod(value = "openScanner",
                      exception = {
                          @ThriftException(type=TIOError.class, id=1)
                      })
        ListenableFuture<Integer> openScanner(
            @ThriftField(value=1, name="table", requiredness=Requiredness.REQUIRED) final byte[] table,
            @ThriftField(value=2, name="tscan", requiredness=Requiredness.REQUIRED) final TScan tscan
        );

        @ThriftMethod(value = "getScannerRows",
                      exception = {
                          @ThriftException(type=TIOError.class, id=1),
                          @ThriftException(type=TIllegalArgument.class, id=2)
                      })
        ListenableFuture<List<TResult>> getScannerRows(
            @ThriftField(value=1, name="scannerId", requiredness=Requiredness.REQUIRED) final int scannerId,
            @ThriftField(value=2, name="numRows", requiredness=Requiredness.NONE) final int numRows
        );

        @ThriftMethod(value = "closeScanner",
                      exception = {
                          @ThriftException(type=TIOError.class, id=1),
                          @ThriftException(type=TIllegalArgument.class, id=2)
                      })
        ListenableFuture<Void> closeScanner(
            @ThriftField(value=1, name="scannerId", requiredness=Requiredness.REQUIRED) final int scannerId
        );

        @ThriftMethod(value = "mutateRow",
                      exception = {
                          @ThriftException(type=TIOError.class, id=1)
                      })
        ListenableFuture<Void> mutateRow(
            @ThriftField(value=1, name="table", requiredness=Requiredness.REQUIRED) final byte[] table,
            @ThriftField(value=2, name="trowMutations", requiredness=Requiredness.REQUIRED) final TRowMutations trowMutations
        );

        @ThriftMethod(value = "getScannerResults",
                      exception = {
                          @ThriftException(type=TIOError.class, id=1)
                      })
        ListenableFuture<List<TResult>> getScannerResults(
            @ThriftField(value=1, name="table", requiredness=Requiredness.REQUIRED) final byte[] table,
            @ThriftField(value=2, name="tscan", requiredness=Requiredness.REQUIRED) final TScan tscan,
            @ThriftField(value=3, name="numRows", requiredness=Requiredness.NONE) final int numRows
        );

        @ThriftMethod(value = "getRegionLocation",
                      exception = {
                          @ThriftException(type=TIOError.class, id=1)
                      })
        ListenableFuture<THRegionLocation> getRegionLocation(
            @ThriftField(value=1, name="table", requiredness=Requiredness.REQUIRED) final byte[] table,
            @ThriftField(value=2, name="row", requiredness=Requiredness.REQUIRED) final byte[] row,
            @ThriftField(value=3, name="reload", requiredness=Requiredness.NONE) final boolean reload
        );

        @ThriftMethod(value = "getAllRegionLocations",
                      exception = {
                          @ThriftException(type=TIOError.class, id=1)
                      })
        ListenableFuture<List<THRegionLocation>> getAllRegionLocations(
            @ThriftField(value=1, name="table", requiredness=Requiredness.REQUIRED) final byte[] table
        );
    }
    @ThriftMethod(value = "exists",
                  exception = {
                      @ThriftException(type=TIOError.class, id=1)
                  })
    boolean exists(
        @ThriftField(value=1, name="table", requiredness=Requiredness.REQUIRED) final byte[] table,
        @ThriftField(value=2, name="tget", requiredness=Requiredness.REQUIRED) final TGet tget
    ) throws TIOError;

    @ThriftMethod(value = "get",
                  exception = {
                      @ThriftException(type=TIOError.class, id=1)
                  })
    TResult get(
        @ThriftField(value=1, name="table", requiredness=Requiredness.REQUIRED) final byte[] table,
        @ThriftField(value=2, name="tget", requiredness=Requiredness.REQUIRED) final TGet tget
    ) throws TIOError;

    @ThriftMethod(value = "getMultiple",
                  exception = {
                      @ThriftException(type=TIOError.class, id=1)
                  })
    List<TResult> getMultiple(
        @ThriftField(value=1, name="table", requiredness=Requiredness.REQUIRED) final byte[] table,
        @ThriftField(value=2, name="tgets", requiredness=Requiredness.REQUIRED) final List<TGet> tgets
    ) throws TIOError;

    @ThriftMethod(value = "put",
                  exception = {
                      @ThriftException(type=TIOError.class, id=1)
                  })
    void put(
        @ThriftField(value=1, name="table", requiredness=Requiredness.REQUIRED) final byte[] table,
        @ThriftField(value=2, name="tput", requiredness=Requiredness.REQUIRED) final TPut tput
    ) throws TIOError;

    @ThriftMethod(value = "checkAndPut",
                  exception = {
                      @ThriftException(type=TIOError.class, id=1)
                  })
    boolean checkAndPut(
        @ThriftField(value=1, name="table", requiredness=Requiredness.REQUIRED) final byte[] table,
        @ThriftField(value=2, name="row", requiredness=Requiredness.REQUIRED) final byte[] row,
        @ThriftField(value=3, name="family", requiredness=Requiredness.REQUIRED) final byte[] family,
        @ThriftField(value=4, name="qualifier", requiredness=Requiredness.REQUIRED) final byte[] qualifier,
        @ThriftField(value=5, name="value", requiredness=Requiredness.NONE) final byte[] value,
        @ThriftField(value=6, name="tput", requiredness=Requiredness.REQUIRED) final TPut tput
    ) throws TIOError;

    @ThriftMethod(value = "putMultiple",
                  exception = {
                      @ThriftException(type=TIOError.class, id=1)
                  })
    void putMultiple(
        @ThriftField(value=1, name="table", requiredness=Requiredness.REQUIRED) final byte[] table,
        @ThriftField(value=2, name="tputs", requiredness=Requiredness.REQUIRED) final List<TPut> tputs
    ) throws TIOError;

    @ThriftMethod(value = "deleteSingle",
                  exception = {
                      @ThriftException(type=TIOError.class, id=1)
                  })
    void deleteSingle(
        @ThriftField(value=1, name="table", requiredness=Requiredness.REQUIRED) final byte[] table,
        @ThriftField(value=2, name="tdelete", requiredness=Requiredness.REQUIRED) final TDelete tdelete
    ) throws TIOError;

    @ThriftMethod(value = "deleteMultiple",
                  exception = {
                      @ThriftException(type=TIOError.class, id=1)
                  })
    List<TDelete> deleteMultiple(
        @ThriftField(value=1, name="table", requiredness=Requiredness.REQUIRED) final byte[] table,
        @ThriftField(value=2, name="tdeletes", requiredness=Requiredness.REQUIRED) final List<TDelete> tdeletes
    ) throws TIOError;

    @ThriftMethod(value = "checkAndDelete",
                  exception = {
                      @ThriftException(type=TIOError.class, id=1)
                  })
    boolean checkAndDelete(
        @ThriftField(value=1, name="table", requiredness=Requiredness.REQUIRED) final byte[] table,
        @ThriftField(value=2, name="row", requiredness=Requiredness.REQUIRED) final byte[] row,
        @ThriftField(value=3, name="family", requiredness=Requiredness.REQUIRED) final byte[] family,
        @ThriftField(value=4, name="qualifier", requiredness=Requiredness.REQUIRED) final byte[] qualifier,
        @ThriftField(value=5, name="value", requiredness=Requiredness.NONE) final byte[] value,
        @ThriftField(value=6, name="tdelete", requiredness=Requiredness.REQUIRED) final TDelete tdelete
    ) throws TIOError;

    @ThriftMethod(value = "increment",
                  exception = {
                      @ThriftException(type=TIOError.class, id=1)
                  })
    TResult increment(
        @ThriftField(value=1, name="table", requiredness=Requiredness.REQUIRED) final byte[] table,
        @ThriftField(value=2, name="tincrement", requiredness=Requiredness.REQUIRED) final TIncrement tincrement
    ) throws TIOError;

    @ThriftMethod(value = "append",
                  exception = {
                      @ThriftException(type=TIOError.class, id=1)
                  })
    TResult append(
        @ThriftField(value=1, name="table", requiredness=Requiredness.REQUIRED) final byte[] table,
        @ThriftField(value=2, name="tappend", requiredness=Requiredness.REQUIRED) final TAppend tappend
    ) throws TIOError;

    @ThriftMethod(value = "openScanner",
                  exception = {
                      @ThriftException(type=TIOError.class, id=1)
                  })
    int openScanner(
        @ThriftField(value=1, name="table", requiredness=Requiredness.REQUIRED) final byte[] table,
        @ThriftField(value=2, name="tscan", requiredness=Requiredness.REQUIRED) final TScan tscan
    ) throws TIOError;

    @ThriftMethod(value = "getScannerRows",
                  exception = {
                      @ThriftException(type=TIOError.class, id=1),
                      @ThriftException(type=TIllegalArgument.class, id=2)
                  })
    List<TResult> getScannerRows(
        @ThriftField(value=1, name="scannerId", requiredness=Requiredness.REQUIRED) final int scannerId,
        @ThriftField(value=2, name="numRows", requiredness=Requiredness.NONE) final int numRows
    ) throws TIOError, TIllegalArgument;

    @ThriftMethod(value = "closeScanner",
                  exception = {
                      @ThriftException(type=TIOError.class, id=1),
                      @ThriftException(type=TIllegalArgument.class, id=2)
                  })
    void closeScanner(
        @ThriftField(value=1, name="scannerId", requiredness=Requiredness.REQUIRED) final int scannerId
    ) throws TIOError, TIllegalArgument;

    @ThriftMethod(value = "mutateRow",
                  exception = {
                      @ThriftException(type=TIOError.class, id=1)
                  })
    void mutateRow(
        @ThriftField(value=1, name="table", requiredness=Requiredness.REQUIRED) final byte[] table,
        @ThriftField(value=2, name="trowMutations", requiredness=Requiredness.REQUIRED) final TRowMutations trowMutations
    ) throws TIOError;

    @ThriftMethod(value = "getScannerResults",
                  exception = {
                      @ThriftException(type=TIOError.class, id=1)
                  })
    List<TResult> getScannerResults(
        @ThriftField(value=1, name="table", requiredness=Requiredness.REQUIRED) final byte[] table,
        @ThriftField(value=2, name="tscan", requiredness=Requiredness.REQUIRED) final TScan tscan,
        @ThriftField(value=3, name="numRows", requiredness=Requiredness.NONE) final int numRows
    ) throws TIOError;

    @ThriftMethod(value = "getRegionLocation",
                  exception = {
                      @ThriftException(type=TIOError.class, id=1)
                  })
    THRegionLocation getRegionLocation(
        @ThriftField(value=1, name="table", requiredness=Requiredness.REQUIRED) final byte[] table,
        @ThriftField(value=2, name="row", requiredness=Requiredness.REQUIRED) final byte[] row,
        @ThriftField(value=3, name="reload", requiredness=Requiredness.NONE) final boolean reload
    ) throws TIOError;

    @ThriftMethod(value = "getAllRegionLocations",
                  exception = {
                      @ThriftException(type=TIOError.class, id=1)
                  })
    List<THRegionLocation> getAllRegionLocations(
        @ThriftField(value=1, name="table", requiredness=Requiredness.REQUIRED) final byte[] table
    ) throws TIOError;
}