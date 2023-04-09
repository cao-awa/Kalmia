package com.github.cao.awa.kalmia.network.packet.unsolve.handshake.hello;

import com.github.cao.awa.kalmia.network.packet.handshake.hello.ServerHelloPacket;
import com.github.cao.awa.kalmia.network.packet.unsolve.handshake.UnsolvedHandshakePacket;
import com.github.cao.awa.modmdo.annotation.platform.Server;

/**
 * @see ServerHelloPacket
 */
@Server
public class UnsolvedServerHelloPacket extends UnsolvedHandshakePacket<ServerHelloPacket> {
    public UnsolvedServerHelloPacket(byte[] data) {
        super(data);
    }

    @Override
    public ServerHelloPacket toPacket() {
        return ServerHelloPacket.create(reader());
    }
}
