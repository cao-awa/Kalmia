package com.github.cao.awa.kalmia.network.packet.request.handshake.crypto.aes;

import com.github.cao.awa.apricot.util.encryption.Crypto;
import com.github.cao.awa.kalmia.mathematic.base.Base256;
import com.github.cao.awa.kalmia.mathematic.base.SkippedBase256;
import com.github.cao.awa.kalmia.network.packet.WritablePacket;
import com.github.cao.awa.kalmia.network.packet.handshake.crypto.aes.HandshakeAesKeyPacket;
import com.github.cao.awa.modmdo.annotation.platform.Client;
import com.github.cao.awa.viburnum.util.bytes.BytesUtil;

/**
 * @see HandshakeAesKeyPacket
 */
@Client
public class HandshakeAesKeyRequest extends WritablePacket {
    private static final byte[] ID = SkippedBase256.longToBuf(2);
    private final byte[] pubkey;

    public HandshakeAesKeyRequest(byte[] pubkey) {
        this.pubkey = pubkey;
    }

    @Override
    public byte[] data() {
        try {
            byte[] encrypted = Crypto.rsaEncrypt("TEST_KEY_EU".getBytes(),
                                                 Crypto.decodeRsaPubkey(this.pubkey)
            );
            return BytesUtil.concat(Base256.tagToBuf(encrypted.length),
                                    encrypted
            );
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public byte[] id() {
        return ID;
    }
}
