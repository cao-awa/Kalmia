package com.github.cao.awa.kalmia.network.packet.inbound.login;

import com.github.cao.awa.kalmia.network.handler.login.LoginHandler;
import com.github.cao.awa.kalmia.network.packet.ReadonlyPacket;
import com.github.cao.awa.kalmia.network.packet.request.login.LoginWithPasswordRequest;
import com.github.cao.awa.kalmia.network.packet.unsolve.login.UnsolvedLoginWithPasswordPacket;
import com.github.cao.awa.kalmia.network.router.UnsolvedRequestRouter;
import com.github.cao.awa.modmdo.annotation.platform.Server;

/**
 * @see LoginWithPasswordRequest
 * @see UnsolvedLoginWithPasswordPacket
 */
@Server
public class LoginWithPasswordPacket extends ReadonlyPacket<LoginHandler> {
    @Override
    public void inbound(UnsolvedRequestRouter router, LoginHandler handler) {
        System.out.println("Client Login");
    }
}
