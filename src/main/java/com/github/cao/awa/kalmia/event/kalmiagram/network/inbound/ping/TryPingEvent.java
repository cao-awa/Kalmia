package com.github.cao.awa.kalmia.event.kalmiagram.network.inbound.ping;

import com.github.cao.awa.kalmia.event.kalmiagram.network.NetworkEvent;
import com.github.cao.awa.kalmia.network.packet.inbound.ping.TryPingPacket;
import com.github.cao.awa.kalmia.network.router.kalmia.RequestRouter;

public class TryPingEvent extends NetworkEvent<TryPingPacket> {
    public TryPingEvent(RequestRouter router, TryPingPacket packet) {
        super(router,
              packet
        );
    }
}
