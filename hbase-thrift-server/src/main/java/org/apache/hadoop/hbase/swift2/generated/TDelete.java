package org.apache.hadoop.hbase.swift2.generated;

import com.facebook.swift.codec.*;
import com.facebook.swift.codec.ThriftField.Requiredness;
import com.facebook.swift.codec.ThriftField.Recursiveness;
import java.util.*;

import static com.google.common.base.Objects.toStringHelper;

@ThriftStruct("TDelete")
public final class TDelete
{
    @ThriftConstructor
    public TDelete(
        @ThriftField(value=1, name="row", requiredness=Requiredness.REQUIRED) final byte[] row,
        @ThriftField(value=2, name="columns", requiredness=Requiredness.OPTIONAL) final List<TColumn> columns,
        @ThriftField(value=3, name="timestamp", requiredness=Requiredness.OPTIONAL) final Long timestamp,
        @ThriftField(value=4, name="deleteType", requiredness=Requiredness.OPTIONAL) final TDeleteType deleteType,
        @ThriftField(value=6, name="attributes", requiredness=Requiredness.OPTIONAL) final Map<byte[], byte[]> attributes,
        @ThriftField(value=7, name="durability", requiredness=Requiredness.OPTIONAL) final TDurability durability
    ) {
        this.row = row;
        this.columns = columns;
        this.timestamp = timestamp;
        this.deleteType = deleteType;
        this.attributes = attributes;
        this.durability = durability;
    }

    public static class Builder {
        private byte[] row;

        public Builder setRow(byte[] row) {
            this.row = row;
            return this;
        }
        private List<TColumn> columns;

        public Builder setColumns(List<TColumn> columns) {
            this.columns = columns;
            return this;
        }
        private Long timestamp;

        public Builder setTimestamp(Long timestamp) {
            this.timestamp = timestamp;
            return this;
        }
        private TDeleteType deleteType;

        public Builder setDeleteType(TDeleteType deleteType) {
            this.deleteType = deleteType;
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

        public Builder() { }
        public Builder(TDelete other) {
            this.row = other.row;
            this.columns = other.columns;
            this.timestamp = other.timestamp;
            this.deleteType = other.deleteType;
            this.attributes = other.attributes;
            this.durability = other.durability;
        }

        public TDelete build() {
            return new TDelete (
                this.row,
                this.columns,
                this.timestamp,
                this.deleteType,
                this.attributes,
                this.durability
            );
        }
    }

    private final byte[] row;

    @ThriftField(value=1, name="row", requiredness=Requiredness.REQUIRED)
    public byte[] getRow() { return row; }

    private final List<TColumn> columns;

    @ThriftField(value=2, name="columns", requiredness=Requiredness.OPTIONAL)
    public List<TColumn> getColumns() { return columns; }

    private final Long timestamp;

    @ThriftField(value=3, name="timestamp", requiredness=Requiredness.OPTIONAL)
    public Long getTimestamp() { return timestamp; }

    private final TDeleteType deleteType;

    @ThriftField(value=4, name="deleteType", requiredness=Requiredness.OPTIONAL)
    public TDeleteType getDeleteType() { return deleteType; }

    private final Map<byte[], byte[]> attributes;

    @ThriftField(value=6, name="attributes", requiredness=Requiredness.OPTIONAL)
    public Map<byte[], byte[]> getAttributes() { return attributes; }

    private final TDurability durability;

    @ThriftField(value=7, name="durability", requiredness=Requiredness.OPTIONAL)
    public TDurability getDurability() { return durability; }

    @Override
    public String toString()
    {
        return toStringHelper(this)
            .add("row", row)
            .add("columns", columns)
            .add("timestamp", timestamp)
            .add("deleteType", deleteType)
            .add("attributes", attributes)
            .add("durability", durability)
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

        TDelete other = (TDelete)o;

        return
            Arrays.equals(row, other.row) &&
            Objects.equals(columns, other.columns) &&
            Objects.equals(timestamp, other.timestamp) &&
            Objects.equals(deleteType, other.deleteType) &&
            Objects.equals(attributes, other.attributes) &&
            Objects.equals(durability, other.durability);
    }

    @Override
    public int hashCode() {
        return Arrays.deepHashCode(new Object[] {
            row,
            columns,
            timestamp,
            deleteType,
            attributes,
            durability
        });
    }
}
