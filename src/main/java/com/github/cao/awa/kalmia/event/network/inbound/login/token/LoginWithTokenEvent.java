package com.github.cao.awa.kalmia.event.network.inbound.login.token;

import com.github.cao.awa.kalmia.event.network.NetworkEvent;
import com.github.cao.awa.kalmia.network.packet.inbound.login.token.LoginWithTokenPacket;
import com.github.cao.awa.kalmia.network.router.kalmia.RequestRouter;

public class LoginWithTokenEvent extends NetworkEvent<LoginWithTokenPacket> {
    public LoginWithTokenEvent(RequestRouter router, LoginWithTokenPacket packet) {
        super(router,
              packet
        );
    }
}
