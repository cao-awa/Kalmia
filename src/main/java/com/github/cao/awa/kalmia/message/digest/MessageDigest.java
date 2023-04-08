package com.github.cao.awa.kalmia.message.digest;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;

public class MessageDigest {
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

    public String value16() {
        return new BigInteger(this.value).toString(16);
    }

    public String value36() {
        return new BigInteger(this.value).toString(36);
    }

    public String value256() {
        return new String(this.value, StandardCharsets.ISO_8859_1);
    }

    public MessageDigest(String type, byte[] value) {
        this.type = type;
        this.value = value;
    }
}
