package com.github.cao.awa.kalmia.plugin.internal.eventbus.handler.chat.session.request;

import com.github.cao.awa.apricot.annotations.auto.Auto;
import com.github.cao.awa.kalmia.event.kalmiagram.handler.network.inbound.chat.session.request.RequestGroupSessionEventHandler;
import com.github.cao.awa.kalmia.network.packet.inbound.chat.session.request.RequestGroupSessionPacket;
import com.github.cao.awa.kalmia.network.router.kalmia.RequestRouter;
import com.github.cao.awa.kalmia.plugin.internal.eventbus.EventBus;
import com.github.cao.awa.modmdo.annotation.platform.Server;

public class RequestGroupSessionEventBus extends EventBus<RequestGroupSessionEventBusHandler> implements RequestGroupSessionEventHandler {
    @Auto
    @Server
    @Override
    public void handle(RequestRouter router, RequestGroupSessionPacket packet) {
        trigger(handler -> handler.handle(router,
                                          packet.receipt(),
                                          packet.name()
        ));
    }
}
