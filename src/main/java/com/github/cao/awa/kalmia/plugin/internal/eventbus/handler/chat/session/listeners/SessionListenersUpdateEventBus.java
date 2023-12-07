package com.github.cao.awa.kalmia.plugin.internal.eventbus.handler.chat.session.listeners;

import com.github.cao.awa.apricot.annotations.auto.Auto;
import com.github.cao.awa.kalmia.annotations.plugin.PluginRegister;
import com.github.cao.awa.kalmia.event.kalmiagram.handler.network.inbound.chat.session.in.SessionListenersUpdateEventHandler;
import com.github.cao.awa.kalmia.network.packet.inbound.chat.session.listeners.SessionListenersUpdatePacket;
import com.github.cao.awa.kalmia.network.router.kalmia.RequestRouter;
import com.github.cao.awa.kalmia.plugin.internal.eventbus.EventBus;
import com.github.cao.awa.modmdo.annotation.platform.Client;

@Auto
@Client
@PluginRegister(name = "kalmia_eventbus")
public class SessionListenersUpdateEventBus extends EventBus<SessionListenersUpdateEventBusHandler> implements SessionListenersUpdateEventHandler {
    @Auto
    @Client
    @Override
    public void handle(RequestRouter router, SessionListenersUpdatePacket packet) {
        trigger(handler -> handler.handle(router,
                                          packet.receipt(),
                                          packet.listeners()
        ));
    }
}
