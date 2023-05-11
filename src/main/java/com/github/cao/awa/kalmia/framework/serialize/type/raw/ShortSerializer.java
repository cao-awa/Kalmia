package com.github.cao.awa.kalmia.framework.serialize.type.raw;

import com.github.cao.awa.apricot.io.bytes.reader.BytesReader;
import com.github.cao.awa.kalmia.framework.serialize.serializer.BytesSerializer;
import com.github.cao.awa.kalmia.mathematic.base.Base256;

public class ShortSerializer implements BytesSerializer<Short> {
    @Override
    public byte[] serialize(Short b) {
        return Base256.tagToBuf(b);
    }

    @Override
    public Short deserialize(BytesReader reader) {
        return (short) Base256.tagFromBuf(reader.read(2));
    }

    @Override
    public Short initializer() {
        return - 1;
    }

    public long id() {
        return 3;
    }
}
