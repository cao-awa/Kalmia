package com.github.cao.awa.kalmia.network.packet.inbound.login.sign.challenge;

import com.github.cao.awa.apricot.annotation.Unsupported;
import com.github.cao.awa.kalmia.network.handler.ping.StatelessHandler;
import com.github.cao.awa.kalmia.network.packet.Packet;
import com.github.cao.awa.kalmia.network.router.RequestRouter;

@Unsupported
public class LoginChallengePacket extends Packet<StatelessHandler> {
    @Override
    public void inbound(RequestRouter router, StatelessHandler handler) {

    }
}
