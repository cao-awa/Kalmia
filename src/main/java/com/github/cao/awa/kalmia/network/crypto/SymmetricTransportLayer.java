package com.github.cao.awa.kalmia.network.crypto;

import com.github.cao.awa.kalmia.network.crypto.symmetric.SymmetricCrypto;
import com.github.cao.awa.kalmia.network.crypto.symmetric.aes.AesCrypto;
import com.github.cao.awa.kalmia.network.crypto.symmetric.no.NoCrypto;

public class SymmetricTransportLayer {
    private static final byte[] key = new byte[]{1,1,1,1,1,1,1,1,1,1,2,2,2,2,2,2};
    private final SymmetricCrypto crypto;

    public SymmetricTransportLayer(boolean init) {
        if (init) {
            this.crypto = new AesCrypto();
        } else {
            this.crypto = new NoCrypto();
        }
    }

    public byte[] encode(byte[] plain) {
        try {
            return crypto.encode(plain,
                                 key
            );
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public byte[] decode(byte[] plain) {
        try {
            return crypto.decode(plain,
                                 key
            );
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
