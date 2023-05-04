package com.github.cao.awa.kalmia.network.encode.crypto;

public abstract class LayerCrypto {
    public abstract byte[] encode(byte[] plains) throws Exception;

    public abstract byte[] decode(byte[] ciphertext) throws Exception;
}
