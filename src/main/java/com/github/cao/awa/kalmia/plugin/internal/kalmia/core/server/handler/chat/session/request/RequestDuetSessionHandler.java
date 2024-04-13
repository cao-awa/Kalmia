package com.github.cao.awa.kalmia.plugin.internal.kalmia.core.server.handler.chat.session.request;

import com.github.cao.awa.apricot.annotations.auto.Auto;
import com.github.cao.awa.apricot.identifier.BytesRandomIdentifier;
import com.github.cao.awa.kalmia.annotations.plugin.PluginRegister;
import com.github.cao.awa.kalmia.bootstrap.Kalmia;
import com.github.cao.awa.kalmia.event.kalmiagram.handler.network.inbound.chat.session.request.RequestDuetSessionEventHandler;
import com.github.cao.awa.kalmia.identity.LongAndExtraIdentity;
import com.github.cao.awa.kalmia.identity.PureExtraIdentity;
import com.github.cao.awa.kalmia.network.packet.inbound.chat.session.in.ChatInSessionPacket;
import com.github.cao.awa.kalmia.network.packet.inbound.chat.session.request.RequestDuetSessionPacket;
import com.github.cao.awa.kalmia.network.router.kalmia.RequestRouter;
import com.github.cao.awa.kalmia.session.SessionAccessibleData;
import com.github.cao.awa.kalmia.session.duet.DuetSession;
import com.github.cao.awa.modmdo.annotation.platform.Server;

@Auto
@Server
@PluginRegister(name = "kalmia_core")
public class RequestDuetSessionHandler implements RequestDuetSessionEventHandler {
    @Auto
    @Server
    @Override
    public void handle(RequestRouter router, RequestDuetSessionPacket packet) {
        LongAndExtraIdentity targetIdentity = packet.targetUser();

        PureExtraIdentity sessionIdentity = Kalmia.SERVER.getUserManager().duetSession(router.accessIdentity(), targetIdentity);
        if (sessionIdentity == null) {
            sessionIdentity = Kalmia.SERVER.getSessionManager().add(new DuetSession(PureExtraIdentity.create(BytesRandomIdentifier.create(16)), router.accessIdentity(), targetIdentity));

            // Update session data.
            Kalmia.SERVER.getUserManager().duetSession(router.accessIdentity(), targetIdentity, sessionIdentity);

            // Update accessible.
            Kalmia.SERVER.getSessionManager().updateAccessible(sessionIdentity, router.accessIdentity(), SessionAccessibleData::accessibleChat);
            Kalmia.SERVER.getSessionManager().updateAccessible(sessionIdentity, targetIdentity, SessionAccessibleData::accessibleChat);
        }

        router.send(new ChatInSessionPacket(targetIdentity, sessionIdentity));
    }
}
