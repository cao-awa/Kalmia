package com.github.cao.awa.kalmia.framework.serialize.type.raw;

import com.github.cao.awa.apricot.io.bytes.reader.BytesReader;
import com.github.cao.awa.kalmia.framework.serialize.serializer.BytesSerializer;
import com.github.cao.awa.kalmia.mathematic.base.SkippedBase256;

public class LongSerializer implements BytesSerializer<Long> {
    @Override
    public byte[] serialize(Long integer) throws Exception {
        return SkippedBase256.longToBuf(integer);
    }

    @Override
    public Long deserialize(BytesReader reader) throws Exception {
        return SkippedBase256.readLong(reader);
    }

    @Override
    public Long initializer() {
        return - 1L;
    }

    @Override
    public long id() {
        return 5;
    }
}
