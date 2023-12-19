package com.github.cao.awa.kalmia.event.kalmiagram.handler.network.inbound.login;

import com.github.cao.awa.apricot.util.collection.ApricotCollectionFactor;
import com.github.cao.awa.kalmia.bootstrap.Kalmia;
import com.github.cao.awa.kalmia.event.kalmiagram.handler.network.NetworkEventHandler;
import com.github.cao.awa.kalmia.event.kalmiagram.network.NetworkEvent;
import com.github.cao.awa.kalmia.identity.LongAndExtraIdentity;
import com.github.cao.awa.kalmia.network.packet.Packet;
import com.github.cao.awa.kalmia.network.packet.inbound.chat.session.listeners.SessionListenersUpdatePacket;
import com.github.cao.awa.kalmia.network.packet.inbound.login.feedback.LoginFailurePacket;
import com.github.cao.awa.kalmia.network.packet.inbound.login.feedback.LoginSuccessPacket;
import com.github.cao.awa.kalmia.network.router.kalmia.RequestRouter;
import com.github.cao.awa.kalmia.session.Session;

import java.util.List;

public interface LoginEventHandler<P extends Packet<?>, E extends NetworkEvent<P>> extends NetworkEventHandler<P, E> {
    default void loginSuccess(RequestRouter router, LongAndExtraIdentity accessIdentity, byte[] token, byte[] receipt) {
        router.send(new LoginSuccessPacket(accessIdentity,
                                           token
        ).receipt(receipt));


        List<Session> sessions = ApricotCollectionFactor.arrayList();

        Kalmia.SERVER.userManager()
                     .sessionListeners(router.accessIdentity())
                     .forEach(id -> {
                         Session session = Kalmia.SERVER.sessionManager()
                                                        .session(id);

                         if (session == null) {
                             return;
                         }

                         sessions.add(session);
                     });

        // Update listeners every time when login success.
        router.send(new SessionListenersUpdatePacket(sessions));
    }

    default void loginFailure(RequestRouter router, LongAndExtraIdentity accessIdentity, String reason, byte[] receipt) {
        router.send(new LoginFailurePacket(accessIdentity,
                                           reason
        ).receipt(receipt));
    }
}
