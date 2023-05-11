package com.github.cao.awa.kalmia.framework.serialize.type.raw;

import com.github.cao.awa.apricot.io.bytes.reader.BytesReader;
import com.github.cao.awa.kalmia.framework.serialize.serializer.BytesSerializer;

import java.nio.ByteBuffer;
import java.util.Arrays;

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

    @Override
    public long id() {
        return 7;
    }

    public static void main(String[] args) {
        FloatSerializer serializer = new FloatSerializer();
        System.out.println(Float.MAX_VALUE);
        byte[] result = serializer.serialize(114.51419198100004F);
        System.out.println(Arrays.toString(result));
        System.out.println(serializer.deserialize(new BytesReader(result)));
    }
}
