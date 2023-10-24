package com.github.cao.awa.kalmia.event.kalmiagram.network.inbound.ping;

import com.github.cao.awa.kalmia.event.kalmiagram.network.NetworkEvent;
import com.github.cao.awa.kalmia.network.packet.inbound.ping.TryPingResponsePacket;
import com.github.cao.awa.kalmia.network.router.kalmia.RequestRouter;

public class TryPingResponseEvent extends NetworkEvent<TryPingResponsePacket> {
    public TryPingResponseEvent(RequestRouter router, TryPingResponsePacket packet) {
        super(router,
              packet
        );
    }
}
