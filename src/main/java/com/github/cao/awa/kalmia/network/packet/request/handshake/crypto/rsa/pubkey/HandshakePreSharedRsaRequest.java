package com.github.cao.awa.kalmia.network.packet.request.handshake.crypto.rsa.pubkey;

import com.github.cao.awa.kalmia.mathematic.base.SkippedBase256;
import com.github.cao.awa.kalmia.network.packet.Request;
import com.github.cao.awa.kalmia.network.packet.inbound.handshake.crypto.rsa.pubkey.HandshakePreSharedRsaPacket;
import com.github.cao.awa.modmdo.annotation.platform.Server;
import com.github.cao.awa.viburnum.util.bytes.BytesUtil;

import java.nio.charset.StandardCharsets;

/**
 * @see HandshakePreSharedRsaPacket
 */
@Server
public class HandshakePreSharedRsaRequest extends Request {
    public static final byte[] ID = SkippedBase256.longToBuf(1);
    private final String preShareKey;

    public HandshakePreSharedRsaRequest(String preShareKey) {
        this.preShareKey = preShareKey;
    }

    @Override
    public byte[] data() {
        return BytesUtil.concat(this.preShareKey.getBytes(StandardCharsets.US_ASCII));
    }

    @Override
    public byte[] id() {
        return ID;
    }
}
