package com.github.cao.awa.kalmia.plugin.internal.eventbus.handler.key.select;

import com.github.cao.awa.apricot.annotations.auto.Auto;
import com.github.cao.awa.kalmia.event.kalmiagram.handler.network.inbound.key.select.SelectKeyStoreEventHandler;
import com.github.cao.awa.kalmia.network.packet.inbound.key.select.SelectKeyStorePacket;
import com.github.cao.awa.kalmia.network.router.kalmia.RequestRouter;
import com.github.cao.awa.kalmia.plugin.internal.eventbus.EventBus;
import com.github.cao.awa.modmdo.annotation.platform.Server;

public class SelectKeyStoreEventBus extends EventBus<SelectKeyStoreEventBusHandler> implements SelectKeyStoreEventHandler {
    @Auto
    @Server
    @Override
    public void handle(RequestRouter router, SelectKeyStorePacket packet) {
        trigger(handler -> handler.handle(router,
                                          packet.receipt(),
                                          packet.ids()
        ));
    }
}
