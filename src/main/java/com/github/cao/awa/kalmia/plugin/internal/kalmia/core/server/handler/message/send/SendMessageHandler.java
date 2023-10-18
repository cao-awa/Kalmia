package com.github.cao.awa.kalmia.plugin.internal.kalmia.core.server.handler.message.send;

import com.github.cao.awa.apricot.annotation.auto.Auto;
import com.github.cao.awa.kalmia.annotation.plugin.PluginRegister;
import com.github.cao.awa.kalmia.bootstrap.Kalmia;
import com.github.cao.awa.kalmia.event.handler.network.inbound.message.send.SendMessageEventHandler;
import com.github.cao.awa.kalmia.mathematic.Mathematics;
import com.github.cao.awa.kalmia.network.packet.inbound.message.notice.NewMessageNoticePacket;
import com.github.cao.awa.kalmia.network.packet.inbound.message.send.SendMessagePacket;
import com.github.cao.awa.kalmia.network.packet.inbound.message.send.SendMessageRefusedPacket;
import com.github.cao.awa.kalmia.network.packet.inbound.message.send.SentMessagePacket;
import com.github.cao.awa.kalmia.network.router.kalmia.RequestRouter;
import com.github.cao.awa.kalmia.session.duet.DuetSession;
import com.github.cao.awa.modmdo.annotation.platform.Server;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

@Auto
@Server
@PluginRegister(name = "kalmia_core")
public class SendMessageHandler implements SendMessageEventHandler {
    private static final Logger LOGGER = LogManager.getLogger("SendMessageHandler");

    @Server
    @Override
    public void handle(RequestRouter router, SendMessagePacket packet) {
        LOGGER.info("""
                            --Send message--
                            UID: {}
                            IDT: {}
                            SID: {}
                            MSG:{}""",
                    packet.handler()
                          .uid(),
                    Mathematics.radix(packet.receipt(),
                                      36
                    ),
                    packet.sessionId(),
                    packet.msg()
        );

        DuetSession session = (DuetSession) Kalmia.SERVER.sessionManager()
                                                         .session(packet.sessionId());

        boolean accessible = false;

        if (session != null) {
            accessible = session.accessible(packet.handler()
                                                  .uid());
        }

        if (accessible) {
            long seq = Kalmia.SERVER.messageManager()
                                    .send(
                                            packet.sessionId(),
                                            packet.msg()
                                    );

            List<RequestRouter> targetRouters = Kalmia.SERVER.getRouter(session.opposite(packet.handler()
                                                                                               .uid()));

            if (targetRouters != null) {
                targetRouters.forEach(targetRouter -> {
                    targetRouter.send(
                            new NewMessageNoticePacket(packet.sessionId(),
                                                       seq,
                                                       packet.msg()
                            ));
                });
            }

            // Response to client the seq.
            router.send(new SentMessagePacket(seq,
                                              packet.receipt()
            ));
        } else {
            // Unable to access the session.
            router.send(new SendMessageRefusedPacket("Session unable to access",
                                                     packet.receipt()
            ));
            LOGGER.warn("The session {} is not accessible",
                        packet.sessionId()
            );
        }
    }
}
