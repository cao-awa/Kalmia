package com.github.cao.awa.kalmia.plugin.internal.eventbus.handler.ping;

import com.github.cao.awa.apricot.annotations.auto.Auto;
import com.github.cao.awa.kalmia.annotations.plugin.PluginRegister;
import com.github.cao.awa.kalmia.event.kalmiagram.handler.network.inbound.ping.TryPingEventHandler;
import com.github.cao.awa.kalmia.network.packet.inbound.ping.TryPingPacket;
import com.github.cao.awa.kalmia.network.router.kalmia.RequestRouter;
import com.github.cao.awa.kalmia.plugin.internal.eventbus.EventBus;
import com.github.cao.awa.modmdo.annotation.platform.Server;

@Auto
@Server
@PluginRegister(name = "kalmia_eventbus")
public class TryPingEventBus extends EventBus<TryPingEventBusHandler> implements TryPingEventHandler {
    @Auto
    @Server
    @Override
    public void handle(RequestRouter router, TryPingPacket packet) {
        trigger(handler -> handler.handle(router,
                                          packet.receipt(),
                                          packet.startTime()
        ));
    }
}
