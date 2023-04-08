package com.github.cao.awa.kalmia.network.packet.request.handshake.crypto.rsa.pubkey;

import com.github.cao.awa.kalmia.mathematic.base.Base256;
import com.github.cao.awa.kalmia.mathematic.base.SkippedBase256;
import com.github.cao.awa.kalmia.network.handler.handshake.HandshakeHandler;
import com.github.cao.awa.kalmia.network.packet.WritablePacket;
import com.github.cao.awa.kalmia.network.packet.handshake.crypto.rsa.pubkey.HandshakeRsaPubkeyPacket;
import com.github.cao.awa.modmdo.annotation.platform.Server;
import com.github.cao.awa.viburnum.util.bytes.BytesUtil;

/**
 * @see HandshakeRsaPubkeyPacket
 */
@Server
public class HandshakeRsaPubkeyRequest extends WritablePacket {
    private static final byte[] ID = SkippedBase256.longToBuf(1);
    private final HandshakeHandler handler;

    public HandshakeRsaPubkeyRequest(HandshakeHandler handler) {
        this.handler = handler;
    }

    @Override
    public byte[] data() {
        return BytesUtil.concat(Base256.tagToBuf(this.handler.getRsaPubkey().length),
                                this.handler.getRsaPubkey()
        );
    }

    @Override
    public byte[] id() {
        return ID;
    }
}
