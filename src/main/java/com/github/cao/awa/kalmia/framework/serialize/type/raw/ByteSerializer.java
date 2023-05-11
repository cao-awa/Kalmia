package com.github.cao.awa.kalmia.framework.serialize.type.raw;

import com.github.cao.awa.apricot.io.bytes.reader.BytesReader;
import com.github.cao.awa.kalmia.framework.serialize.serializer.BytesSerializer;

public class ByteSerializer implements BytesSerializer<Byte> {
    @Override
    public byte[] serialize(Byte b) {
        return new byte[]{b};
    }

    @Override
    public Byte deserialize(BytesReader reader) {
        return reader.read();
    }

    @Override
    public Byte initializer() {
        return - 1;
    }

    @Override
    public long id() {
        return 1;
    }
}
