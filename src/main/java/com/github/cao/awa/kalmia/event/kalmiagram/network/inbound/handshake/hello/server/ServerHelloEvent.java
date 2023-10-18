package com.github.cao.awa.kalmia.event.kalmiagram.network.inbound.handshake.hello.server;

import com.github.cao.awa.kalmia.event.kalmiagram.network.NetworkEvent;
import com.github.cao.awa.kalmia.network.packet.inbound.handshake.hello.server.ServerHelloPacket;
import com.github.cao.awa.kalmia.network.router.kalmia.RequestRouter;

public class ServerHelloEvent extends NetworkEvent<ServerHelloPacket> {
    public ServerHelloEvent(RequestRouter router, ServerHelloPacket packet) {
        super(router,
              packet
        );
    }
}
