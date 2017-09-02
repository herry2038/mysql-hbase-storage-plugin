package org.apache.hadoop.hbase.swift2.generated;

import com.facebook.swift.codec.*;
import com.facebook.swift.codec.ThriftField.Requiredness;
import com.facebook.swift.codec.ThriftField.Recursiveness;
import java.util.*;

import static com.google.common.base.Objects.toStringHelper;

@ThriftUnion("TMutation")
public final class TMutation
{
    private final Object value;
    private final int id;
    private final String name;

    @ThriftConstructor
    public TMutation(final TPut put) {
        this.value = put;
        this.id = 1;
        this.name = "put";
    }

    @ThriftConstructor
    public TMutation(final TDelete deleteSingle) {
        this.value = deleteSingle;
        this.id = 2;
        this.name = "deleteSingle";
    }

    @ThriftField(value=1, name="put", requiredness=Requiredness.NONE)
    public TPut getPut() {
        if (this.id != 1) {
            throw new IllegalStateException("Not a put element!");
        }
        return (TPut) value;
    }

    public boolean isSetPut() {
        return this.id == 1;
    }

    @ThriftField(value=2, name="deleteSingle", requiredness=Requiredness.NONE)
    public TDelete getDeleteSingle() {
        if (this.id != 2) {
            throw new IllegalStateException("Not a deleteSingle element!");
        }
        return (TDelete) value;
    }

    public boolean isSetDeleteSingle() {
        return this.id == 2;
    }

    @ThriftUnionId
    public int getThriftId()
    {
        return this.id;
    }

    public String getThriftName()
    {
        return this.name;
    }

    @Override
    public String toString()
    {
        return toStringHelper(this)
            .add("value", value)
            .add("id", id)
            .add("name", name)
            .add("type", value == null ? "<null>" : value.getClass().getSimpleName())
            .toString();
    }
}
