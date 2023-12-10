package com.github.cao.awa.kalmia.keypair.store;

import com.github.cao.awa.apricot.io.bytes.reader.BytesReader;
import com.github.cao.awa.kalmia.annotations.crypto.CryptoEncoded;
import com.github.cao.awa.kalmia.keypair.store.key.KeyStore;
import com.github.cao.awa.kalmia.mathematic.base.Base256;
import com.github.cao.awa.viburnum.util.bytes.BytesUtil;

import java.security.PrivateKey;
import java.security.PublicKey;

public abstract class KeyPairStore {
    private final KeyStore<? extends PublicKey> publicKey;
    @CryptoEncoded
    private final KeyStore<? extends PrivateKey> privateKey;

    public KeyPairStore(byte[] publicKey, @CryptoEncoded byte[] privateKey) {
        this.publicKey = createPublicStore(publicKey);
        this.privateKey = createPrivateStore(privateKey);
    }

    public KeyStore<? extends PublicKey> publicKey() {
        return this.publicKey;
    }

    @CryptoEncoded
    public KeyStore<? extends PrivateKey> privateKey() {
        return this.privateKey;
    }

    public abstract int type();

    public abstract KeyStore<? extends PublicKey> createPublicStore(byte[] publicKey);

    public abstract KeyStore<? extends PrivateKey> createPrivateStore(byte[] privateKey);

    public byte[] toBytes() {
        byte[] publicKeyData = Base256.tagToBuf(0);
        byte[] privateKeyData = Base256.tagToBuf(0);

        if (publicKey().key().length > 0) {
            publicKeyData = BytesUtil.concat(
                    Base256.tagToBuf(publicKey().key().length),
                    publicKey().key()
            );
        }

        if (privateKey().key().length > 0) {
            privateKeyData = BytesUtil.concat(
                    Base256.tagToBuf(privateKey().key().length),
                    privateKey().key()
            );
        }

        return BytesUtil.concat(
                new byte[]{(byte) type()},
                publicKeyData,
                privateKeyData
        );
    }

    public static KeyPairStore create(BytesReader reader) {
        int type = reader.read();

        byte[] publicKey = reader.read(Base256.readTag(reader));
        byte[] privateKey = reader.read(Base256.readTag(reader));

        return KeyPairStoreFactor.create(type,
                                         publicKey,
                                         privateKey
        );
    }
}
