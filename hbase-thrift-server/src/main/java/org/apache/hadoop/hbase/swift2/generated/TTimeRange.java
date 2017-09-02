package org.apache.hadoop.hbase.swift2.generated;

import com.facebook.swift.codec.*;
import com.facebook.swift.codec.ThriftField.Requiredness;
import com.facebook.swift.codec.ThriftField.Recursiveness;
import java.util.*;

import static com.google.common.base.Objects.toStringHelper;

@ThriftStruct("TTimeRange")
public final class TTimeRange
{
    @ThriftConstructor
    public TTimeRange(
        @ThriftField(value=1, name="minStamp", requiredness=Requiredness.REQUIRED) final long minStamp,
        @ThriftField(value=2, name="maxStamp", requiredness=Requiredness.REQUIRED) final long maxStamp
    ) {
        this.minStamp = minStamp;
        this.maxStamp = maxStamp;
    }

    public static class Builder {
        private long minStamp;

        public Builder setMinStamp(long minStamp) {
            this.minStamp = minStamp;
            return this;
        }
        private long maxStamp;

        public Builder setMaxStamp(long maxStamp) {
            this.maxStamp = maxStamp;
            return this;
        }

        public Builder() { }
        public Builder(TTimeRange other) {
            this.minStamp = other.minStamp;
            this.maxStamp = other.maxStamp;
        }

        public TTimeRange build() {
            return new TTimeRange (
                this.minStamp,
                this.maxStamp
            );
        }
    }

    private final long minStamp;

    @ThriftField(value=1, name="minStamp", requiredness=Requiredness.REQUIRED)
    public long getMinStamp() { return minStamp; }

    private final long maxStamp;

    @ThriftField(value=2, name="maxStamp", requiredness=Requiredness.REQUIRED)
    public long getMaxStamp() { return maxStamp; }

    @Override
    public String toString()
    {
        return toStringHelper(this)
            .add("minStamp", minStamp)
            .add("maxStamp", maxStamp)
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

        TTimeRange other = (TTimeRange)o;

        return
            Objects.equals(minStamp, other.minStamp) &&
            Objects.equals(maxStamp, other.maxStamp);
    }

    @Override
    public int hashCode() {
        return Arrays.deepHashCode(new Object[] {
            minStamp,
            maxStamp
        });
    }
}
