package com.github.cao.awa.kalmia.framework.serialize.bytes.type.primitive;

import com.github.cao.awa.apricot.io.bytes.reader.BytesReader;
import com.github.cao.awa.kalmia.annotation.auto.serializer.AutoBytesSerializer;
import com.github.cao.awa.kalmia.framework.serialize.bytes.BytesSerializer;

@AutoBytesSerializer(value = 0, target = {Boolean.class, boolean.class})
public class BytesBooleanSerializer implements BytesSerializer<Boolean> {
    @Override
    public byte[] serialize(Boolean b) {
        return new byte[]{(byte) (b ? 1 : 0)};
    }

    @Override
    public Boolean deserialize(BytesReader reader) {
        return reader.read() == 1;
    }
}
