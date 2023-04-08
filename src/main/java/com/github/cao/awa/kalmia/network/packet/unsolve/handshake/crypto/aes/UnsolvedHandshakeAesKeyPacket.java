package com.github.cao.awa.kalmia.network.packet.unsolve.handshake.crypto.aes;

import com.github.cao.awa.kalmia.network.packet.handshake.crypto.aes.HandshakeAesKeyPacket;
import com.github.cao.awa.kalmia.network.packet.unsolve.handshake.UnsolvedHandshakePacket;
import com.github.cao.awa.modmdo.annotation.platform.Server;

/**
 * @see HandshakeAesKeyPacket
 */
@Server
public class UnsolvedHandshakeAesKeyPacket extends UnsolvedHandshakePacket<HandshakeAesKeyPacket> {
    public UnsolvedHandshakeAesKeyPacket(byte[] data) {
        super(data);
    }

    @Override
    public HandshakeAesKeyPacket toPacket() {
        return HandshakeAesKeyPacket.create(reader());
    }
}
