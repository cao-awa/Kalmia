package com.github.cao.awa.kalmia.plugin.internal.eventbus.handler.login.password;

import com.github.cao.awa.apricot.annotations.auto.Auto;
import com.github.cao.awa.kalmia.event.kalmiagram.handler.network.inbound.login.password.LoginWithPasswordEventHandler;
import com.github.cao.awa.kalmia.network.packet.inbound.login.password.LoginWithPasswordPacket;
import com.github.cao.awa.kalmia.network.router.kalmia.RequestRouter;
import com.github.cao.awa.kalmia.plugin.internal.eventbus.EventBus;
import com.github.cao.awa.modmdo.annotation.platform.Server;

public class LoginWithPasswordEventBus extends EventBus<LoginWithPasswordEventBusHandler> implements LoginWithPasswordEventHandler {
    @Auto
    @Server
    @Override
    public void handle(RequestRouter router, LoginWithPasswordPacket packet) {
        trigger(handler -> handler.handle(router,
                                          packet.receipt(),
                                          packet.uid(),
                                          packet.password()
        ));
    }
}
