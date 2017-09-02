package org.apache.hadoop.hbase.swift2.generated;

import com.facebook.swift.codec.*;
import com.facebook.swift.codec.ThriftField.Requiredness;
import com.facebook.swift.codec.ThriftField.Recursiveness;
import java.util.*;

import static com.google.common.base.Objects.toStringHelper;

@ThriftStruct("THRegionInfo")
public final class THRegionInfo
{
    @ThriftConstructor
    public THRegionInfo(
        @ThriftField(value=1, name="regionId", requiredness=Requiredness.REQUIRED) final long regionId,
        @ThriftField(value=2, name="tableName", requiredness=Requiredness.REQUIRED) final byte[] tableName,
        @ThriftField(value=3, name="startKey", requiredness=Requiredness.OPTIONAL) final byte[] startKey,
        @ThriftField(value=4, name="endKey", requiredness=Requiredness.OPTIONAL) final byte[] endKey,
        @ThriftField(value=5, name="offline", requiredness=Requiredness.OPTIONAL) final Boolean offline,
        @ThriftField(value=6, name="split", requiredness=Requiredness.OPTIONAL) final Boolean split,
        @ThriftField(value=7, name="replicaId", requiredness=Requiredness.OPTIONAL) final Integer replicaId
    ) {
        this.regionId = regionId;
        this.tableName = tableName;
        this.startKey = startKey;
        this.endKey = endKey;
        this.offline = offline;
        this.split = split;
        this.replicaId = replicaId;
    }

    public static class Builder {
        private long regionId;

        public Builder setRegionId(long regionId) {
            this.regionId = regionId;
            return this;
        }
        private byte[] tableName;

        public Builder setTableName(byte[] tableName) {
            this.tableName = tableName;
            return this;
        }
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
        private Boolean offline;

        public Builder setOffline(Boolean offline) {
            this.offline = offline;
            return this;
        }
        private Boolean split;

        public Builder setSplit(Boolean split) {
            this.split = split;
            return this;
        }
        private Integer replicaId;

        public Builder setReplicaId(Integer replicaId) {
            this.replicaId = replicaId;
            return this;
        }

        public Builder() { }
        public Builder(THRegionInfo other) {
            this.regionId = other.regionId;
            this.tableName = other.tableName;
            this.startKey = other.startKey;
            this.endKey = other.endKey;
            this.offline = other.offline;
            this.split = other.split;
            this.replicaId = other.replicaId;
        }

        public THRegionInfo build() {
            return new THRegionInfo (
                this.regionId,
                this.tableName,
                this.startKey,
                this.endKey,
                this.offline,
                this.split,
                this.replicaId
            );
        }
    }

    private final long regionId;

    @ThriftField(value=1, name="regionId", requiredness=Requiredness.REQUIRED)
    public long getRegionId() { return regionId; }

    private final byte[] tableName;

    @ThriftField(value=2, name="tableName", requiredness=Requiredness.REQUIRED)
    public byte[] getTableName() { return tableName; }

    private final byte[] startKey;

    @ThriftField(value=3, name="startKey", requiredness=Requiredness.OPTIONAL)
    public byte[] getStartKey() { return startKey; }

    private final byte[] endKey;

    @ThriftField(value=4, name="endKey", requiredness=Requiredness.OPTIONAL)
    public byte[] getEndKey() { return endKey; }

    private final Boolean offline;

    @ThriftField(value=5, name="offline", requiredness=Requiredness.OPTIONAL)
    public Boolean isOffline() { return offline; }

    private final Boolean split;

    @ThriftField(value=6, name="split", requiredness=Requiredness.OPTIONAL)
    public Boolean isSplit() { return split; }

    private final Integer replicaId;

    @ThriftField(value=7, name="replicaId", requiredness=Requiredness.OPTIONAL)
    public Integer getReplicaId() { return replicaId; }

    @Override
    public String toString()
    {
        return toStringHelper(this)
            .add("regionId", regionId)
            .add("tableName", tableName)
            .add("startKey", startKey)
            .add("endKey", endKey)
            .add("offline", offline)
            .add("split", split)
            .add("replicaId", replicaId)
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

        THRegionInfo other = (THRegionInfo)o;

        return
            Objects.equals(regionId, other.regionId) &&
            Arrays.equals(tableName, other.tableName) &&
            Arrays.equals(startKey, other.startKey) &&
            Arrays.equals(endKey, other.endKey) &&
            Objects.equals(offline, other.offline) &&
            Objects.equals(split, other.split) &&
            Objects.equals(replicaId, other.replicaId);
    }

    @Override
    public int hashCode() {
        return Arrays.deepHashCode(new Object[] {
            regionId,
            tableName,
            startKey,
            endKey,
            offline,
            split,
            replicaId
        });
    }
}
