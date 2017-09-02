package org.apache.hadoop.hbase.swift.generated;

import com.facebook.swift.codec.*;
import com.facebook.swift.codec.ThriftField.Requiredness;
import com.facebook.swift.codec.ThriftField.Recursiveness;
import java.util.*;

import static com.google.common.base.Objects.toStringHelper;

@ThriftStruct("TIncrement")
public final class TIncrement
{
    @ThriftConstructor
    public TIncrement(
        @ThriftField(value=1, name="table", requiredness=Requiredness.NONE) final byte[] table,
        @ThriftField(value=2, name="row", requiredness=Requiredness.NONE) final byte[] row,
        @ThriftField(value=3, name="column", requiredness=Requiredness.NONE) final byte[] column,
        @ThriftField(value=4, name="ammount", requiredness=Requiredness.NONE) final long ammount
    ) {
        this.table = table;
        this.row = row;
        this.column = column;
        this.ammount = ammount;
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
        private byte[] column;

        public Builder setColumn(byte[] column) {
            this.column = column;
            return this;
        }
        private long ammount;

        public Builder setAmmount(long ammount) {
            this.ammount = ammount;
            return this;
        }

        public Builder() { }
        public Builder(TIncrement other) {
            this.table = other.table;
            this.row = other.row;
            this.column = other.column;
            this.ammount = other.ammount;
        }

        public TIncrement build() {
            return new TIncrement (
                this.table,
                this.row,
                this.column,
                this.ammount
            );
        }
    }

    private final byte[] table;

    @ThriftField(value=1, name="table", requiredness=Requiredness.NONE)
    public byte[] getTable() { return table; }

    private final byte[] row;

    @ThriftField(value=2, name="row", requiredness=Requiredness.NONE)
    public byte[] getRow() { return row; }

    private final byte[] column;

    @ThriftField(value=3, name="column", requiredness=Requiredness.NONE)
    public byte[] getColumn() { return column; }

    private final long ammount;

    @ThriftField(value=4, name="ammount", requiredness=Requiredness.NONE)
    public long getAmmount() { return ammount; }

    @Override
    public String toString()
    {
        return toStringHelper(this)
            .add("table", table)
            .add("row", row)
            .add("column", column)
            .add("ammount", ammount)
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

        TIncrement other = (TIncrement)o;

        return
            Arrays.equals(table, other.table) &&
            Arrays.equals(row, other.row) &&
            Arrays.equals(column, other.column) &&
            Objects.equals(ammount, other.ammount);
    }

    @Override
    public int hashCode() {
        return Arrays.deepHashCode(new Object[] {
            table,
            row,
            column,
            ammount
        });
    }
}
