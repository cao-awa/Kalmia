package com.github.cao.awa.kalmia.event.handler.network.inbound.login;

import com.github.cao.awa.kalmia.event.handler.network.NetworkEventHandler;
import com.github.cao.awa.kalmia.event.network.NetworkEvent;
import com.github.cao.awa.kalmia.network.packet.Packet;
import com.github.cao.awa.kalmia.network.packet.inbound.login.failed.LoginFailedPacket;
import com.github.cao.awa.kalmia.network.packet.inbound.login.success.LoginSuccessPacket;
import com.github.cao.awa.kalmia.network.router.RequestRouter;

public interface LoginEventHandler<P extends Packet<?>, E extends NetworkEvent<P>> extends NetworkEventHandler<P, E> {
    default void loginSuccess(RequestRouter router, long uid, byte[] token) {
        router.send(new LoginSuccessPacket(uid,
                                           token
        ));
    }

    default void loginFailure(RequestRouter router, long uid) {
        router.send(new LoginFailedPacket(uid));
    }
}
