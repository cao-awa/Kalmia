package com.github.cao.awa.kalmia.framework.serialize.bytes.type.primitive;

import com.github.cao.awa.apricot.io.bytes.reader.BytesReader;
import com.github.cao.awa.kalmia.annotation.auto.serializer.AutoBytesSerializer;
import com.github.cao.awa.kalmia.framework.serialize.bytes.BytesSerializer;

@AutoBytesSerializer(value = 1, target = {Byte.class, byte.class})
public class BytesByteSerializer implements BytesSerializer<Byte> {
    @Override
    public byte[] serialize(Byte b) {
        return new byte[]{b};
    }

    @Override
    public Byte deserialize(BytesReader reader) {
        return reader.read();
    }
}
