package com.github.cao.awa.kalmia.event.kalmiagram.network.inbound.login.feedback;

import com.github.cao.awa.kalmia.event.kalmiagram.network.NetworkEvent;
import com.github.cao.awa.kalmia.network.packet.inbound.login.feedback.LoginFailurePacket;
import com.github.cao.awa.kalmia.network.router.kalmia.RequestRouter;

public class LoginFailureEvent extends NetworkEvent<LoginFailurePacket> {
    public LoginFailureEvent(RequestRouter router, LoginFailurePacket packet) {
        super(router,
              packet
        );
    }
}

