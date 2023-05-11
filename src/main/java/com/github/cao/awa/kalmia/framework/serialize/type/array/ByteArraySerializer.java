package com.github.cao.awa.kalmia.framework.serialize.type.array;

import com.github.cao.awa.apricot.io.bytes.reader.BytesReader;
import com.github.cao.awa.kalmia.framework.serialize.serializer.BytesSerializer;
import com.github.cao.awa.kalmia.mathematic.base.SkippedBase256;
import com.github.cao.awa.viburnum.util.bytes.BytesUtil;

public class ByteArraySerializer implements BytesSerializer<byte[]> {
    @Override
    public byte[] serialize(byte[] bytes) throws Exception {
        return BytesUtil.concat(SkippedBase256.intToBuf(bytes.length),
                                bytes
        );
    }

    @Override
    public byte[] deserialize(BytesReader reader) throws Exception {
        return reader.read(SkippedBase256.readInt(reader));
    }

    @Override
    public byte[] initializer() {
        return new byte[0];
    }

    @Override
    public long id() {
        return 201;
    }
}
