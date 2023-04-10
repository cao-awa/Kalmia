package com.github.cao.awa.kalmia.network.packet.inbound.login;

import com.github.cao.awa.kalmia.network.handler.PacketHandler;
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
public class LoginWithPasswordPacket extends ReadonlyPacket {
    @Override
    public void inbound(UnsolvedRequestRouter router, PacketHandler<?> handler) {
        System.out.println("Client Login");
    }
}
