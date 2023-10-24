package com.github.cao.awa.kalmia.plugin.internal.kalmia.core.uni.handler.ping;

import com.github.cao.awa.apricot.util.time.TimeUtil;
import com.github.cao.awa.kalmia.event.kalmiagram.handler.network.inbound.ping.TryPingResponseEventHandler;
import com.github.cao.awa.kalmia.network.packet.inbound.ping.TryPingResponsePacket;
import com.github.cao.awa.kalmia.network.router.kalmia.RequestRouter;

public class TryPingResponseHandler implements TryPingResponseEventHandler {
    @Override
    public void handle(RequestRouter router, TryPingResponsePacket packet) {
        double responseMillions = TimeUtil.processNano(packet.startTime()) / 1000000D;
        System.out.println("Ping: " + responseMillions + "ms");
    }
}
