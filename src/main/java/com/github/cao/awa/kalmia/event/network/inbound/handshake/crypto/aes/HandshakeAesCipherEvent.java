package com.github.cao.awa.kalmia.event.network.inbound.handshake.crypto.aes;

import com.github.cao.awa.kalmia.event.network.NetworkEvent;
import com.github.cao.awa.kalmia.network.packet.inbound.handshake.crypto.aes.HandshakeAesCipherPacket;
import com.github.cao.awa.kalmia.network.router.kalmia.RequestRouter;

public class HandshakeAesCipherEvent extends NetworkEvent<HandshakeAesCipherPacket> {
    public HandshakeAesCipherEvent(RequestRouter router, HandshakeAesCipherPacket packet) {
        super(router,
              packet
        );
    }
}

