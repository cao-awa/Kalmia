package com.github.cao.awa.kalmia.network.packet.inbound.login.token;

import com.github.cao.awa.apricot.annotation.Unsupported;
import com.github.cao.awa.kalmia.annotation.auto.network.unsolve.AutoSolvedPacket;
import com.github.cao.awa.kalmia.network.handler.ping.StatelessHandler;
import com.github.cao.awa.kalmia.network.packet.Packet;
import com.github.cao.awa.kalmia.network.router.RequestRouter;

@Unsupported
@AutoSolvedPacket(100002)
public class LoginWithTokenPacket extends Packet<StatelessHandler> {
    @Override
    public void inbound(RequestRouter router, StatelessHandler handler) {

    }
}