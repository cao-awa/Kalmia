package com.github.cao.awa.kalmia.event.kalmiagram.network.inbound.login.feedback;

import com.github.cao.awa.kalmia.event.kalmiagram.network.NetworkEvent;
import com.github.cao.awa.kalmia.network.packet.inbound.login.feedback.LoginSuccessPacket;
import com.github.cao.awa.kalmia.network.router.kalmia.RequestRouter;

public class LoginSuccessEvent extends NetworkEvent<LoginSuccessPacket> {
    public LoginSuccessEvent(RequestRouter router, LoginSuccessPacket packet) {
        super(router,
              packet
        );
    }
}
