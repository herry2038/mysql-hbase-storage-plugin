package org.apache.hadoop.hbase.swift.generated;

import com.facebook.swift.codec.*;
import com.facebook.swift.codec.ThriftField.Requiredness;
import com.facebook.swift.codec.ThriftField.Recursiveness;
import java.util.*;

import static com.google.common.base.Objects.toStringHelper;

@ThriftStruct("ColumnDescriptor")
public final class ColumnDescriptor
{
    @ThriftConstructor
    public ColumnDescriptor(
        @ThriftField(value=1, name="name", requiredness=Requiredness.NONE) final byte[] name,
        @ThriftField(value=2, name="maxVersions", requiredness=Requiredness.NONE) final int maxVersions,
        @ThriftField(value=3, name="compression", requiredness=Requiredness.NONE) final String compression,
        @ThriftField(value=4, name="inMemory", requiredness=Requiredness.NONE) final boolean inMemory,
        @ThriftField(value=5, name="bloomFilterType", requiredness=Requiredness.NONE) final String bloomFilterType,
        @ThriftField(value=6, name="bloomFilterVectorSize", requiredness=Requiredness.NONE) final int bloomFilterVectorSize,
        @ThriftField(value=7, name="bloomFilterNbHashes", requiredness=Requiredness.NONE) final int bloomFilterNbHashes,
        @ThriftField(value=8, name="blockCacheEnabled", requiredness=Requiredness.NONE) final boolean blockCacheEnabled,
        @ThriftField(value=9, name="timeToLive", requiredness=Requiredness.NONE) final int timeToLive
    ) {
        this.name = name;
        this.maxVersions = maxVersions;
        this.compression = compression;
        this.inMemory = inMemory;
        this.bloomFilterType = bloomFilterType;
        this.bloomFilterVectorSize = bloomFilterVectorSize;
        this.bloomFilterNbHashes = bloomFilterNbHashes;
        this.blockCacheEnabled = blockCacheEnabled;
        this.timeToLive = timeToLive;
    }

    public static class Builder {
        private byte[] name;

        public Builder setName(byte[] name) {
            this.name = name;
            return this;
        }
        private int maxVersions;

        public Builder setMaxVersions(int maxVersions) {
            this.maxVersions = maxVersions;
            return this;
        }
        private String compression;

        public Builder setCompression(String compression) {
            this.compression = compression;
            return this;
        }
        private boolean inMemory;

        public Builder setInMemory(boolean inMemory) {
            this.inMemory = inMemory;
            return this;
        }
        private String bloomFilterType;

        public Builder setBloomFilterType(String bloomFilterType) {
            this.bloomFilterType = bloomFilterType;
            return this;
        }
        private int bloomFilterVectorSize;

        public Builder setBloomFilterVectorSize(int bloomFilterVectorSize) {
            this.bloomFilterVectorSize = bloomFilterVectorSize;
            return this;
        }
        private int bloomFilterNbHashes;

        public Builder setBloomFilterNbHashes(int bloomFilterNbHashes) {
            this.bloomFilterNbHashes = bloomFilterNbHashes;
            return this;
        }
        private boolean blockCacheEnabled;

        public Builder setBlockCacheEnabled(boolean blockCacheEnabled) {
            this.blockCacheEnabled = blockCacheEnabled;
            return this;
        }
        private int timeToLive;

        public Builder setTimeToLive(int timeToLive) {
            this.timeToLive = timeToLive;
            return this;
        }

        public Builder() { }
        public Builder(ColumnDescriptor other) {
            this.name = other.name;
            this.maxVersions = other.maxVersions;
            this.compression = other.compression;
            this.inMemory = other.inMemory;
            this.bloomFilterType = other.bloomFilterType;
            this.bloomFilterVectorSize = other.bloomFilterVectorSize;
            this.bloomFilterNbHashes = other.bloomFilterNbHashes;
            this.blockCacheEnabled = other.blockCacheEnabled;
            this.timeToLive = other.timeToLive;
        }

        public ColumnDescriptor build() {
            return new ColumnDescriptor (
                this.name,
                this.maxVersions,
                this.compression,
                this.inMemory,
                this.bloomFilterType,
                this.bloomFilterVectorSize,
                this.bloomFilterNbHashes,
                this.blockCacheEnabled,
                this.timeToLive
            );
        }
    }

    private final byte[] name;

    @ThriftField(value=1, name="name", requiredness=Requiredness.NONE)
    public byte[] getName() { return name; }

    private final int maxVersions;

    @ThriftField(value=2, name="maxVersions", requiredness=Requiredness.NONE)
    public int getMaxVersions() { return maxVersions; }

    private final String compression;

    @ThriftField(value=3, name="compression", requiredness=Requiredness.NONE)
    public String getCompression() { return compression; }

    private final boolean inMemory;

    @ThriftField(value=4, name="inMemory", requiredness=Requiredness.NONE)
    public boolean isInMemory() { return inMemory; }

    private final String bloomFilterType;

    @ThriftField(value=5, name="bloomFilterType", requiredness=Requiredness.NONE)
    public String getBloomFilterType() { return bloomFilterType; }

    private final int bloomFilterVectorSize;

    @ThriftField(value=6, name="bloomFilterVectorSize", requiredness=Requiredness.NONE)
    public int getBloomFilterVectorSize() { return bloomFilterVectorSize; }

    private final int bloomFilterNbHashes;

    @ThriftField(value=7, name="bloomFilterNbHashes", requiredness=Requiredness.NONE)
    public int getBloomFilterNbHashes() { return bloomFilterNbHashes; }

    private final boolean blockCacheEnabled;

    @ThriftField(value=8, name="blockCacheEnabled", requiredness=Requiredness.NONE)
    public boolean isBlockCacheEnabled() { return blockCacheEnabled; }

    private final int timeToLive;

    @ThriftField(value=9, name="timeToLive", requiredness=Requiredness.NONE)
    public int getTimeToLive() { return timeToLive; }

    @Override
    public String toString()
    {
        return toStringHelper(this)
            .add("name", name)
            .add("maxVersions", maxVersions)
            .add("compression", compression)
            .add("inMemory", inMemory)
            .add("bloomFilterType", bloomFilterType)
            .add("bloomFilterVectorSize", bloomFilterVectorSize)
            .add("bloomFilterNbHashes", bloomFilterNbHashes)
            .add("blockCacheEnabled", blockCacheEnabled)
            .add("timeToLive", timeToLive)
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

        ColumnDescriptor other = (ColumnDescriptor)o;

        return
            Arrays.equals(name, other.name) &&
            Objects.equals(maxVersions, other.maxVersions) &&
            Objects.equals(compression, other.compression) &&
            Objects.equals(inMemory, other.inMemory) &&
            Objects.equals(bloomFilterType, other.bloomFilterType) &&
            Objects.equals(bloomFilterVectorSize, other.bloomFilterVectorSize) &&
            Objects.equals(bloomFilterNbHashes, other.bloomFilterNbHashes) &&
            Objects.equals(blockCacheEnabled, other.blockCacheEnabled) &&
            Objects.equals(timeToLive, other.timeToLive);
    }

    @Override
    public int hashCode() {
        return Arrays.deepHashCode(new Object[] {
            name,
            maxVersions,
            compression,
            inMemory,
            bloomFilterType,
            bloomFilterVectorSize,
            bloomFilterNbHashes,
            blockCacheEnabled,
            timeToLive
        });
    }
}
