package com.github.cao.awa.kalmia.message.digest;

import com.github.cao.awa.kalmia.convert.BytesValueConvertable;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;

public class MessageDigest extends BytesValueConvertable {
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

    public MessageDigest(String type, byte[] value) {
        this.type = type;
        this.value = value;
    }
}
