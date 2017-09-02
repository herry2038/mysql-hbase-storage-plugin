package org.apache.hadoop.hbase.swift2.generated;

import com.facebook.swift.codec.*;

public enum TDurability
{
    SKIP_WAL(1), ASYNC_WAL(2), SYNC_WAL(3), FSYNC_WAL(4);

    private final int value;

    TDurability(int value)
    {
        this.value = value;
    }

    @ThriftEnumValue
    public int getValue()
    {
        return value;
    }
}
