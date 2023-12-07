package com.github.cao.awa.kalmia.keypair;

import com.github.cao.awa.apricot.util.encryption.Crypto;
import com.github.cao.awa.kalmia.keypair.pair.ec.EcKeyPair;
import com.github.cao.awa.kalmia.keypair.pair.rsa.RsaKeyPair;
import com.github.cao.awa.kalmia.keypair.store.KeyPairStore;
import com.github.cao.awa.kalmia.mathematic.base.Base256;
import com.github.cao.awa.viburnum.util.bytes.BytesUtil;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.interfaces.ECPublicKey;
import java.security.interfaces.RSAPublicKey;

public class KeyStoreIdentity {
    public static final int EMPTY_IDENTITY = 0;
    public static final int RSA_IDENTITY = 0;
    public static final int EC_IDENTITY = 1;

    public static PublicKey createPublicKey(int identity, byte[] data) {
        return switch (identity) {
            case RSA_IDENTITY -> Crypto.decodeRsaPubkey(data);
            case EC_IDENTITY -> Crypto.decodeEcPubkey(data);
            default -> throw new IllegalStateException("Unexpected value: " + identity);
        };
    }

    public static PrivateKey createPrivateKey(int identity, byte[] data) {
        return switch (identity) {
            case RSA_IDENTITY -> Crypto.decodeRsaPrikey(data);
            case EC_IDENTITY -> Crypto.decodeEcPrikey(data);
            default -> throw new IllegalStateException("Unexpected value: " + identity);
        };
    }

    public static byte[] encodeKey(PublicKey publicKey) {
        return BytesUtil.concat(
                Base256.tagToBuf(getIdentity(publicKey)),
                publicKey.getEncoded()
        );
    }

    public static byte getIdentity(PublicKey publicKey) {
        if (publicKey instanceof RSAPublicKey) {
            return RSA_IDENTITY;
        } else if (publicKey instanceof ECPublicKey) {
            return EC_IDENTITY;
        }
        return - 1;
    }

    public static KeyPairStore createKeyPairStore(PublicKey publicKey, byte[] privateKey) {
        int identity = getIdentity(publicKey);
        return switch (identity) {
            case RSA_IDENTITY -> new RsaKeyPair(publicKey.getEncoded(),
                                                privateKey
            );
            case EC_IDENTITY -> new EcKeyPair(publicKey.getEncoded(),
                                              privateKey
            );
            default -> throw new IllegalStateException("Unexpected value: " + identity);
        };
    }
}
