package com.github.cao.awa.kalmia.framework.serialize.type.primitive;

import com.github.cao.awa.apricot.io.bytes.reader.BytesReader;
import com.github.cao.awa.kalmia.annotation.auto.serializer.AutoSerializer;
import com.github.cao.awa.kalmia.framework.serialize.BytesSerializer;

import java.nio.ByteBuffer;

@AutoSerializer(value = 6, target = {Float.class, float.class})
public class FloatSerializer implements BytesSerializer<Float> {
    @Override
    public byte[] serialize(Float f) {
        return ByteBuffer.allocate(4)
                         .putFloat(f)
                         .array();
    }

    @Override
    public Float deserialize(BytesReader reader) {
        return ByteBuffer.wrap(reader.read(4))
                         .getFloat();
    }
}
