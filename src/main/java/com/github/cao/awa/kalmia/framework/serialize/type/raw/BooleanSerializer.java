package com.github.cao.awa.kalmia.framework.serialize.type.raw;

import com.github.cao.awa.apricot.io.bytes.reader.BytesReader;
import com.github.cao.awa.kalmia.annotation.auto.serializer.AutoSerializer;
import com.github.cao.awa.kalmia.framework.serialize.serializer.BytesSerializer;

@AutoSerializer(value = 0, target = Boolean.class)
public class BooleanSerializer implements BytesSerializer<Boolean> {
    @Override
    public byte[] serialize(Boolean b) {
        return new byte[]{(byte) (b ? 1 : 0)};
    }

    @Override
    public Boolean deserialize(BytesReader reader) {
        return reader.read() == 1;
    }

    @Override
    public Boolean initializer() {
        return false;
    }
}