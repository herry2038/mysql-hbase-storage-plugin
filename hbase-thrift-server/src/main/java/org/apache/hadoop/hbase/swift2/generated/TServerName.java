package org.apache.hadoop.hbase.swift2.generated;

import com.facebook.swift.codec.*;
import com.facebook.swift.codec.ThriftField.Requiredness;
import com.facebook.swift.codec.ThriftField.Recursiveness;
import java.util.*;

import static com.google.common.base.Objects.toStringHelper;

@ThriftStruct("TServerName")
public final class TServerName
{
    @ThriftConstructor
    public TServerName(
        @ThriftField(value=1, name="hostName", requiredness=Requiredness.REQUIRED) final String hostName,
        @ThriftField(value=2, name="port", requiredness=Requiredness.OPTIONAL) final Integer port,
        @ThriftField(value=3, name="startCode", requiredness=Requiredness.OPTIONAL) final Long startCode
    ) {
        this.hostName = hostName;
        this.port = port;
        this.startCode = startCode;
    }

    public static class Builder {
        private String hostName;

        public Builder setHostName(String hostName) {
            this.hostName = hostName;
            return this;
        }
        private Integer port;

        public Builder setPort(Integer port) {
            this.port = port;
            return this;
        }
        private Long startCode;

        public Builder setStartCode(Long startCode) {
            this.startCode = startCode;
            return this;
        }

        public Builder() { }
        public Builder(TServerName other) {
            this.hostName = other.hostName;
            this.port = other.port;
            this.startCode = other.startCode;
        }

        public TServerName build() {
            return new TServerName (
                this.hostName,
                this.port,
                this.startCode
            );
        }
    }

    private final String hostName;

    @ThriftField(value=1, name="hostName", requiredness=Requiredness.REQUIRED)
    public String getHostName() { return hostName; }

    private final Integer port;

    @ThriftField(value=2, name="port", requiredness=Requiredness.OPTIONAL)
    public Integer getPort() { return port; }

    private final Long startCode;

    @ThriftField(value=3, name="startCode", requiredness=Requiredness.OPTIONAL)
    public Long getStartCode() { return startCode; }

    @Override
    public String toString()
    {
        return toStringHelper(this)
            .add("hostName", hostName)
            .add("port", port)
            .add("startCode", startCode)
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

        TServerName other = (TServerName)o;

        return
            Objects.equals(hostName, other.hostName) &&
            Objects.equals(port, other.port) &&
            Objects.equals(startCode, other.startCode);
    }

    @Override
    public int hashCode() {
        return Arrays.deepHashCode(new Object[] {
            hostName,
            port,
            startCode
        });
    }
}
