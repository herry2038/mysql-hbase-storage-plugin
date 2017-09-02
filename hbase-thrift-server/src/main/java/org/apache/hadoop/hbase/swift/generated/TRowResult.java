package org.apache.hadoop.hbase.swift.generated;

import com.facebook.swift.codec.*;
import com.facebook.swift.codec.ThriftField.Requiredness;
import com.facebook.swift.codec.ThriftField.Recursiveness;
import java.util.*;

import static com.google.common.base.Objects.toStringHelper;

@ThriftStruct("TRowResult")
public final class TRowResult
{
    @ThriftConstructor
    public TRowResult(
        @ThriftField(value=1, name="row", requiredness=Requiredness.NONE) final byte[] row,
        @ThriftField(value=2, name="columns", requiredness=Requiredness.OPTIONAL) final Map<byte[], TCell> columns,
        @ThriftField(value=3, name="sortedColumns", requiredness=Requiredness.OPTIONAL) final List<TColumn> sortedColumns
    ) {
        this.row = row;
        this.columns = columns;
        this.sortedColumns = sortedColumns;
    }

    public static class Builder {
        private byte[] row;

        public Builder setRow(byte[] row) {
            this.row = row;
            return this;
        }
        private Map<byte[], TCell> columns;

        public Builder setColumns(Map<byte[], TCell> columns) {
            this.columns = columns;
            return this;
        }
        private List<TColumn> sortedColumns;

        public Builder setSortedColumns(List<TColumn> sortedColumns) {
            this.sortedColumns = sortedColumns;
            return this;
        }

        public Builder() { }
        public Builder(TRowResult other) {
            this.row = other.row;
            this.columns = other.columns;
            this.sortedColumns = other.sortedColumns;
        }

        public TRowResult build() {
            return new TRowResult (
                this.row,
                this.columns,
                this.sortedColumns
            );
        }
    }

    private final byte[] row;

    @ThriftField(value=1, name="row", requiredness=Requiredness.NONE)
    public byte[] getRow() { return row; }

    private final Map<byte[], TCell> columns;

    @ThriftField(value=2, name="columns", requiredness=Requiredness.OPTIONAL)
    public Map<byte[], TCell> getColumns() { return columns; }

    private final List<TColumn> sortedColumns;

    @ThriftField(value=3, name="sortedColumns", requiredness=Requiredness.OPTIONAL)
    public List<TColumn> getSortedColumns() { return sortedColumns; }

    @Override
    public String toString()
    {
        return toStringHelper(this)
            .add("row", row)
            .add("columns", columns)
            .add("sortedColumns", sortedColumns)
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

        TRowResult other = (TRowResult)o;

        return
            Arrays.equals(row, other.row) &&
            Objects.equals(columns, other.columns) &&
            Objects.equals(sortedColumns, other.sortedColumns);
    }

    @Override
    public int hashCode() {
        return Arrays.deepHashCode(new Object[] {
            row,
            columns,
            sortedColumns
        });
    }
}
