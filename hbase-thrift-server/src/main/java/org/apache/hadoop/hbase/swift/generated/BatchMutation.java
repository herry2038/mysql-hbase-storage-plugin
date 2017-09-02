package org.apache.hadoop.hbase.swift.generated;

import com.facebook.swift.codec.*;
import com.facebook.swift.codec.ThriftField.Requiredness;
import com.facebook.swift.codec.ThriftField.Recursiveness;
import java.util.*;

import static com.google.common.base.Objects.toStringHelper;

@ThriftStruct("BatchMutation")
public final class BatchMutation
{
    @ThriftConstructor
    public BatchMutation(
        @ThriftField(value=1, name="row", requiredness=Requiredness.NONE) final byte[] row,
        @ThriftField(value=2, name="mutations", requiredness=Requiredness.NONE) final List<Mutation> mutations
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
        private List<Mutation> mutations;

        public Builder setMutations(List<Mutation> mutations) {
            this.mutations = mutations;
            return this;
        }

        public Builder() { }
        public Builder(BatchMutation other) {
            this.row = other.row;
            this.mutations = other.mutations;
        }

        public BatchMutation build() {
            return new BatchMutation (
                this.row,
                this.mutations
            );
        }
    }

    private final byte[] row;

    @ThriftField(value=1, name="row", requiredness=Requiredness.NONE)
    public byte[] getRow() { return row; }

    private final List<Mutation> mutations;

    @ThriftField(value=2, name="mutations", requiredness=Requiredness.NONE)
    public List<Mutation> getMutations() { return mutations; }

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

        BatchMutation other = (BatchMutation)o;

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
