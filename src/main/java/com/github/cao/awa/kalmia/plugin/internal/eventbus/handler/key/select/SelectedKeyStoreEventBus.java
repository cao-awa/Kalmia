package com.github.cao.awa.kalmia.plugin.internal.eventbus.handler.key.select;

import com.github.cao.awa.apricot.annotations.auto.Auto;
import com.github.cao.awa.kalmia.annotations.plugin.PluginRegister;
import com.github.cao.awa.kalmia.event.kalmiagram.handler.network.inbound.key.select.SelectedKeyStoreEventHandler;
import com.github.cao.awa.kalmia.network.packet.inbound.key.select.SelectedKeyStorePacket;
import com.github.cao.awa.kalmia.network.router.kalmia.RequestRouter;
import com.github.cao.awa.kalmia.plugin.internal.eventbus.EventBus;
import com.github.cao.awa.modmdo.annotation.platform.Client;

@Auto
@Client
@PluginRegister(name = "kalmia_eventbus")
public class SelectedKeyStoreEventBus extends EventBus<SelectedKeyStoreEventBusHandler> implements SelectedKeyStoreEventHandler {
    @Auto
    @Client
    @Override
    public void handle(RequestRouter router, SelectedKeyStorePacket packet) {
        trigger(handler -> handler.handle(router,
                                          packet.receipt(),
                                          packet.keys()
        ));
    }
}
