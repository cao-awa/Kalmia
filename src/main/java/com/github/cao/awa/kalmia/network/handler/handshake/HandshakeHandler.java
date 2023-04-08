package com.github.cao.awa.kalmia.network.handler.handshake;

import com.github.cao.awa.kalmia.network.handler.PacketHandler;
import com.github.cao.awa.kalmia.network.packet.ReadonlyPacket;
import com.github.cao.awa.kalmia.network.packet.unsolve.handshake.UnsolvedHandshakePacket;
import com.github.cao.awa.kalmia.network.router.UnsolvedRequestRouter;

public class HandshakeHandler extends PacketHandler<UnsolvedHandshakePacket<?>> {
    @Override
    public ReadonlyPacket handle(UnsolvedHandshakePacket<?> packet) {
        return packet.toPacket();
    }

    @Override
    public void inbound(ReadonlyPacket packet, UnsolvedRequestRouter router) {
        packet.inbound(router);
    }
}
