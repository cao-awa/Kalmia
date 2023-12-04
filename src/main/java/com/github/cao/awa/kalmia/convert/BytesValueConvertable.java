package com.github.cao.awa.kalmia.convert;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;

public interface BytesValueConvertable {
    byte[] bytes();

    default String value16() {
        return new BigInteger(bytes()).toString(16);
    }

    default String value36() {
        return new BigInteger(bytes()).toString(36);
    }

    default String value256() {
        return new String(bytes(),
                          StandardCharsets.ISO_8859_1
        );
    }
}
