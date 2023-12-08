package com.github.cao.awa.kalmia.plugin.internal.eventbus.handler.login.sign;

import com.github.cao.awa.apricot.annotations.auto.Auto;
import com.github.cao.awa.kalmia.event.kalmiagram.handler.network.inbound.login.sign.LoginWithSignEventHandler;
import com.github.cao.awa.kalmia.network.packet.inbound.login.sign.LoginWithSignPacket;
import com.github.cao.awa.kalmia.network.router.kalmia.RequestRouter;
import com.github.cao.awa.kalmia.plugin.internal.eventbus.EventBus;
import com.github.cao.awa.modmdo.annotation.platform.Server;

public class LoginWithSignEventBus extends EventBus<LoginWithSignEventBusHandler> implements LoginWithSignEventHandler {
    @Auto
    @Server
    @Override
    public void handle(RequestRouter router, LoginWithSignPacket packet) {
        trigger(handler -> handler.handle(router,
                                          packet.receipt(),
                                          packet.uid(),
                                          packet.challengeData()
        ));
    }
}
