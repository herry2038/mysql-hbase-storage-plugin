package org.apache.hadoop.hbase.swift.generated;

import com.facebook.swift.codec.*;
import com.facebook.swift.codec.ThriftField.Requiredness;
import com.facebook.swift.codec.ThriftField.Recursiveness;
import java.util.*;

import static com.google.common.base.Objects.toStringHelper;

@ThriftStruct("TAppend")
public final class TAppend
{
    @ThriftConstructor
    public TAppend(
        @ThriftField(value=1, name="table", requiredness=Requiredness.NONE) final byte[] table,
        @ThriftField(value=2, name="row", requiredness=Requiredness.NONE) final byte[] row,
        @ThriftField(value=3, name="columns", requiredness=Requiredness.NONE) final List<byte[]> columns,
        @ThriftField(value=4, name="values", requiredness=Requiredness.NONE) final List<byte[]> values
    ) {
        this.table = table;
        this.row = row;
        this.columns = columns;
        this.values = values;
    }

    public static class Builder {
        private byte[] table;

        public Builder setTable(byte[] table) {
            this.table = table;
            return this;
        }
        private byte[] row;

        public Builder setRow(byte[] row) {
            this.row = row;
            return this;
        }
        private List<byte[]> columns;

        public Builder setColumns(List<byte[]> columns) {
            this.columns = columns;
            return this;
        }
        private List<byte[]> values;

        public Builder setValues(List<byte[]> values) {
            this.values = values;
            return this;
        }

        public Builder() { }
        public Builder(TAppend other) {
            this.table = other.table;
            this.row = other.row;
            this.columns = other.columns;
            this.values = other.values;
        }

        public TAppend build() {
            return new TAppend (
                this.table,
                this.row,
                this.columns,
                this.values
            );
        }
    }

    private final byte[] table;

    @ThriftField(value=1, name="table", requiredness=Requiredness.NONE)
    public byte[] getTable() { return table; }

    private final byte[] row;

    @ThriftField(value=2, name="row", requiredness=Requiredness.NONE)
    public byte[] getRow() { return row; }

    private final List<byte[]> columns;

    @ThriftField(value=3, name="columns", requiredness=Requiredness.NONE)
    public List<byte[]> getColumns() { return columns; }

    private final List<byte[]> values;

    @ThriftField(value=4, name="values", requiredness=Requiredness.NONE)
    public List<byte[]> getValues() { return values; }

    @Override
    public String toString()
    {
        return toStringHelper(this)
            .add("table", table)
            .add("row", row)
            .add("columns", columns)
            .add("values", values)
            .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        TAppend other = (TAppend)o;

        return
            Arrays.equals(table, other.table) &&
            Arrays.equals(row, other.row) &&
            Objects.equals(columns, other.columns) &&
            Objects.equals(values, other.values);
    }

    @Override
    public int hashCode() {
        return Arrays.deepHashCode(new Object[] {
            table,
            row,
            columns,
            values
        });
    }
}
