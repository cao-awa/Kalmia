package com.github.cao.awa.kalmia.plugin.internal.kalmia.core.client.handler.login.feedback;

import com.github.cao.awa.apricot.annotation.auto.Auto;
import com.github.cao.awa.kalmia.annotation.plugin.PluginRegister;
import com.github.cao.awa.kalmia.event.kalmiagram.handler.network.inbound.login.feedback.LoginFailureEventHandler;
import com.github.cao.awa.kalmia.network.packet.inbound.login.feedback.LoginFailurePacket;
import com.github.cao.awa.kalmia.network.router.kalmia.RequestRouter;
import com.github.cao.awa.modmdo.annotation.platform.Client;

@Auto
@Client
@PluginRegister(name = "kalmia_client")
public class LoginFailureHandler implements LoginFailureEventHandler {
    @Auto
    @Client
    @Override
    public void handle(RequestRouter router, LoginFailurePacket packet) {
        System.out.println("---Login failed---");
        System.out.println("UID: " + packet.uid());
    }
}
