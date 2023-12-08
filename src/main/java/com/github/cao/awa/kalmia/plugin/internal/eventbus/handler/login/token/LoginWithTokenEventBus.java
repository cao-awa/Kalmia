package com.github.cao.awa.kalmia.plugin.internal.eventbus.handler.login.token;

import com.github.cao.awa.apricot.annotations.auto.Auto;
import com.github.cao.awa.kalmia.event.kalmiagram.handler.network.inbound.login.token.LoginWithTokenEventHandler;
import com.github.cao.awa.kalmia.network.packet.inbound.login.token.LoginWithTokenPacket;
import com.github.cao.awa.kalmia.network.router.kalmia.RequestRouter;
import com.github.cao.awa.kalmia.plugin.internal.eventbus.EventBus;
import com.github.cao.awa.modmdo.annotation.platform.Server;

public class LoginWithTokenEventBus extends EventBus<LoginWithTokenEventBusHandler> implements LoginWithTokenEventHandler {
    @Auto
    @Server
    @Override
    public void handle(RequestRouter router, LoginWithTokenPacket packet) {
        trigger(handler -> handler.handle(router,
                                          packet.receipt(),
                                          packet.uid(),
                                          packet.token()
        ));
    }
}
