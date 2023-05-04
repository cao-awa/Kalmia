package com.github.cao.awa.kalmia.network.packet.request.handshake.crypto.aes;

import com.github.cao.awa.kalmia.mathematic.base.Base256;
import com.github.cao.awa.kalmia.mathematic.base.SkippedBase256;
import com.github.cao.awa.kalmia.network.packet.Request;
import com.github.cao.awa.kalmia.network.packet.inbound.handshake.crypto.aes.HandshakeAesCipherPacket;
import com.github.cao.awa.modmdo.annotation.platform.Client;
import com.github.cao.awa.viburnum.util.bytes.BytesUtil;

/**
 * @see HandshakeAesCipherPacket
 */
@Client
public class HandshakeAesCipherRequest extends Request {
    public static final byte[] ID = SkippedBase256.longToBuf(2);
    private final byte[] cipher;

    public HandshakeAesCipherRequest(byte[] cipher) {
        this.cipher = cipher;
    }

    @Override
    public byte[] data() {
        try {
            return BytesUtil.concat(Base256.tagToBuf(this.cipher.length),
                                    this.cipher
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
