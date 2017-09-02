package org.apache.hadoop.hbase.swift.generated;

import com.facebook.swift.codec.*;
import com.facebook.swift.codec.ThriftField.Requiredness;
import com.facebook.swift.codec.ThriftField.Recursiveness;
import java.util.*;

@ThriftStruct("IOError")
public final class IOError extends Exception
{
    private static final long serialVersionUID = 1L;

    @ThriftConstructor
    public IOError(
        @ThriftField(value=1, name="message", requiredness=Requiredness.NONE) final String message
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
        public Builder(IOError other) {
            this.message = other.message;
        }

        public IOError build() {
            return new IOError (
                this.message
            );
        }
    }

    private final String message;

    @ThriftField(value=1, name="message", requiredness=Requiredness.NONE)
    public String getMessage() { return message; }
}
