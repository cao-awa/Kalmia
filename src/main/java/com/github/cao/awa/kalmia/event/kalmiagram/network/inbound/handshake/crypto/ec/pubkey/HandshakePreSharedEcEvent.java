package com.github.cao.awa.kalmia.event.kalmiagram.network.inbound.handshake.crypto.ec.pubkey;

import com.github.cao.awa.kalmia.event.kalmiagram.network.NetworkEvent;
import com.github.cao.awa.kalmia.network.packet.inbound.handshake.crypto.ec.pubkey.HandshakePreSharedEcPacket;
import com.github.cao.awa.kalmia.network.router.kalmia.RequestRouter;

public class HandshakePreSharedEcEvent extends NetworkEvent<HandshakePreSharedEcPacket> {
    public HandshakePreSharedEcEvent(RequestRouter router, HandshakePreSharedEcPacket packet) {
        super(router,
              packet
        );
    }
}
