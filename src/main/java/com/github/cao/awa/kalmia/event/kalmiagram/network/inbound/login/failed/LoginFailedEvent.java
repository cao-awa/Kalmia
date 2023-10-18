package com.github.cao.awa.kalmia.event.kalmiagram.network.inbound.login.failed;

import com.github.cao.awa.kalmia.event.kalmiagram.network.NetworkEvent;
import com.github.cao.awa.kalmia.network.packet.inbound.login.failed.LoginFailedPacket;
import com.github.cao.awa.kalmia.network.router.kalmia.RequestRouter;

public class LoginFailedEvent extends NetworkEvent<LoginFailedPacket> {
    public LoginFailedEvent(RequestRouter router, LoginFailedPacket packet) {
        super(router,
              packet
        );
    }
}

