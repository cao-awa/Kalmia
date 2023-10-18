package com.github.cao.awa.kalmia.event.network.inbound.handshake.hello.client;

import com.github.cao.awa.kalmia.event.network.NetworkEvent;
import com.github.cao.awa.kalmia.network.packet.inbound.handshake.hello.client.ClientHelloPacket;
import com.github.cao.awa.kalmia.network.router.kalmia.RequestRouter;

public class ClientHelloEvent extends NetworkEvent<ClientHelloPacket> {
    public ClientHelloEvent(RequestRouter router, ClientHelloPacket packet) {
        super(router,
              packet
        );
    }
}
