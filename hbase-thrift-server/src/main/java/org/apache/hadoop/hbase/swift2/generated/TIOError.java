package org.apache.hadoop.hbase.swift2.generated;

import com.facebook.swift.codec.*;
import com.facebook.swift.codec.ThriftField.Requiredness;
import com.facebook.swift.codec.ThriftField.Recursiveness;
import java.util.*;

@ThriftStruct("TIOError")
public final class TIOError extends Exception
{
    private static final long serialVersionUID = 1L;

    @ThriftConstructor
    public TIOError(
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
        public Builder(TIOError other) {
            this.message = other.message;
        }

        public TIOError build() {
            return new TIOError (
                this.message
            );
        }
    }

    private final String message;

    @ThriftField(value=1, name="message", requiredness=Requiredness.OPTIONAL)
    public String getMessage() { return message; }
}
