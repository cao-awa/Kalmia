package com.github.cao.awa.kalmia.framework.serialize.type.raw;

import com.github.cao.awa.apricot.io.bytes.reader.BytesReader;
import com.github.cao.awa.kalmia.framework.serialize.serializer.BytesSerializer;

import java.nio.ByteBuffer;

public class FloatSerializer implements BytesSerializer<Float> {
    @Override
    public byte[] serialize(Float i) {
        return ByteBuffer.allocate(4)
                         .putFloat(i)
                         .array();
    }

    @Override
    public Float deserialize(BytesReader reader) {
        return ByteBuffer.wrap(reader.read(4))
                         .getFloat();
    }

    @Override
    public Float initializer() {
        return - 1F;
    }

    @Override
    public long id() {
        return 6;
    }
}
