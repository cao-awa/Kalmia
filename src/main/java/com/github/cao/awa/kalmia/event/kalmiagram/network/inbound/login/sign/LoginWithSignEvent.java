package com.github.cao.awa.kalmia.event.kalmiagram.network.inbound.login.sign;

import com.github.cao.awa.kalmia.event.kalmiagram.network.NetworkEvent;
import com.github.cao.awa.kalmia.network.packet.inbound.login.sign.LoginWithSignPacket;
import com.github.cao.awa.kalmia.network.router.kalmia.RequestRouter;

public class LoginWithSignEvent extends NetworkEvent<LoginWithSignPacket> {
    public LoginWithSignEvent(RequestRouter router, LoginWithSignPacket packet) {
        super(router,
              packet
        );
    }
}
