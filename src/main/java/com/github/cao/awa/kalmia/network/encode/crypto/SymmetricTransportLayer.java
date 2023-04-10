package com.github.cao.awa.kalmia.network.encode.crypto;

import com.github.cao.awa.kalmia.network.encode.crypto.symmetric.SymmetricCrypto;
import com.github.cao.awa.kalmia.network.encode.crypto.symmetric.no.NoCrypto;

public class SymmetricTransportLayer {
    private SymmetricCrypto crypto;

    public SymmetricTransportLayer() {
        this.crypto = new NoCrypto();
    }

    public byte[] encode(byte[] plain) {
        try {
            return crypto.encode(plain);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public byte[] decode(byte[] plain) {
        try {
            return crypto.decode(plain);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean isCipherEquals(byte[] cipher) {
        return this.crypto.isCipherEquals(cipher);
    }

    public void setCrypto(SymmetricCrypto crypto) {
        this.crypto = crypto;
    }
}
