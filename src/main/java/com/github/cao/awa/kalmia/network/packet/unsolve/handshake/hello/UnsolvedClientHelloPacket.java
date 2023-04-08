package com.github.cao.awa.kalmia.network.packet.unsolve.handshake.hello;

import com.github.cao.awa.kalmia.network.packet.handshake.hello.ClientHelloPacket;
import com.github.cao.awa.kalmia.network.packet.unsolve.handshake.UnsolvedHandshakePacket;
import com.github.cao.awa.modmdo.annotation.platform.Server;

/**
 * @see ClientHelloPacket
 */
@Server
public class UnsolvedClientHelloPacket extends UnsolvedHandshakePacket<ClientHelloPacket> {
    public UnsolvedClientHelloPacket(byte[] data) {
        super(data);
    }

    @Override
    public ClientHelloPacket toPacket() {
        return ClientHelloPacket.create(reader());
    }
}
