package org.apache.hadoop.hbase.swift2.generated;

import com.facebook.swift.codec.*;
import com.facebook.swift.codec.ThriftField.Requiredness;
import com.facebook.swift.codec.ThriftField.Recursiveness;
import java.util.*;

import static com.google.common.base.Objects.toStringHelper;

@ThriftStruct("TPut")
public final class TPut
{
    @ThriftConstructor
    public TPut(
        @ThriftField(value=1, name="row", requiredness=Requiredness.REQUIRED) final byte[] row,
        @ThriftField(value=2, name="columnValues", requiredness=Requiredness.REQUIRED) final List<TColumnValue> columnValues,
        @ThriftField(value=3, name="timestamp", requiredness=Requiredness.OPTIONAL) final Long timestamp,
        @ThriftField(value=5, name="attributes", requiredness=Requiredness.OPTIONAL) final Map<byte[], byte[]> attributes,
        @ThriftField(value=6, name="durability", requiredness=Requiredness.OPTIONAL) final TDurability durability,
        @ThriftField(value=7, name="cellVisibility", requiredness=Requiredness.OPTIONAL) final TCellVisibility cellVisibility
    ) {
        this.row = row;
        this.columnValues = columnValues;
        this.timestamp = timestamp;
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
        private List<TColumnValue> columnValues;

        public Builder setColumnValues(List<TColumnValue> columnValues) {
            this.columnValues = columnValues;
            return this;
        }
        private Long timestamp;

        public Builder setTimestamp(Long timestamp) {
            this.timestamp = timestamp;
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
        public Builder(TPut other) {
            this.row = other.row;
            this.columnValues = other.columnValues;
            this.timestamp = other.timestamp;
            this.attributes = other.attributes;
            this.durability = other.durability;
            this.cellVisibility = other.cellVisibility;
        }

        public TPut build() {
            return new TPut (
                this.row,
                this.columnValues,
                this.timestamp,
                this.attributes,
                this.durability,
                this.cellVisibility
            );
        }
    }

    private final byte[] row;

    @ThriftField(value=1, name="row", requiredness=Requiredness.REQUIRED)
    public byte[] getRow() { return row; }

    private final List<TColumnValue> columnValues;

    @ThriftField(value=2, name="columnValues", requiredness=Requiredness.REQUIRED)
    public List<TColumnValue> getColumnValues() { return columnValues; }

    private final Long timestamp;

    @ThriftField(value=3, name="timestamp", requiredness=Requiredness.OPTIONAL)
    public Long getTimestamp() { return timestamp; }

    private final Map<byte[], byte[]> attributes;

    @ThriftField(value=5, name="attributes", requiredness=Requiredness.OPTIONAL)
    public Map<byte[], byte[]> getAttributes() { return attributes; }

    private final TDurability durability;

    @ThriftField(value=6, name="durability", requiredness=Requiredness.OPTIONAL)
    public TDurability getDurability() { return durability; }

    private final TCellVisibility cellVisibility;

    @ThriftField(value=7, name="cellVisibility", requiredness=Requiredness.OPTIONAL)
    public TCellVisibility getCellVisibility() { return cellVisibility; }

    @Override
    public String toString()
    {
        return toStringHelper(this)
            .add("row", row)
            .add("columnValues", columnValues)
            .add("timestamp", timestamp)
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

        TPut other = (TPut)o;

        return
            Arrays.equals(row, other.row) &&
            Objects.equals(columnValues, other.columnValues) &&
            Objects.equals(timestamp, other.timestamp) &&
            Objects.equals(attributes, other.attributes) &&
            Objects.equals(durability, other.durability) &&
            Objects.equals(cellVisibility, other.cellVisibility);
    }

    @Override
    public int hashCode() {
        return Arrays.deepHashCode(new Object[] {
            row,
            columnValues,
            timestamp,
            attributes,
            durability,
            cellVisibility
        });
    }
}
