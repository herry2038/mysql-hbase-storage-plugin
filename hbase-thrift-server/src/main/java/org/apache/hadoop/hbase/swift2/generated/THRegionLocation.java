package org.apache.hadoop.hbase.swift2.generated;

import com.facebook.swift.codec.*;
import com.facebook.swift.codec.ThriftField.Requiredness;
import com.facebook.swift.codec.ThriftField.Recursiveness;
import java.util.*;

import static com.google.common.base.Objects.toStringHelper;

@ThriftStruct("THRegionLocation")
public final class THRegionLocation
{
    @ThriftConstructor
    public THRegionLocation(
        @ThriftField(value=1, name="serverName", requiredness=Requiredness.REQUIRED) final TServerName serverName,
        @ThriftField(value=2, name="regionInfo", requiredness=Requiredness.REQUIRED) final THRegionInfo regionInfo
    ) {
        this.serverName = serverName;
        this.regionInfo = regionInfo;
    }

    public static class Builder {
        private TServerName serverName;

        public Builder setServerName(TServerName serverName) {
            this.serverName = serverName;
            return this;
        }
        private THRegionInfo regionInfo;

        public Builder setRegionInfo(THRegionInfo regionInfo) {
            this.regionInfo = regionInfo;
            return this;
        }

        public Builder() { }
        public Builder(THRegionLocation other) {
            this.serverName = other.serverName;
            this.regionInfo = other.regionInfo;
        }

        public THRegionLocation build() {
            return new THRegionLocation (
                this.serverName,
                this.regionInfo
            );
        }
    }

    private final TServerName serverName;

    @ThriftField(value=1, name="serverName", requiredness=Requiredness.REQUIRED)
    public TServerName getServerName() { return serverName; }

    private final THRegionInfo regionInfo;

    @ThriftField(value=2, name="regionInfo", requiredness=Requiredness.REQUIRED)
    public THRegionInfo getRegionInfo() { return regionInfo; }

    @Override
    public String toString()
    {
        return toStringHelper(this)
            .add("serverName", serverName)
            .add("regionInfo", regionInfo)
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

        THRegionLocation other = (THRegionLocation)o;

        return
            Objects.equals(serverName, other.serverName) &&
            Objects.equals(regionInfo, other.regionInfo);
    }

    @Override
    public int hashCode() {
        return Arrays.deepHashCode(new Object[] {
            serverName,
            regionInfo
        });
    }
}
