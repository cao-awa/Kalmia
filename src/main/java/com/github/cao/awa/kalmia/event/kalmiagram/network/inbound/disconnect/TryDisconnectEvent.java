package com.github.cao.awa.kalmia.event.kalmiagram.network.inbound.disconnect;

import com.github.cao.awa.kalmia.event.kalmiagram.network.NetworkEvent;
import com.github.cao.awa.kalmia.network.packet.inbound.disconnet.TryDisconnectPacket;
import com.github.cao.awa.kalmia.network.router.kalmia.RequestRouter;

public class TryDisconnectEvent extends NetworkEvent<TryDisconnectPacket> {
    public TryDisconnectEvent(RequestRouter router, TryDisconnectPacket packet) {
        super(router,
              packet
        );
    }
}

