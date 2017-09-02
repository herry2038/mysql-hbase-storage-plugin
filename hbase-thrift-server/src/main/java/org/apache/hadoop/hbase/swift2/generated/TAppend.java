package org.apache.hadoop.hbase.swift2.generated;

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
        @ThriftField(value=1, name="row", requiredness=Requiredness.REQUIRED) final byte[] row,
        @ThriftField(value=2, name="columns", requiredness=Requiredness.REQUIRED) final List<TColumnValue> columns,
        @ThriftField(value=3, name="attributes", requiredness=Requiredness.OPTIONAL) final Map<byte[], byte[]> attributes,
        @ThriftField(value=4, name="durability", requiredness=Requiredness.OPTIONAL) final TDurability durability,
        @ThriftField(value=5, name="cellVisibility", requiredness=Requiredness.OPTIONAL) final TCellVisibility cellVisibility
    ) {
        this.row = row;
        this.columns = columns;
        this.attributes = attributes;
        this.durability = durability;
        this.cellVisibility = cellVisibility;
    }

    public static class Builder {
        private byte[] row;

        public Builder setRow(byte[] row) {
            this.row = row;
            return this;
        }
        private List<TColumnValue> columns;

        public Builder setColumns(List<TColumnValue> columns) {
            this.columns = columns;
            return this;
        }
        private Map<byte[], byte[]> attributes;

        public Builder setAttributes(Map<byte[], byte[]> attributes) {
            this.attributes = attributes;
            return this;
        }
        private TDurability durability;

        public Builder setDurability(TDurability durability) {
            this.durability = durability;
            return this;
        }
        private TCellVisibility cellVisibility;

        public Builder setCellVisibility(TCellVisibility cellVisibility) {
            this.cellVisibility = cellVisibility;
            return this;
        }

        public Builder() { }
        public Builder(TAppend other) {
            this.row = other.row;
            this.columns = other.columns;
            this.attributes = other.attributes;
            this.durability = other.durability;
            this.cellVisibility = other.cellVisibility;
        }

        public TAppend build() {
            return new TAppend (
                this.row,
                this.columns,
                this.attributes,
                this.durability,
                this.cellVisibility
            );
        }
    }

    private final byte[] row;

    @ThriftField(value=1, name="row", requiredness=Requiredness.REQUIRED)
    public byte[] getRow() { return row; }

    private final List<TColumnValue> columns;

    @ThriftField(value=2, name="columns", requiredness=Requiredness.REQUIRED)
    public List<TColumnValue> getColumns() { return columns; }

    private final Map<byte[], byte[]> attributes;

    @ThriftField(value=3, name="attributes", requiredness=Requiredness.OPTIONAL)
    public Map<byte[], byte[]> getAttributes() { return attributes; }

    private final TDurability durability;

    @ThriftField(value=4, name="durability", requiredness=Requiredness.OPTIONAL)
    public TDurability getDurability() { return durability; }

    private final TCellVisibility cellVisibility;

    @ThriftField(value=5, name="cellVisibility", requiredness=Requiredness.OPTIONAL)
    public TCellVisibility getCellVisibility() { return cellVisibility; }

    @Override
    public String toString()
    {
        return toStringHelper(this)
            .add("row", row)
            .add("columns", columns)
            .add("attributes", attributes)
            .add("durability", durability)
            .add("cellVisibility", cellVisibility)
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
            Arrays.equals(row, other.row) &&
            Objects.equals(columns, other.columns) &&
            Objects.equals(attributes, other.attributes) &&
            Objects.equals(durability, other.durability) &&
            Objects.equals(cellVisibility, other.cellVisibility);
    }

    @Override
    public int hashCode() {
        return Arrays.deepHashCode(new Object[] {
            row,
            columns,
            attributes,
            durability,
            cellVisibility
        });
    }
}
