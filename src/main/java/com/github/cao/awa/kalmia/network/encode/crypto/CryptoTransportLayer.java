package com.github.cao.awa.kalmia.network.encode.crypto;

import com.github.cao.awa.kalmia.network.encode.crypto.symmetric.SymmetricCrypto;
import com.github.cao.awa.kalmia.network.encode.crypto.symmetric.no.NoCrypto;

public class CryptoTransportLayer {
    private TransportLayerCrypto crypto;

    public CryptoTransportLayer() {
        this.crypto = new NoCrypto();
    }

    public byte[] encode(byte[] source) {
        try {
            return this.crypto.encode(source);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public byte[] decode(byte[] source) {
        try {
            return this.crypto.decode(source);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean isCipherEquals(byte[] cipher) {
        if (this.crypto instanceof SymmetricCrypto symmetric) {
            return symmetric.isCipherEquals(cipher);
        }
        return false;
    }

    public void setCrypto(TransportLayerCrypto crypto) {
        this.crypto = crypto;
    }

    public TransportLayerCrypto crypto() {
        return this.crypto;
    }

    public void setCipher(byte[] cipher) {
        if (this.crypto instanceof SymmetricCrypto symmetric) {
            symmetric.cipher(cipher);
        }
    }

    public void setIv(byte[] iv) {
        if (this.crypto instanceof SymmetricCrypto symmetric) {
            symmetric.iv(iv);
        }
    }
}
