package com.github.cao.awa.kalmia.framework.serialize.bytes.type.primitive;

import com.github.cao.awa.apricot.io.bytes.reader.BytesReader;
import com.github.cao.awa.kalmia.annotations.auto.serializer.AutoBytesSerializer;
import com.github.cao.awa.kalmia.framework.serialize.bytes.BytesSerializer;

import java.nio.ByteBuffer;

@AutoBytesSerializer(value = 7, target = {Double.class, double.class})
public class BytesDoubleSerializer implements BytesSerializer<Double> {
    @Override
    public byte[] serialize(Double d) {
        return ByteBuffer.allocate(8)
                         .putDouble(d)
                         .array();
    }

    @Override
    public Double deserialize(BytesReader reader) {
        return ByteBuffer.wrap(reader.read(8))
                         .getDouble();
    }
}
