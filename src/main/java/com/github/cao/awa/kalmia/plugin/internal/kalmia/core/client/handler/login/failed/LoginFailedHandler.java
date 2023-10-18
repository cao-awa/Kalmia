package com.github.cao.awa.kalmia.plugin.internal.kalmia.core.client.handler.login.failed;

import com.github.cao.awa.apricot.annotation.auto.Auto;
import com.github.cao.awa.kalmia.annotation.plugin.PluginRegister;
import com.github.cao.awa.kalmia.event.kalmiagram.handler.network.inbound.login.failed.LoginFailedEventHandler;
import com.github.cao.awa.kalmia.network.packet.inbound.login.failed.LoginFailedPacket;
import com.github.cao.awa.kalmia.network.router.kalmia.RequestRouter;
import com.github.cao.awa.modmdo.annotation.platform.Client;

@Auto
@Client
@PluginRegister(name = "kalmia_core")
public class LoginFailedHandler implements LoginFailedEventHandler {
    @Auto
    @Client
    @Override
    public void handle(RequestRouter router, LoginFailedPacket packet) {
        System.out.println("---Login failed---");
        System.out.println("UID: " + packet.uid());
    }
}
