package org.apache.hadoop.hbase.swift2.generated;

import com.facebook.swift.codec.*;
import com.facebook.swift.codec.ThriftField.Requiredness;
import com.facebook.swift.codec.ThriftField.Recursiveness;
import java.util.*;

import static com.google.common.base.Objects.toStringHelper;

@ThriftStruct("TScan")
public final class TScan
{
    @ThriftConstructor
    public TScan(
        @ThriftField(value=1, name="startRow", requiredness=Requiredness.OPTIONAL) final byte[] startRow,
        @ThriftField(value=2, name="stopRow", requiredness=Requiredness.OPTIONAL) final byte[] stopRow,
        @ThriftField(value=3, name="columns", requiredness=Requiredness.OPTIONAL) final List<TColumn> columns,
        @ThriftField(value=4, name="caching", requiredness=Requiredness.OPTIONAL) final Integer caching,
        @ThriftField(value=5, name="maxVersions", requiredness=Requiredness.OPTIONAL) final Integer maxVersions,
        @ThriftField(value=6, name="timeRange", requiredness=Requiredness.OPTIONAL) final TTimeRange timeRange,
        @ThriftField(value=7, name="filterString", requiredness=Requiredness.OPTIONAL) final byte[] filterString,
        @ThriftField(value=8, name="batchSize", requiredness=Requiredness.OPTIONAL) final Integer batchSize,
        @ThriftField(value=9, name="attributes", requiredness=Requiredness.OPTIONAL) final Map<byte[], byte[]> attributes,
        @ThriftField(value=10, name="authorizations", requiredness=Requiredness.OPTIONAL) final TAuthorization authorizations,
        @ThriftField(value=11, name="reversed", requiredness=Requiredness.OPTIONAL) final Boolean reversed
    ) {
        this.startRow = startRow;
        this.stopRow = stopRow;
        this.columns = columns;
        this.caching = caching;
        this.maxVersions = maxVersions;
        this.timeRange = timeRange;
        this.filterString = filterString;
        this.batchSize = batchSize;
        this.attributes = attributes;
        this.authorizations = authorizations;
        this.reversed = reversed;
    }

    public static class Builder {
        private byte[] startRow;

        public Builder setStartRow(byte[] startRow) {
            this.startRow = startRow;
            return this;
        }
        private byte[] stopRow;

        public Builder setStopRow(byte[] stopRow) {
            this.stopRow = stopRow;
            return this;
        }
        private List<TColumn> columns;

        public Builder setColumns(List<TColumn> columns) {
            this.columns = columns;
            return this;
        }
        private Integer caching;

        public Builder setCaching(Integer caching) {
            this.caching = caching;
            return this;
        }
        private Integer maxVersions;

        public Builder setMaxVersions(Integer maxVersions) {
            this.maxVersions = maxVersions;
            return this;
        }
        private TTimeRange timeRange;

        public Builder setTimeRange(TTimeRange timeRange) {
            this.timeRange = timeRange;
            return this;
        }
        private byte[] filterString;

        public Builder setFilterString(byte[] filterString) {
            this.filterString = filterString;
            return this;
        }
        private Integer batchSize;

        public Builder setBatchSize(Integer batchSize) {
            this.batchSize = batchSize;
            return this;
        }
        private Map<byte[], byte[]> attributes;

        public Builder setAttributes(Map<byte[], byte[]> attributes) {
            this.attributes = attributes;
            return this;
        }
        private TAuthorization authorizations;

        public Builder setAuthorizations(TAuthorization authorizations) {
            this.authorizations = authorizations;
            return this;
        }
        private Boolean reversed;

        public Builder setReversed(Boolean reversed) {
            this.reversed = reversed;
            return this;
        }

        public Builder() { }
        public Builder(TScan other) {
            this.startRow = other.startRow;
            this.stopRow = other.stopRow;
            this.columns = other.columns;
            this.caching = other.caching;
            this.maxVersions = other.maxVersions;
            this.timeRange = other.timeRange;
            this.filterString = other.filterString;
            this.batchSize = other.batchSize;
            this.attributes = other.attributes;
            this.authorizations = other.authorizations;
            this.reversed = other.reversed;
        }

        public TScan build() {
            return new TScan (
                this.startRow,
                this.stopRow,
                this.columns,
                this.caching,
                this.maxVersions,
                this.timeRange,
                this.filterString,
                this.batchSize,
                this.attributes,
                this.authorizations,
                this.reversed
            );
        }
    }

    private final byte[] startRow;

    @ThriftField(value=1, name="startRow", requiredness=Requiredness.OPTIONAL)
    public byte[] getStartRow() { return startRow; }

    private final byte[] stopRow;

    @ThriftField(value=2, name="stopRow", requiredness=Requiredness.OPTIONAL)
    public byte[] getStopRow() { return stopRow; }

    private final List<TColumn> columns;

    @ThriftField(value=3, name="columns", requiredness=Requiredness.OPTIONAL)
    public List<TColumn> getColumns() { return columns; }

    private final Integer caching;

    @ThriftField(value=4, name="caching", requiredness=Requiredness.OPTIONAL)
    public Integer getCaching() { return caching; }

    private final Integer maxVersions;

    @ThriftField(value=5, name="maxVersions", requiredness=Requiredness.OPTIONAL)
    public Integer getMaxVersions() { return maxVersions; }

    private final TTimeRange timeRange;

    @ThriftField(value=6, name="timeRange", requiredness=Requiredness.OPTIONAL)
    public TTimeRange getTimeRange() { return timeRange; }

    private final byte[] filterString;

    @ThriftField(value=7, name="filterString", requiredness=Requiredness.OPTIONAL)
    public byte[] getFilterString() { return filterString; }

    private final Integer batchSize;

    @ThriftField(value=8, name="batchSize", requiredness=Requiredness.OPTIONAL)
    public Integer getBatchSize() { return batchSize; }

    private final Map<byte[], byte[]> attributes;

    @ThriftField(value=9, name="attributes", requiredness=Requiredness.OPTIONAL)
    public Map<byte[], byte[]> getAttributes() { return attributes; }

    private final TAuthorization authorizations;

    @ThriftField(value=10, name="authorizations", requiredness=Requiredness.OPTIONAL)
    public TAuthorization getAuthorizations() { return authorizations; }

    private final Boolean reversed;

    @ThriftField(value=11, name="reversed", requiredness=Requiredness.OPTIONAL)
    public Boolean isReversed() { return reversed; }

    @Override
    public String toString()
    {
        return toStringHelper(this)
            .add("startRow", startRow)
            .add("stopRow", stopRow)
            .add("columns", columns)
            .add("caching", caching)
            .add("maxVersions", maxVersions)
            .add("timeRange", timeRange)
            .add("filterString", filterString)
            .add("batchSize", batchSize)
            .add("attributes", attributes)
            .add("authorizations", authorizations)
            .add("reversed", reversed)
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

        TScan other = (TScan)o;

        return
            Arrays.equals(startRow, other.startRow) &&
            Arrays.equals(stopRow, other.stopRow) &&
            Objects.equals(columns, other.columns) &&
            Objects.equals(caching, other.caching) &&
            Objects.equals(maxVersions, other.maxVersions) &&
            Objects.equals(timeRange, other.timeRange) &&
            Arrays.equals(filterString, other.filterString) &&
            Objects.equals(batchSize, other.batchSize) &&
            Objects.equals(attributes, other.attributes) &&
            Objects.equals(authorizations, other.authorizations) &&
            Objects.equals(reversed, other.reversed);
    }

    @Override
    public int hashCode() {
        return Arrays.deepHashCode(new Object[] {
            startRow,
            stopRow,
            columns,
            caching,
            maxVersions,
            timeRange,
            filterString,
            batchSize,
            attributes,
            authorizations,
            reversed
        });
    }
}
