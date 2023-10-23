package com.github.cao.awa.kalmia.plugin.internal.kalmia.core.server.handler.login.token;

import com.github.cao.awa.apricot.annotation.auto.Auto;
import com.github.cao.awa.kalmia.annotation.plugin.PluginRegister;
import com.github.cao.awa.kalmia.event.kalmiagram.handler.network.inbound.login.token.LoginWithTokenEventHandler;
import com.github.cao.awa.kalmia.network.packet.inbound.login.token.LoginWithTokenPacket;
import com.github.cao.awa.kalmia.network.router.kalmia.RequestRouter;
import com.github.cao.awa.modmdo.annotation.platform.Server;

@Auto
@Server
@PluginRegister(name = "kalmia_core")
public class LoginWithTokenHandler implements LoginWithTokenEventHandler {
    @Auto
    @Server
    @Override
    public void handle(RequestRouter router, LoginWithTokenPacket packet) {
        long uid = packet.uid();

        loginFailure(
                router,
                uid,
                "login.failure.invalid_token"
        );
    }
}
