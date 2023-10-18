package com.github.cao.awa.kalmia.plugin.internal.translation.handler.login.failed;

import com.github.cao.awa.apricot.annotation.auto.Auto;
import com.github.cao.awa.kalmia.annotation.plugin.PluginRegister;
import com.github.cao.awa.kalmia.event.handler.network.inbound.login.failed.LoginFailedEventHandler;
import com.github.cao.awa.kalmia.network.packet.inbound.login.failed.LoginFailedPacket;
import com.github.cao.awa.kalmia.network.router.kalmia.RequestRouter;

@Auto
@PluginRegister(name = "kalmia_translation")
public class LoginFailedHandler implements LoginFailedEventHandler {
    @Auto
    @Override
    public void handle(RequestRouter router, LoginFailedPacket packet) {
        System.out.println("---Login failed---");
        System.out.println("UID: " + packet.uid());
    }
}
