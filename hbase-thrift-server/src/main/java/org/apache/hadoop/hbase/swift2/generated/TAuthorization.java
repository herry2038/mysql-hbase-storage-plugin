package org.apache.hadoop.hbase.swift2.generated;

import com.facebook.swift.codec.*;
import com.facebook.swift.codec.ThriftField.Requiredness;
import com.facebook.swift.codec.ThriftField.Recursiveness;
import java.util.*;

import static com.google.common.base.Objects.toStringHelper;

@ThriftStruct("TAuthorization")
public final class TAuthorization
{
    @ThriftConstructor
    public TAuthorization(
        @ThriftField(value=1, name="labels", requiredness=Requiredness.OPTIONAL) final List<String> labels
    ) {
        this.labels = labels;
    }

    public static class Builder {
        private List<String> labels;

        public Builder setLabels(List<String> labels) {
            this.labels = labels;
            return this;
        }

        public Builder() { }
        public Builder(TAuthorization other) {
            this.labels = other.labels;
        }

        public TAuthorization build() {
            return new TAuthorization (
                this.labels
            );
        }
    }

    private final List<String> labels;

    @ThriftField(value=1, name="labels", requiredness=Requiredness.OPTIONAL)
    public List<String> getLabels() { return labels; }

    @Override
    public String toString()
    {
        return toStringHelper(this)
            .add("labels", labels)
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

        TAuthorization other = (TAuthorization)o;

        return
            Objects.equals(labels, other.labels);
    }

    @Override
    public int hashCode() {
        return Arrays.deepHashCode(new Object[] {
            labels
        });
    }
}
