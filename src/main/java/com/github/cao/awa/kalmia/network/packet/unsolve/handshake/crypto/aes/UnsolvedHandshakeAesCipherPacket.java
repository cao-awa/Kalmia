package com.github.cao.awa.kalmia.network.packet.unsolve.handshake.crypto.aes;

import com.github.cao.awa.kalmia.network.packet.inbound.handshake.crypto.aes.HandshakeAesCipherPacket;
import com.github.cao.awa.kalmia.network.packet.unsolve.handshake.UnsolvedHandshakePacket;
import com.github.cao.awa.modmdo.annotation.platform.Server;

/**
 * @see HandshakeAesCipherPacket
 */
@Server
public class UnsolvedHandshakeAesCipherPacket extends UnsolvedHandshakePacket<HandshakeAesCipherPacket> {
    public UnsolvedHandshakeAesCipherPacket(byte[] data) {
        super(data);
    }

    @Override
    public HandshakeAesCipherPacket toPacket() {
        return HandshakeAesCipherPacket.create(reader());
    }
}
