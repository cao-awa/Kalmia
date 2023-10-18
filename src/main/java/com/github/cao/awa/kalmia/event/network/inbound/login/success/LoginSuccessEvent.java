package com.github.cao.awa.kalmia.event.network.inbound.login.success;

import com.github.cao.awa.kalmia.event.network.NetworkEvent;
import com.github.cao.awa.kalmia.network.packet.inbound.login.success.LoginSuccessPacket;
import com.github.cao.awa.kalmia.network.router.kalmia.RequestRouter;

public class LoginSuccessEvent extends NetworkEvent<LoginSuccessPacket> {
    public LoginSuccessEvent(RequestRouter router, LoginSuccessPacket packet) {
        super(router,
              packet
        );
    }
}
