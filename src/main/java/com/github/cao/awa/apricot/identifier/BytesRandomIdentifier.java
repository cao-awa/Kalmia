package com.github.cao.awa.apricot.identifier;

import java.util.Random;

public class BytesRandomIdentifier {
    private static final Random RANDOM = new Random();

    public static byte[] create() {
        return create(1024);
    }

    public static byte[] create(int size) {
        byte[] bytes = new byte[size];
        RANDOM.nextBytes(bytes);
        return bytes;
    }
}
