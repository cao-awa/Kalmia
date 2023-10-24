package com.github.cao.awa.kalmia.plugin.internal.kalmia.core.uni.handler.ping;

import com.github.cao.awa.kalmia.event.kalmiagram.handler.network.inbound.ping.TryPingEventHandler;
import com.github.cao.awa.kalmia.network.packet.inbound.ping.TryPingPacket;
import com.github.cao.awa.kalmia.network.packet.inbound.ping.TryPingResponsePacket;
import com.github.cao.awa.kalmia.network.router.kalmia.RequestRouter;

public class TryPingHandler implements TryPingEventHandler {

    @Override
    public void handle(RequestRouter router, TryPingPacket packet) {
        router.send(new TryPingResponsePacket(packet.startTime(),
                                              packet.receipt()
        ));
    }
}
