package com.github.cao.awa.kalmia.message.digest;

import com.github.cao.awa.apricot.io.bytes.reader.BytesReader;
import com.github.cao.awa.kalmia.convert.ByteArrayConvertable;
import com.github.cao.awa.kalmia.convert.BytesValueConvertable;
import com.github.cao.awa.kalmia.mathematic.base.Base256;
import com.github.cao.awa.viburnum.util.bytes.BytesUtil;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;

public class MessageDigestData extends BytesValueConvertable implements ByteArrayConvertable {
    private final String type;
    private final byte[] value;

    public String type() {
        return this.type;
    }

    public byte[] value() {
        return this.value;
    }

    public String value10() {
        return new BigInteger(this.value).toString(10);
    }

    public MessageDigestData(String type, byte[] value) {
        this.type = type;
        this.value = value;
    }

    @Override
    public byte[] toBytes() {
        return BytesUtil.concat(new byte[]{(byte) type().length()},
                                type().getBytes(StandardCharsets.US_ASCII),
                                Base256.tagToBuf(value().length),
                                value()
        );
    }

    public static MessageDigestData create(BytesReader reader) {
        String type = new String(reader.read(reader.read()),
                                 StandardCharsets.US_ASCII
        );

        byte[] value = reader.read(Base256.tagFromBuf(reader.read(2)));

        return new MessageDigestData(type,
                                     value
        );
    }
}
