package org.apache.hadoop.hbase.swift2.generated;

import com.facebook.swift.codec.*;
import com.facebook.swift.codec.ThriftField.Requiredness;
import com.facebook.swift.codec.ThriftField.Recursiveness;
import java.util.*;

import static com.google.common.base.Objects.toStringHelper;

@ThriftStruct("TResult")
public final class TResult
{
    @ThriftConstructor
    public TResult(
        @ThriftField(value=1, name="row", requiredness=Requiredness.OPTIONAL) final byte[] row,
        @ThriftField(value=2, name="columnValues", requiredness=Requiredness.REQUIRED) final List<TColumnValue> columnValues
    ) {
        this.row = row;
        this.columnValues = columnValues;
    }

    public static class Builder {
        private byte[] row;

        public Builder setRow(byte[] row) {
            this.row = row;
            return this;
        }
        private List<TColumnValue> columnValues;

        public Builder setColumnValues(List<TColumnValue> columnValues) {
            this.columnValues = columnValues;
            return this;
        }

        public Builder() { }
        public Builder(TResult other) {
            this.row = other.row;
            this.columnValues = other.columnValues;
        }

        public TResult build() {
            return new TResult (
                this.row,
                this.columnValues
            );
        }
    }

    private final byte[] row;

    @ThriftField(value=1, name="row", requiredness=Requiredness.OPTIONAL)
    public byte[] getRow() { return row; }

    private final List<TColumnValue> columnValues;

    @ThriftField(value=2, name="columnValues", requiredness=Requiredness.REQUIRED)
    public List<TColumnValue> getColumnValues() { return columnValues; }

    @Override
    public String toString()
    {
        return toStringHelper(this)
            .add("row", row)
            .add("columnValues", columnValues)
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

        TResult other = (TResult)o;

        return
            Arrays.equals(row, other.row) &&
            Objects.equals(columnValues, other.columnValues);
    }

    @Override
    public int hashCode() {
        return Arrays.deepHashCode(new Object[] {
            row,
            columnValues
        });
    }
}
