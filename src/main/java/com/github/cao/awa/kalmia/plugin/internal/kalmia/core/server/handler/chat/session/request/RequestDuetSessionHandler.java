package com.github.cao.awa.kalmia.plugin.internal.kalmia.core.server.handler.chat.session.request;

import com.github.cao.awa.apricot.annotations.auto.Auto;
import com.github.cao.awa.kalmia.annotations.plugin.PluginRegister;
import com.github.cao.awa.kalmia.bootstrap.Kalmia;
import com.github.cao.awa.kalmia.event.kalmiagram.handler.network.inbound.chat.session.request.RequestDuetSessionEventHandler;
import com.github.cao.awa.kalmia.network.handler.inbound.AuthedRequestHandler;
import com.github.cao.awa.kalmia.network.packet.inbound.chat.session.in.ChatInSessionPacket;
import com.github.cao.awa.kalmia.network.packet.inbound.chat.session.request.RequestDuetSessionPacket;
import com.github.cao.awa.kalmia.network.router.kalmia.RequestRouter;
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
        long targetUid = packet.targetUid();
        AuthedRequestHandler handler = packet.handler();

        long sessionId = Kalmia.SERVER.userManager()
                                      .session(handler.uid(),
                                               targetUid
                                      );
        if (sessionId == - 1) {
            sessionId = Kalmia.SERVER.sessionManager()
                                     .add(new DuetSession(Kalmia.SERVER.sessionManager()
                                                                       .nextSeq(),
                                                          handler.uid(),
                                                          targetUid
                                     ));
            Kalmia.SERVER.userManager()
                         .session(handler.uid(),
                                  targetUid,
                                  sessionId
                         );
        }

        router.send(new ChatInSessionPacket(targetUid,
                                            sessionId
        ));
    }
}
