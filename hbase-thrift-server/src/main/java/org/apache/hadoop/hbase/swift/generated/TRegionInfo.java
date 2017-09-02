package org.apache.hadoop.hbase.swift.generated;

import com.facebook.swift.codec.*;
import com.facebook.swift.codec.ThriftField.Requiredness;
import com.facebook.swift.codec.ThriftField.Recursiveness;
import java.util.*;

import static com.google.common.base.Objects.toStringHelper;

@ThriftStruct("TRegionInfo")
public final class TRegionInfo
{
    @ThriftConstructor
    public TRegionInfo(
        @ThriftField(value=1, name="startKey", requiredness=Requiredness.NONE) final byte[] startKey,
        @ThriftField(value=2, name="endKey", requiredness=Requiredness.NONE) final byte[] endKey,
        @ThriftField(value=3, name="id", requiredness=Requiredness.NONE) final long id,
        @ThriftField(value=4, name="name", requiredness=Requiredness.NONE) final byte[] name,
        @ThriftField(value=5, name="version", requiredness=Requiredness.NONE) final byte version,
        @ThriftField(value=6, name="serverName", requiredness=Requiredness.NONE) final byte[] serverName,
        @ThriftField(value=7, name="port", requiredness=Requiredness.NONE) final int port
    ) {
        this.startKey = startKey;
        this.endKey = endKey;
        this.id = id;
        this.name = name;
        this.version = version;
        this.serverName = serverName;
        this.port = port;
    }

    public static class Builder {
        private byte[] startKey;

        public Builder setStartKey(byte[] startKey) {
            this.startKey = startKey;
            return this;
        }
        private byte[] endKey;

        public Builder setEndKey(byte[] endKey) {
            this.endKey = endKey;
            return this;
        }
        private long id;

        public Builder setId(long id) {
            this.id = id;
            return this;
        }
        private byte[] name;

        public Builder setName(byte[] name) {
            this.name = name;
            return this;
        }
        private byte version;

        public Builder setVersion(byte version) {
            this.version = version;
            return this;
        }
        private byte[] serverName;

        public Builder setServerName(byte[] serverName) {
            this.serverName = serverName;
            return this;
        }
        private int port;

        public Builder setPort(int port) {
            this.port = port;
            return this;
        }

        public Builder() { }
        public Builder(TRegionInfo other) {
            this.startKey = other.startKey;
            this.endKey = other.endKey;
            this.id = other.id;
            this.name = other.name;
            this.version = other.version;
            this.serverName = other.serverName;
            this.port = other.port;
        }

        public TRegionInfo build() {
            return new TRegionInfo (
                this.startKey,
                this.endKey,
                this.id,
                this.name,
                this.version,
                this.serverName,
                this.port
            );
        }
    }

    private final byte[] startKey;

    @ThriftField(value=1, name="startKey", requiredness=Requiredness.NONE)
    public byte[] getStartKey() { return startKey; }

    private final byte[] endKey;

    @ThriftField(value=2, name="endKey", requiredness=Requiredness.NONE)
    public byte[] getEndKey() { return endKey; }

    private final long id;

    @ThriftField(value=3, name="id", requiredness=Requiredness.NONE)
    public long getId() { return id; }

    private final byte[] name;

    @ThriftField(value=4, name="name", requiredness=Requiredness.NONE)
    public byte[] getName() { return name; }

    private final byte version;

    @ThriftField(value=5, name="version", requiredness=Requiredness.NONE)
    public byte getVersion() { return version; }

    private final byte[] serverName;

    @ThriftField(value=6, name="serverName", requiredness=Requiredness.NONE)
    public byte[] getServerName() { return serverName; }

    private final int port;

    @ThriftField(value=7, name="port", requiredness=Requiredness.NONE)
    public int getPort() { return port; }

    @Override
    public String toString()
    {
        return toStringHelper(this)
            .add("startKey", startKey)
            .add("endKey", endKey)
            .add("id", id)
            .add("name", name)
            .add("version", version)
            .add("serverName", serverName)
            .add("port", port)
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

        TRegionInfo other = (TRegionInfo)o;

        return
            Arrays.equals(startKey, other.startKey) &&
            Arrays.equals(endKey, other.endKey) &&
            Objects.equals(id, other.id) &&
            Arrays.equals(name, other.name) &&
            Objects.equals(version, other.version) &&
            Arrays.equals(serverName, other.serverName) &&
            Objects.equals(port, other.port);
    }

    @Override
    public int hashCode() {
        return Arrays.deepHashCode(new Object[] {
            startKey,
            endKey,
            id,
            name,
            version,
            serverName,
            port
        });
    }
}
