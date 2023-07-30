package com.github.cao.awa.kalmia.event.network.inbound.disconnect;

import com.github.cao.awa.kalmia.event.network.NetworkEvent;
import com.github.cao.awa.kalmia.network.packet.inbound.disconnet.TryDisconnectPacket;
import com.github.cao.awa.kalmia.network.router.RequestRouter;

public class TryDisconnectEvent extends NetworkEvent<TryDisconnectPacket> {
    public TryDisconnectEvent(RequestRouter router, TryDisconnectPacket packet) {
        super(router,
              packet
        );
    }
}

