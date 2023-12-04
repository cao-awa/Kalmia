package com.github.cao.awa.kalmia.framework.serialize.bytes.type.primitive;

import com.github.cao.awa.apricot.io.bytes.reader.BytesReader;
import com.github.cao.awa.kalmia.annotations.auto.serializer.AutoBytesSerializer;
import com.github.cao.awa.kalmia.framework.serialize.bytes.BytesSerializer;

import java.nio.ByteBuffer;

@AutoBytesSerializer(value = 6, target = {Float.class, float.class})
public class BytesFloatSerializer implements BytesSerializer<Float> {
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
