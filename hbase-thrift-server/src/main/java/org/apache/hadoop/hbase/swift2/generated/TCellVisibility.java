package org.apache.hadoop.hbase.swift2.generated;

import com.facebook.swift.codec.*;
import com.facebook.swift.codec.ThriftField.Requiredness;
import com.facebook.swift.codec.ThriftField.Recursiveness;
import java.util.*;

import static com.google.common.base.Objects.toStringHelper;

@ThriftStruct("TCellVisibility")
public final class TCellVisibility
{
    @ThriftConstructor
    public TCellVisibility(
        @ThriftField(value=1, name="expression", requiredness=Requiredness.OPTIONAL) final String expression
    ) {
        this.expression = expression;
    }

    public static class Builder {
        private String expression;

        public Builder setExpression(String expression) {
            this.expression = expression;
            return this;
        }

        public Builder() { }
        public Builder(TCellVisibility other) {
            this.expression = other.expression;
        }

        public TCellVisibility build() {
            return new TCellVisibility (
                this.expression
            );
        }
    }

    private final String expression;

    @ThriftField(value=1, name="expression", requiredness=Requiredness.OPTIONAL)
    public String getExpression() { return expression; }

    @Override
    public String toString()
    {
        return toStringHelper(this)
            .add("expression", expression)
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

        TCellVisibility other = (TCellVisibility)o;

        return
            Objects.equals(expression, other.expression);
    }

    @Override
    public int hashCode() {
        return Arrays.deepHashCode(new Object[] {
            expression
        });
    }
}
