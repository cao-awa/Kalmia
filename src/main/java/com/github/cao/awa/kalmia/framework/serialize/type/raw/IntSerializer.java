package com.github.cao.awa.kalmia.framework.serialize.type.raw;

import com.github.cao.awa.apricot.io.bytes.reader.BytesReader;
import com.github.cao.awa.kalmia.framework.serialize.serializer.BytesSerializer;
import com.github.cao.awa.kalmia.mathematic.base.SkippedBase256;

public class IntSerializer implements BytesSerializer<Integer> {
    @Override
    public byte[] serialize(Integer integer) throws Exception {
        return SkippedBase256.intToBuf(integer);
    }

    @Override
    public Integer deserialize(BytesReader reader) throws Exception {
        return SkippedBase256.readInt(reader);
    }

    @Override
    public Integer initializer() {
        return - 1;
    }

    @Override
    public long id() {
        return 4;
    }
}
