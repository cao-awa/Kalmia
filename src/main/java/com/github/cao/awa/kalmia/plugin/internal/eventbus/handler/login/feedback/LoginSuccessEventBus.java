package com.github.cao.awa.kalmia.plugin.internal.eventbus.handler.login.feedback;

import com.github.cao.awa.apricot.annotations.auto.Auto;
import com.github.cao.awa.kalmia.event.kalmiagram.handler.network.inbound.login.feedback.LoginSuccessEventHandler;
import com.github.cao.awa.kalmia.network.packet.inbound.login.feedback.LoginSuccessPacket;
import com.github.cao.awa.kalmia.network.router.kalmia.RequestRouter;
import com.github.cao.awa.kalmia.plugin.internal.eventbus.EventBus;
import com.github.cao.awa.modmdo.annotation.platform.Client;

public class LoginSuccessEventBus extends EventBus<LoginSuccessEventBusHandler> implements LoginSuccessEventHandler {
    @Auto
    @Client
    @Override
    public void handle(RequestRouter router, LoginSuccessPacket packet) {
        trigger(handler -> handler.handle(router,
                                          packet.receipt(),
                                          packet.uid(),
                                          packet.token()
        ));
    }
}
