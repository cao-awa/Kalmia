package com.github.cao.awa.kalmia.framework.serialize.type.raw;

import com.github.cao.awa.apricot.io.bytes.reader.BytesReader;
import com.github.cao.awa.kalmia.framework.serialize.serializer.BytesSerializer;
import com.github.cao.awa.kalmia.mathematic.base.SkippedBase256;
import com.github.cao.awa.viburnum.util.bytes.BytesUtil;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;

public class BigDecimalSerializer implements BytesSerializer<BigDecimal> {
    @Override
    public byte[] serialize(BigDecimal decimal) {
        String str = decimal.toString();
        return BytesUtil.concat(SkippedBase256.intToBuf(str.length()),
                                decimal.toString()
                                       .getBytes(StandardCharsets.US_ASCII)
        );
    }

    @Override
    public BigDecimal deserialize(BytesReader reader) {
        return new BigDecimal(new String(reader.read(SkippedBase256.readInt(reader)),
                                         StandardCharsets.US_ASCII
        ));
    }

    @Override
    public BigDecimal initializer() {
        return BigDecimal.valueOf(- 1L);
    }

    @Override
    public long id() {
        return 101;
    }
}
