package org.apache.hadoop.hbase.swift.generated;

import com.facebook.swift.codec.*;
import com.facebook.swift.codec.ThriftField.Requiredness;
import com.facebook.swift.codec.ThriftField.Recursiveness;
import java.util.*;

import static com.google.common.base.Objects.toStringHelper;

@ThriftStruct("Mutation")
public final class Mutation
{
    @ThriftConstructor
    public Mutation(
        @ThriftField(value=1, name="isDelete", requiredness=Requiredness.NONE) final boolean isDelete,
        @ThriftField(value=2, name="column", requiredness=Requiredness.NONE) final byte[] column,
        @ThriftField(value=3, name="value", requiredness=Requiredness.NONE) final byte[] value,
        @ThriftField(value=4, name="writeToWAL", requiredness=Requiredness.NONE) final boolean writeToWAL
    ) {
        this.isDelete = isDelete;
        this.column = column;
        this.value = value;
        this.writeToWAL = writeToWAL;
    }

    public static class Builder {
        private boolean isDelete;

        public Builder setIsDelete(boolean isDelete) {
            this.isDelete = isDelete;
            return this;
        }
        private byte[] column;

        public Builder setColumn(byte[] column) {
            this.column = column;
            return this;
        }
        private byte[] value;

        public Builder setValue(byte[] value) {
            this.value = value;
            return this;
        }
        private boolean writeToWAL;

        public Builder setWriteToWAL(boolean writeToWAL) {
            this.writeToWAL = writeToWAL;
            return this;
        }

        public Builder() { }
        public Builder(Mutation other) {
            this.isDelete = other.isDelete;
            this.column = other.column;
            this.value = other.value;
            this.writeToWAL = other.writeToWAL;
        }

        public Mutation build() {
            return new Mutation (
                this.isDelete,
                this.column,
                this.value,
                this.writeToWAL
            );
        }
    }

    private final boolean isDelete;

    @ThriftField(value=1, name="isDelete", requiredness=Requiredness.NONE)
    public boolean isIsDelete() { return isDelete; }

    private final byte[] column;

    @ThriftField(value=2, name="column", requiredness=Requiredness.NONE)
    public byte[] getColumn() { return column; }

    private final byte[] value;

    @ThriftField(value=3, name="value", requiredness=Requiredness.NONE)
    public byte[] getValue() { return value; }

    private final boolean writeToWAL;

    @ThriftField(value=4, name="writeToWAL", requiredness=Requiredness.NONE)
    public boolean isWriteToWAL() { return writeToWAL; }

    @Override
    public String toString()
    {
        return toStringHelper(this)
            .add("isDelete", isDelete)
            .add("column", column)
            .add("value", value)
            .add("writeToWAL", writeToWAL)
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

        Mutation other = (Mutation)o;

        return
            Objects.equals(isDelete, other.isDelete) &&
            Arrays.equals(column, other.column) &&
            Arrays.equals(value, other.value) &&
            Objects.equals(writeToWAL, other.writeToWAL);
    }

    @Override
    public int hashCode() {
        return Arrays.deepHashCode(new Object[] {
            isDelete,
            column,
            value,
            writeToWAL
        });
    }
}
