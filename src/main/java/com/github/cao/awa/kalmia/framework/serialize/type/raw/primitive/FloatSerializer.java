package com.github.cao.awa.kalmia.framework.serialize.type.raw.primitive;

import com.github.cao.awa.apricot.io.bytes.reader.BytesReader;
import com.github.cao.awa.kalmia.annotation.auto.serializer.AutoSerializer;
import com.github.cao.awa.kalmia.framework.serialize.serializer.BytesSerializer;

import java.nio.ByteBuffer;

@AutoSerializer(value = 6, target = {Float.class, float.class})
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
}
