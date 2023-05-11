package com.github.cao.awa.kalmia.framework.serialize.type.raw;

import com.github.cao.awa.apricot.io.bytes.reader.BytesReader;
import com.github.cao.awa.kalmia.framework.serialize.serializer.BytesSerializer;
import com.github.cao.awa.kalmia.mathematic.base.SkippedBase256;
import com.github.cao.awa.viburnum.util.bytes.BytesUtil;

import java.math.BigInteger;

public class BigIntegerSerializer implements BytesSerializer<BigInteger> {
    @Override
    public byte[] serialize(BigInteger integer) {
        byte[] data = integer.toByteArray();

        return BytesUtil.concat(SkippedBase256.intToBuf(data.length),
                                data
        );
    }

    @Override
    public BigInteger deserialize(BytesReader reader) {
        return new BigInteger(reader.read(SkippedBase256.readInt(reader)));
    }

    @Override
    public BigInteger initializer() {
        return BigInteger.valueOf(- 1L);
    }

    @Override
    public long id() {
        return 100;
    }
}
