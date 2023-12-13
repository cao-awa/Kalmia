package com.github.cao.awa.kalmia.plugin.internal.kalmia.core.server.handler.chat.session.request;

import com.github.cao.awa.apricot.annotations.auto.Auto;
import com.github.cao.awa.kalmia.annotations.plugin.PluginRegister;
import com.github.cao.awa.kalmia.bootstrap.Kalmia;
import com.github.cao.awa.kalmia.event.kalmiagram.handler.network.inbound.chat.session.request.RequestGroupSessionEventHandler;
import com.github.cao.awa.kalmia.network.handler.inbound.AuthedRequestHandler;
import com.github.cao.awa.kalmia.network.packet.inbound.chat.session.listeners.SessionListenersUpdatePacket;
import com.github.cao.awa.kalmia.network.packet.inbound.chat.session.request.RequestGroupSessionPacket;
import com.github.cao.awa.kalmia.network.router.kalmia.RequestRouter;
import com.github.cao.awa.kalmia.session.Session;
import com.github.cao.awa.kalmia.session.SessionAccessibleData;
import com.github.cao.awa.kalmia.session.types.group.GroupSession;
import com.github.cao.awa.modmdo.annotation.platform.Server;

import java.util.List;
import java.util.stream.Collectors;

@Auto
@Server
@PluginRegister(name = "kalmia_core")
public class RequestGroupSessionHandler implements RequestGroupSessionEventHandler {
    @Auto
    @Server
    @Override
    public void handle(RequestRouter router, RequestGroupSessionPacket packet) {
        AuthedRequestHandler handler = packet.handler();

        long sessionId = Kalmia.SERVER.sessionManager()
                                      .add(new GroupSession(Kalmia.SERVER.sessionManager()
                                                                         .nextSeq(),
                                                            packet.name()
                                      ));

        // Update session data.
        List<Long> listeners = Kalmia.SERVER.userManager()
                                            .sessionListeners(handler.uid());

        listeners.add(sessionId);

        Kalmia.SERVER.userManager()
                     .sessionListeners(handler.uid(),
                                       listeners
                     );

        // Update accessible.
        Kalmia.SERVER.sessionManager()
                     .updateAccessible(
                             sessionId,
                             handler.uid(),
                             SessionAccessibleData :: accessibleChat
                     );

        List<Session> sessions = listeners.stream()
                                          .map(Kalmia.SERVER.sessionManager() :: session)
                                          .collect(Collectors.toList());

        router.send(new SessionListenersUpdatePacket(sessions));
    }
}
