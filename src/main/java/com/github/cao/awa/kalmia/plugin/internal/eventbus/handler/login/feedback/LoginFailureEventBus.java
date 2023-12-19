package com.github.cao.awa.kalmia.plugin.internal.eventbus.handler.login.feedback;

import com.github.cao.awa.apricot.annotations.auto.Auto;
import com.github.cao.awa.kalmia.event.kalmiagram.handler.network.inbound.login.feedback.LoginFailureEventHandler;
import com.github.cao.awa.kalmia.network.packet.inbound.login.feedback.LoginFailurePacket;
import com.github.cao.awa.kalmia.network.router.kalmia.RequestRouter;
import com.github.cao.awa.kalmia.plugin.internal.eventbus.EventBus;
import com.github.cao.awa.modmdo.annotation.platform.Client;

public class LoginFailureEventBus extends EventBus<LoginFailureEventBusHandler> implements LoginFailureEventHandler {
    @Auto
    @Client
    @Override
    public void handle(RequestRouter router, LoginFailurePacket packet) {
        trigger(handler -> handler.handle(router,
                                          packet.receipt(),
                                          packet.accessIdentity(),
                                          packet.reason()
        ));
    }
}
