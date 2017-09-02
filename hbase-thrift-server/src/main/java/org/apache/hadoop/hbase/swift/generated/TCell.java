package org.apache.hadoop.hbase.swift.generated;

import com.facebook.swift.codec.*;
import com.facebook.swift.codec.ThriftField.Requiredness;
import com.facebook.swift.codec.ThriftField.Recursiveness;
import java.util.*;

import static com.google.common.base.Objects.toStringHelper;

@ThriftStruct("TCell")
public final class TCell
{
    @ThriftConstructor
    public TCell(
        @ThriftField(value=1, name="value", requiredness=Requiredness.NONE) final byte[] value,
        @ThriftField(value=2, name="timestamp", requiredness=Requiredness.NONE) final long timestamp
    ) {
        this.value = value;
        this.timestamp = timestamp;
    }

    public static class Builder {
        private byte[] value;

        public Builder setValue(byte[] value) {
            this.value = value;
            return this;
        }
        private long timestamp;

        public Builder setTimestamp(long timestamp) {
            this.timestamp = timestamp;
            return this;
        }

        public Builder() { }
        public Builder(TCell other) {
            this.value = other.value;
            this.timestamp = other.timestamp;
        }

        public TCell build() {
            return new TCell (
                this.value,
                this.timestamp
            );
        }
    }

    private final byte[] value;

    @ThriftField(value=1, name="value", requiredness=Requiredness.NONE)
    public byte[] getValue() { return value; }

    private final long timestamp;

    @ThriftField(value=2, name="timestamp", requiredness=Requiredness.NONE)
    public long getTimestamp() { return timestamp; }

    @Override
    public String toString()
    {
        return toStringHelper(this)
            .add("value", value)
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

        TCell other = (TCell)o;

        return
            Arrays.equals(value, other.value) &&
            Objects.equals(timestamp, other.timestamp);
    }

    @Override
    public int hashCode() {
        return Arrays.deepHashCode(new Object[] {
            value,
            timestamp
        });
    }
}
