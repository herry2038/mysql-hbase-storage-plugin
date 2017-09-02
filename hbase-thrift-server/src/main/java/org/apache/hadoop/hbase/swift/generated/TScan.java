package org.apache.hadoop.hbase.swift.generated;

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
        @ThriftField(value=3, name="timestamp", requiredness=Requiredness.OPTIONAL) final Long timestamp,
        @ThriftField(value=4, name="columns", requiredness=Requiredness.OPTIONAL) final List<byte[]> columns,
        @ThriftField(value=5, name="caching", requiredness=Requiredness.OPTIONAL) final Integer caching,
        @ThriftField(value=6, name="filterString", requiredness=Requiredness.OPTIONAL) final byte[] filterString,
        @ThriftField(value=7, name="batchSize", requiredness=Requiredness.OPTIONAL) final Integer batchSize,
        @ThriftField(value=8, name="sortColumns", requiredness=Requiredness.OPTIONAL) final Boolean sortColumns,
        @ThriftField(value=9, name="reversed", requiredness=Requiredness.OPTIONAL) final Boolean reversed
    ) {
        this.startRow = startRow;
        this.stopRow = stopRow;
        this.timestamp = timestamp;
        this.columns = columns;
        this.caching = caching;
        this.filterString = filterString;
        this.batchSize = batchSize;
        this.sortColumns = sortColumns;
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
        private Long timestamp;

        public Builder setTimestamp(Long timestamp) {
            this.timestamp = timestamp;
            return this;
        }
        private List<byte[]> columns;

        public Builder setColumns(List<byte[]> columns) {
            this.columns = columns;
            return this;
        }
        private Integer caching;

        public Builder setCaching(Integer caching) {
            this.caching = caching;
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
        private Boolean sortColumns;

        public Builder setSortColumns(Boolean sortColumns) {
            this.sortColumns = sortColumns;
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
            this.timestamp = other.timestamp;
            this.columns = other.columns;
            this.caching = other.caching;
            this.filterString = other.filterString;
            this.batchSize = other.batchSize;
            this.sortColumns = other.sortColumns;
            this.reversed = other.reversed;
        }

        public TScan build() {
            return new TScan (
                this.startRow,
                this.stopRow,
                this.timestamp,
                this.columns,
                this.caching,
                this.filterString,
                this.batchSize,
                this.sortColumns,
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

    private final Long timestamp;

    @ThriftField(value=3, name="timestamp", requiredness=Requiredness.OPTIONAL)
    public Long getTimestamp() { return timestamp; }

    private final List<byte[]> columns;

    @ThriftField(value=4, name="columns", requiredness=Requiredness.OPTIONAL)
    public List<byte[]> getColumns() { return columns; }

    private final Integer caching;

    @ThriftField(value=5, name="caching", requiredness=Requiredness.OPTIONAL)
    public Integer getCaching() { return caching; }

    private final byte[] filterString;

    @ThriftField(value=6, name="filterString", requiredness=Requiredness.OPTIONAL)
    public byte[] getFilterString() { return filterString; }

    private final Integer batchSize;

    @ThriftField(value=7, name="batchSize", requiredness=Requiredness.OPTIONAL)
    public Integer getBatchSize() { return batchSize; }

    private final Boolean sortColumns;

    @ThriftField(value=8, name="sortColumns", requiredness=Requiredness.OPTIONAL)
    public Boolean isSortColumns() { return sortColumns; }

    private final Boolean reversed;

    @ThriftField(value=9, name="reversed", requiredness=Requiredness.OPTIONAL)
    public Boolean isReversed() { return reversed; }

    @Override
    public String toString()
    {
        return toStringHelper(this)
            .add("startRow", startRow)
            .add("stopRow", stopRow)
            .add("timestamp", timestamp)
            .add("columns", columns)
            .add("caching", caching)
            .add("filterString", filterString)
            .add("batchSize", batchSize)
            .add("sortColumns", sortColumns)
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
            Objects.equals(timestamp, other.timestamp) &&
            Objects.equals(columns, other.columns) &&
            Objects.equals(caching, other.caching) &&
            Arrays.equals(filterString, other.filterString) &&
            Objects.equals(batchSize, other.batchSize) &&
            Objects.equals(sortColumns, other.sortColumns) &&
            Objects.equals(reversed, other.reversed);
    }

    @Override
    public int hashCode() {
        return Arrays.deepHashCode(new Object[] {
            startRow,
            stopRow,
            timestamp,
            columns,
            caching,
            filterString,
            batchSize,
            sortColumns,
            reversed
        });
    }
}
