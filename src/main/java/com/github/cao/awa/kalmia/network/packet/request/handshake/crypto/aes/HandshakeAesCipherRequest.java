package com.github.cao.awa.kalmia.network.packet.request.handshake.crypto.aes;

import com.github.cao.awa.apricot.util.encryption.Crypto;
import com.github.cao.awa.kalmia.mathematic.base.Base256;
import com.github.cao.awa.kalmia.mathematic.base.SkippedBase256;
import com.github.cao.awa.kalmia.network.packet.WritablePacket;
import com.github.cao.awa.kalmia.network.packet.inbound.handshake.crypto.aes.HandshakeAesCipherPacket;
import com.github.cao.awa.modmdo.annotation.platform.Client;
import com.github.cao.awa.viburnum.util.bytes.BytesUtil;

/**
 * @see HandshakeAesCipherPacket
 */
@Client
public class HandshakeAesCipherRequest extends WritablePacket {
    public static final byte[] ID = SkippedBase256.longToBuf(2);
    private final byte[] pubkey;
    private final byte[] cipher;

    public HandshakeAesCipherRequest(byte[] pubkey, byte[] cipher) {
        this.pubkey = pubkey;
        this.cipher = cipher;
    }

    @Override
    public byte[] data() {
        try {
            byte[] encrypted = Crypto.rsaEncrypt(cipher,
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
