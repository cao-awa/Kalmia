package com.github.cao.awa.kalmia.network.crypto.symmetric;

public abstract class SymmetricCrypto {
    public abstract byte[] encode(byte[] plains, byte[] cipher) throws Exception;
    public abstract byte[] decode(byte[] ciphertext, byte[] cipher) throws Exception;
}
