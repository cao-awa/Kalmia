package com.github.cao.awa.kalmia.convert;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;

public abstract class BytesValueConvertable {
    public abstract byte[] value();

    public String value16() {
        return new BigInteger(value()).toString(16);
    }

    public String value36() {
        return new BigInteger(value()).toString(36);
    }

    public String value256() {
        return new String(value(), StandardCharsets.ISO_8859_1);
    }
}
