package com.github.cao.awa.kalmia.event.kalmiagram.handler.network.inbound.login;

import com.github.cao.awa.kalmia.bootstrap.Kalmia;
import com.github.cao.awa.kalmia.event.kalmiagram.handler.network.NetworkEventHandler;
import com.github.cao.awa.kalmia.event.kalmiagram.network.NetworkEvent;
import com.github.cao.awa.kalmia.network.packet.Packet;
import com.github.cao.awa.kalmia.network.packet.inbound.chat.session.listeners.SessionListenersUpdatePacket;
import com.github.cao.awa.kalmia.network.packet.inbound.login.feedback.LoginFailurePacket;
import com.github.cao.awa.kalmia.network.packet.inbound.login.feedback.LoginSuccessPacket;
import com.github.cao.awa.kalmia.network.router.kalmia.RequestRouter;

public interface LoginEventHandler<P extends Packet<?>, E extends NetworkEvent<P>> extends NetworkEventHandler<P, E> {
    default void loginSuccess(RequestRouter router, long uid, byte[] token) {
        router.send(new LoginSuccessPacket(uid,
                                           token
        ));


        // Update listeners every time when login success.
        router.send(new SessionListenersUpdatePacket(Kalmia.SERVER.userManager()
                                                                  .sessionListeners(router.uid())));
    }

    default void loginFailure(RequestRouter router, long uid, String reason) {
        router.send(new LoginFailurePacket(uid,
                                           reason
        ));
    }
}
