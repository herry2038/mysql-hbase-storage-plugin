package org.apache.hadoop.hbase.swift.generated;

import com.facebook.swift.codec.*;
import com.facebook.swift.codec.ThriftField.Requiredness;
import com.facebook.swift.codec.ThriftField.Recursiveness;
import java.util.*;

import static com.google.common.base.Objects.toStringHelper;

@ThriftStruct("TColumn")
public final class TColumn
{
    @ThriftConstructor
    public TColumn(
        @ThriftField(value=1, name="columnName", requiredness=Requiredness.NONE) final byte[] columnName,
        @ThriftField(value=2, name="cell", requiredness=Requiredness.NONE) final TCell cell
    ) {
        this.columnName = columnName;
        this.cell = cell;
    }

    public static class Builder {
        private byte[] columnName;

        public Builder setColumnName(byte[] columnName) {
            this.columnName = columnName;
            return this;
        }
        private TCell cell;

        public Builder setCell(TCell cell) {
            this.cell = cell;
            return this;
        }

        public Builder() { }
        public Builder(TColumn other) {
            this.columnName = other.columnName;
            this.cell = other.cell;
        }

        public TColumn build() {
            return new TColumn (
                this.columnName,
                this.cell
            );
        }
    }

    private final byte[] columnName;

    @ThriftField(value=1, name="columnName", requiredness=Requiredness.NONE)
    public byte[] getColumnName() { return columnName; }

    private final TCell cell;

    @ThriftField(value=2, name="cell", requiredness=Requiredness.NONE)
    public TCell getCell() { return cell; }

    @Override
    public String toString()
    {
        return toStringHelper(this)
            .add("columnName", columnName)
            .add("cell", cell)
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

        TColumn other = (TColumn)o;

        return
            Arrays.equals(columnName, other.columnName) &&
            Objects.equals(cell, other.cell);
    }

    @Override
    public int hashCode() {
        return Arrays.deepHashCode(new Object[] {
            columnName,
            cell
        });
    }
}
