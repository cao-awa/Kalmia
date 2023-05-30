package com.github.cao.awa.kalmia.network.packet.inbound.login.sign;

import com.github.cao.awa.kalmia.annotation.auto.network.unsolve.AutoSolvedPacket;
import com.github.cao.awa.kalmia.network.handler.inbound.AuthedRequestHandler;
import com.github.cao.awa.kalmia.network.packet.Packet;
import com.github.cao.awa.kalmia.network.router.RequestRouter;

@AutoSolvedPacket(100003)
public class LoginWithSignPacket extends Packet<AuthedRequestHandler> {
    @Override
    public void inbound(RequestRouter router, AuthedRequestHandler handler) {

    }
}
