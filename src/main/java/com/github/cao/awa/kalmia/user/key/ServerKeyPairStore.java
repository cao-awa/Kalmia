package com.github.cao.awa.kalmia.user.key;

import com.github.cao.awa.apricot.io.bytes.reader.BytesReader;
import com.github.cao.awa.kalmia.mathematic.base.Base256;
import com.github.cao.awa.viburnum.util.bytes.BytesUtil;

public abstract class ServerKeyPairStore {
    private final byte[] publicKey;
    private final byte[] privateKey;

    public ServerKeyPairStore(byte[] publicKey, byte[] privateKey) {
        this.publicKey = publicKey;
        this.privateKey = privateKey;
    }

    public byte[] publicKey() {
        return this.publicKey;
    }

    public byte[] privateKey() {
        return this.privateKey;
    }

    public abstract int type();

    public byte[] toBytes() {
        return BytesUtil.concat(
                new byte[]{(byte) type()},
                Base256.tagToBuf(publicKey().length),
                publicKey(),
                Base256.tagToBuf(privateKey().length),
                privateKey()
        );
    }

    public static ServerKeyPairStore create(BytesReader reader) {
        int type = reader.read();

        byte[] publicKey = reader.read(Base256.tagFromBuf(reader.read(2)));
        byte[] privateKey = reader.read(Base256.tagFromBuf(reader.read(2)));

        return ServerKeyPairStoreFactor.create(type,
                                               publicKey,
                                               privateKey
        );
    }
}
