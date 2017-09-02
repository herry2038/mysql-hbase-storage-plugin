package org.apache.hadoop.hbase.swift2.generated;

import com.facebook.swift.codec.*;
import com.facebook.swift.codec.ThriftField.Requiredness;
import com.facebook.swift.codec.ThriftField.Recursiveness;
import java.util.*;

@ThriftStruct("TIllegalArgument")
public final class TIllegalArgument extends Exception
{
    private static final long serialVersionUID = 1L;

    @ThriftConstructor
    public TIllegalArgument(
        @ThriftField(value=1, name="message", requiredness=Requiredness.OPTIONAL) final String message
    ) {
        this.message = message;
    }

    public static class Builder {
        private String message;

        public Builder setMessage(String message) {
            this.message = message;
            return this;
        }

        public Builder() { }
        public Builder(TIllegalArgument other) {
            this.message = other.message;
        }

        public TIllegalArgument build() {
            return new TIllegalArgument (
                this.message
            );
        }
    }

    private final String message;

    @ThriftField(value=1, name="message", requiredness=Requiredness.OPTIONAL)
    public String getMessage() { return message; }
}
