package com.github.cao.awa.kalmia.network.encode.crypto.symmetric;

public abstract class SymmetricCrypto {
    private final byte[] cipher;

    protected SymmetricCrypto(byte[] cipher) {
        this.cipher = cipher;
    }

    public byte[] cipher() {
        return this.cipher;
    }

    public abstract byte[] encode(byte[] plains) throws Exception;
    public abstract byte[] decode(byte[] ciphertext) throws Exception;
}
