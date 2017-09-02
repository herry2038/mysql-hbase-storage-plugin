package org.apache.hadoop.hbase.swift2.generated;

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
        @ThriftField(value=1, name="family", requiredness=Requiredness.REQUIRED) final byte[] family,
        @ThriftField(value=2, name="qualifier", requiredness=Requiredness.OPTIONAL) final byte[] qualifier,
        @ThriftField(value=3, name="timestamp", requiredness=Requiredness.OPTIONAL) final Long timestamp
    ) {
        this.family = family;
        this.qualifier = qualifier;
        this.timestamp = timestamp;
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
        private Long timestamp;

        public Builder setTimestamp(Long timestamp) {
            this.timestamp = timestamp;
            return this;
        }

        public Builder() { }
        public Builder(TColumn other) {
            this.family = other.family;
            this.qualifier = other.qualifier;
            this.timestamp = other.timestamp;
        }

        public TColumn build() {
            return new TColumn (
                this.family,
                this.qualifier,
                this.timestamp
            );
        }
    }

    private final byte[] family;

    @ThriftField(value=1, name="family", requiredness=Requiredness.REQUIRED)
    public byte[] getFamily() { return family; }

    private final byte[] qualifier;

    @ThriftField(value=2, name="qualifier", requiredness=Requiredness.OPTIONAL)
    public byte[] getQualifier() { return qualifier; }

    private final Long timestamp;

    @ThriftField(value=3, name="timestamp", requiredness=Requiredness.OPTIONAL)
    public Long getTimestamp() { return timestamp; }

    @Override
    public String toString()
    {
        return toStringHelper(this)
            .add("family", family)
            .add("qualifier", qualifier)
            .add("timestamp", timestamp)
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
            Arrays.equals(family, other.family) &&
            Arrays.equals(qualifier, other.qualifier) &&
            Objects.equals(timestamp, other.timestamp);
    }

    @Override
    public int hashCode() {
        return Arrays.deepHashCode(new Object[] {
            family,
            qualifier,
            timestamp
        });
    }
}
