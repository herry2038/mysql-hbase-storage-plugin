package org.apache.hadoop.hbase.swift2.generated;

import com.facebook.swift.codec.*;
import com.facebook.swift.codec.ThriftField.Requiredness;
import com.facebook.swift.codec.ThriftField.Recursiveness;
import java.util.*;

import static com.google.common.base.Objects.toStringHelper;

@ThriftStruct("TGet")
public final class TGet
{
    @ThriftConstructor
    public TGet(
        @ThriftField(value=1, name="row", requiredness=Requiredness.REQUIRED) final byte[] row,
        @ThriftField(value=2, name="columns", requiredness=Requiredness.OPTIONAL) final List<TColumn> columns,
        @ThriftField(value=3, name="timestamp", requiredness=Requiredness.OPTIONAL) final Long timestamp,
        @ThriftField(value=4, name="timeRange", requiredness=Requiredness.OPTIONAL) final TTimeRange timeRange,
        @ThriftField(value=5, name="maxVersions", requiredness=Requiredness.OPTIONAL) final Integer maxVersions,
        @ThriftField(value=6, name="filterString", requiredness=Requiredness.OPTIONAL) final byte[] filterString,
        @ThriftField(value=7, name="attributes", requiredness=Requiredness.OPTIONAL) final Map<byte[], byte[]> attributes,
        @ThriftField(value=8, name="authorizations", requiredness=Requiredness.OPTIONAL) final TAuthorization authorizations
    ) {
        this.row = row;
        this.columns = columns;
        this.timestamp = timestamp;
        this.timeRange = timeRange;
        this.maxVersions = maxVersions;
        this.filterString = filterString;
        this.attributes = attributes;
        this.authorizations = authorizations;
    }

    public static class Builder {
        private byte[] row;

        public Builder setRow(byte[] row) {
            this.row = row;
            return this;
        }
        private List<TColumn> columns;

        public Builder setColumns(List<TColumn> columns) {
            this.columns = columns;
            return this;
        }
        private Long timestamp;

        public Builder setTimestamp(Long timestamp) {
            this.timestamp = timestamp;
            return this;
        }
        private TTimeRange timeRange;

        public Builder setTimeRange(TTimeRange timeRange) {
            this.timeRange = timeRange;
            return this;
        }
        private Integer maxVersions;

        public Builder setMaxVersions(Integer maxVersions) {
            this.maxVersions = maxVersions;
            return this;
        }
        private byte[] filterString;

        public Builder setFilterString(byte[] filterString) {
            this.filterString = filterString;
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

        public Builder() { }
        public Builder(TGet other) {
            this.row = other.row;
            this.columns = other.columns;
            this.timestamp = other.timestamp;
            this.timeRange = other.timeRange;
            this.maxVersions = other.maxVersions;
            this.filterString = other.filterString;
            this.attributes = other.attributes;
            this.authorizations = other.authorizations;
        }

        public TGet build() {
            return new TGet (
                this.row,
                this.columns,
                this.timestamp,
                this.timeRange,
                this.maxVersions,
                this.filterString,
                this.attributes,
                this.authorizations
            );
        }
    }

    private final byte[] row;

    @ThriftField(value=1, name="row", requiredness=Requiredness.REQUIRED)
    public byte[] getRow() { return row; }

    private final List<TColumn> columns;

    @ThriftField(value=2, name="columns", requiredness=Requiredness.OPTIONAL)
    public List<TColumn> getColumns() { return columns; }

    private final Long timestamp;

    @ThriftField(value=3, name="timestamp", requiredness=Requiredness.OPTIONAL)
    public Long getTimestamp() { return timestamp; }

    private final TTimeRange timeRange;

    @ThriftField(value=4, name="timeRange", requiredness=Requiredness.OPTIONAL)
    public TTimeRange getTimeRange() { return timeRange; }

    private final Integer maxVersions;

    @ThriftField(value=5, name="maxVersions", requiredness=Requiredness.OPTIONAL)
    public Integer getMaxVersions() { return maxVersions; }

    private final byte[] filterString;

    @ThriftField(value=6, name="filterString", requiredness=Requiredness.OPTIONAL)
    public byte[] getFilterString() { return filterString; }

    private final Map<byte[], byte[]> attributes;

    @ThriftField(value=7, name="attributes", requiredness=Requiredness.OPTIONAL)
    public Map<byte[], byte[]> getAttributes() { return attributes; }

    private final TAuthorization authorizations;

    @ThriftField(value=8, name="authorizations", requiredness=Requiredness.OPTIONAL)
    public TAuthorization getAuthorizations() { return authorizations; }

    @Override
    public String toString()
    {
        return toStringHelper(this)
            .add("row", row)
            .add("columns", columns)
            .add("timestamp", timestamp)
            .add("timeRange", timeRange)
            .add("maxVersions", maxVersions)
            .add("filterString", filterString)
            .add("attributes", attributes)
            .add("authorizations", authorizations)
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

        TGet other = (TGet)o;

        return
            Arrays.equals(row, other.row) &&
            Objects.equals(columns, other.columns) &&
            Objects.equals(timestamp, other.timestamp) &&
            Objects.equals(timeRange, other.timeRange) &&
            Objects.equals(maxVersions, other.maxVersions) &&
            Arrays.equals(filterString, other.filterString) &&
            Objects.equals(attributes, other.attributes) &&
            Objects.equals(authorizations, other.authorizations);
    }

    @Override
    public int hashCode() {
        return Arrays.deepHashCode(new Object[] {
            row,
            columns,
            timestamp,
            timeRange,
            maxVersions,
            filterString,
            attributes,
            authorizations
        });
    }
}
