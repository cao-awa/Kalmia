package com.github.cao.awa.kalmia.plugin.internal.eventbus.handler.chat.session.request;

import com.github.cao.awa.apricot.annotations.auto.Auto;
import com.github.cao.awa.kalmia.event.kalmiagram.handler.network.inbound.chat.session.request.RequestDuetSessionEventHandler;
import com.github.cao.awa.kalmia.network.packet.inbound.chat.session.request.RequestDuetSessionPacket;
import com.github.cao.awa.kalmia.network.router.kalmia.RequestRouter;
import com.github.cao.awa.kalmia.plugin.internal.eventbus.EventBus;
import com.github.cao.awa.modmdo.annotation.platform.Server;

public class RequestDuetSessionUpdateEventBus extends EventBus<RequestDuetSessionEventBusHandler> implements RequestDuetSessionEventHandler {
    @Auto
    @Server
    @Override
    public void handle(RequestRouter router, RequestDuetSessionPacket packet) {
        trigger(handler -> handler.handle(router,
                                          packet.receipt(),
                                          packet.targetUser()
        ));
    }
}
