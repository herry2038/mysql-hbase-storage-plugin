package org.apache.hadoop.hbase.swift2.generated;

import com.facebook.swift.codec.*;
import com.facebook.swift.codec.ThriftField.Requiredness;
import com.facebook.swift.codec.ThriftField.Recursiveness;
import java.util.*;

import static com.google.common.base.Objects.toStringHelper;

@ThriftStruct("TRowMutations")
public final class TRowMutations
{
    @ThriftConstructor
    public TRowMutations(
        @ThriftField(value=1, name="row", requiredness=Requiredness.REQUIRED) final byte[] row,
        @ThriftField(value=2, name="mutations", requiredness=Requiredness.REQUIRED) final List<TMutation> mutations
    ) {
        this.row = row;
        this.mutations = mutations;
    }

    public static class Builder {
        private byte[] row;

        public Builder setRow(byte[] row) {
            this.row = row;
            return this;
        }
        private List<TMutation> mutations;

        public Builder setMutations(List<TMutation> mutations) {
            this.mutations = mutations;
            return this;
        }

        public Builder() { }
        public Builder(TRowMutations other) {
            this.row = other.row;
            this.mutations = other.mutations;
        }

        public TRowMutations build() {
            return new TRowMutations (
                this.row,
                this.mutations
            );
        }
    }

    private final byte[] row;

    @ThriftField(value=1, name="row", requiredness=Requiredness.REQUIRED)
    public byte[] getRow() { return row; }

    private final List<TMutation> mutations;

    @ThriftField(value=2, name="mutations", requiredness=Requiredness.REQUIRED)
    public List<TMutation> getMutations() { return mutations; }

    @Override
    public String toString()
    {
        return toStringHelper(this)
            .add("row", row)
            .add("mutations", mutations)
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

        TRowMutations other = (TRowMutations)o;

        return
            Arrays.equals(row, other.row) &&
            Objects.equals(mutations, other.mutations);
    }

    @Override
    public int hashCode() {
        return Arrays.deepHashCode(new Object[] {
            row,
            mutations
        });
    }
}
