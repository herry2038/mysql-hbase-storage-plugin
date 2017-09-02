package org.apache.hadoop.hbase.swift2.generated;

import com.facebook.swift.codec.*;

public enum TDeleteType
{
    DELETE_COLUMN(0), DELETE_COLUMNS(1);

    private final int value;

    TDeleteType(int value)
    {
        this.value = value;
    }

    @ThriftEnumValue
    public int getValue()
    {
        return value;
    }
}
