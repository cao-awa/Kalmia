package com.github.cao.awa.kalmia.framework.serialize.type.raw;

import com.github.cao.awa.apricot.io.bytes.reader.BytesReader;
import com.github.cao.awa.kalmia.annotation.auto.serializer.AutoSerializer;
import com.github.cao.awa.kalmia.framework.serialize.serializer.BytesSerializer;

import java.nio.ByteBuffer;

@AutoSerializer(value = 7, target = Double.class)
public class DoubleSerializer implements BytesSerializer<Double> {
    @Override
    public byte[] serialize(Double i) {
        return ByteBuffer.allocate(8)
                         .putDouble(i)
                         .array();
    }

    @Override
    public Double deserialize(BytesReader reader) {
        return ByteBuffer.wrap(reader.read(8))
                         .getDouble();
    }

    @Override
    public Double initializer() {
        return - 1D;
    }
}
