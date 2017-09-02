package org.apache.hadoop.hbase.swift2.generated;

import com.facebook.swift.codec.*;
import com.facebook.swift.codec.ThriftField.Requiredness;
import com.facebook.swift.codec.ThriftField.Recursiveness;
import java.util.*;

import static com.google.common.base.Objects.toStringHelper;

@ThriftStruct("TColumnValue")
public final class TColumnValue
{
    @ThriftConstructor
    public TColumnValue(
        @ThriftField(value=1, name="family", requiredness=Requiredness.REQUIRED) final byte[] family,
        @ThriftField(value=2, name="qualifier", requiredness=Requiredness.REQUIRED) final byte[] qualifier,
        @ThriftField(value=3, name="value", requiredness=Requiredness.REQUIRED) final byte[] value,
        @ThriftField(value=4, name="timestamp", requiredness=Requiredness.OPTIONAL) final Long timestamp,
        @ThriftField(value=5, name="tags", requiredness=Requiredness.OPTIONAL) final byte[] tags
    ) {
        this.family = family;
        this.qualifier = qualifier;
        this.value = value;
        this.timestamp = timestamp;
        this.tags = tags;
    }

    public static class Builder {
        private byte[] family;

        public Builder setFamily(byte[] family) {
            this.family = family;
            return this;
        }
        private byte[] qualifier;

        public Builder setQualifier(byte[] qualifier) {
            this.qualifier = qualifier;
            return this;
        }
        private byte[] value;

        public Builder setValue(byte[] value) {
            this.value = value;
            return this;
        }
        private Long timestamp;

        public Builder setTimestamp(Long timestamp) {
            this.timestamp = timestamp;
            return this;
        }
        private byte[] tags;

        public Builder setTags(byte[] tags) {
            this.tags = tags;
            return this;
        }

        public Builder() { }
        public Builder(TColumnValue other) {
            this.family = other.family;
            this.qualifier = other.qualifier;
            this.value = other.value;
            this.timestamp = other.timestamp;
            this.tags = other.tags;
        }

        public TColumnValue build() {
            return new TColumnValue (
                this.family,
                this.qualifier,
                this.value,
                this.timestamp,
                this.tags
            );
        }
    }

    private final byte[] family;

    @ThriftField(value=1, name="family", requiredness=Requiredness.REQUIRED)
    public byte[] getFamily() { return family; }

    private final byte[] qualifier;

    @ThriftField(value=2, name="qualifier", requiredness=Requiredness.REQUIRED)
    public byte[] getQualifier() { return qualifier; }

    private final byte[] value;

    @ThriftField(value=3, name="value", requiredness=Requiredness.REQUIRED)
    public byte[] getValue() { return value; }

    private final Long timestamp;

    @ThriftField(value=4, name="timestamp", requiredness=Requiredness.OPTIONAL)
    public Long getTimestamp() { return timestamp; }

    private final byte[] tags;

    @ThriftField(value=5, name="tags", requiredness=Requiredness.OPTIONAL)
    public byte[] getTags() { return tags; }

    @Override
    public String toString()
    {
        return toStringHelper(this)
            .add("family", family)
            .add("qualifier", qualifier)
            .add("value", value)
            .add("timestamp", timestamp)
            .add("tags", tags)
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

        TColumnValue other = (TColumnValue)o;

        return
            Arrays.equals(family, other.family) &&
            Arrays.equals(qualifier, other.qualifier) &&
            Arrays.equals(value, other.value) &&
            Objects.equals(timestamp, other.timestamp) &&
            Arrays.equals(tags, other.tags);
    }

    @Override
    public int hashCode() {
        return Arrays.deepHashCode(new Object[] {
            family,
            qualifier,
            value,
            timestamp,
            tags
        });
    }
}
