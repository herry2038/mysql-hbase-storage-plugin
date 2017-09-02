package org.apache.hadoop.hbase.swift2.generated;

import com.facebook.swift.codec.*;
import com.facebook.swift.codec.ThriftField.Requiredness;
import com.facebook.swift.codec.ThriftField.Recursiveness;
import java.util.*;

import static com.google.common.base.Objects.toStringHelper;

@ThriftStruct("TColumnIncrement")
public final class TColumnIncrement
{
    @ThriftConstructor
    public TColumnIncrement(
        @ThriftField(value=1, name="family", requiredness=Requiredness.REQUIRED) final byte[] family,
        @ThriftField(value=2, name="qualifier", requiredness=Requiredness.REQUIRED) final byte[] qualifier,
        @ThriftField(value=3, name="amount", requiredness=Requiredness.OPTIONAL) final Long amount
    ) {
        this.family = family;
        this.qualifier = qualifier;
        this.amount = amount;
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
        private Long amount;

        public Builder setAmount(Long amount) {
            this.amount = amount;
            return this;
        }

        public Builder() { }
        public Builder(TColumnIncrement other) {
            this.family = other.family;
            this.qualifier = other.qualifier;
            this.amount = other.amount;
        }

        public TColumnIncrement build() {
            return new TColumnIncrement (
                this.family,
                this.qualifier,
                this.amount
            );
        }
    }

    private final byte[] family;

    @ThriftField(value=1, name="family", requiredness=Requiredness.REQUIRED)
    public byte[] getFamily() { return family; }

    private final byte[] qualifier;

    @ThriftField(value=2, name="qualifier", requiredness=Requiredness.REQUIRED)
    public byte[] getQualifier() { return qualifier; }

    private final Long amount;

    @ThriftField(value=3, name="amount", requiredness=Requiredness.OPTIONAL)
    public Long getAmount() { return amount; }

    @Override
    public String toString()
    {
        return toStringHelper(this)
            .add("family", family)
            .add("qualifier", qualifier)
            .add("amount", amount)
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

        TColumnIncrement other = (TColumnIncrement)o;

        return
            Arrays.equals(family, other.family) &&
            Arrays.equals(qualifier, other.qualifier) &&
            Objects.equals(amount, other.amount);
    }

    @Override
    public int hashCode() {
        return Arrays.deepHashCode(new Object[] {
            family,
            qualifier,
            amount
        });
    }
}
