package com.github.cao.awa.kalmia.plugin.internal.kalmia.core.server.handler.message.send;

import com.github.cao.awa.apricot.annotations.auto.Auto;
import com.github.cao.awa.kalmia.annotations.plugin.PluginRegister;
import com.github.cao.awa.kalmia.bootstrap.Kalmia;
import com.github.cao.awa.kalmia.event.kalmiagram.handler.network.inbound.message.send.SendMessageEventHandler;
import com.github.cao.awa.kalmia.mathematic.Mathematics;
import com.github.cao.awa.kalmia.message.Message;
import com.github.cao.awa.kalmia.network.packet.inbound.message.send.SendMessagePacket;
import com.github.cao.awa.kalmia.network.packet.inbound.message.send.SendMessageRefusedPacket;
import com.github.cao.awa.kalmia.network.packet.inbound.message.send.SentMessagePacket;
import com.github.cao.awa.kalmia.network.router.kalmia.RequestRouter;
import com.github.cao.awa.kalmia.session.Session;
import com.github.cao.awa.modmdo.annotation.platform.Server;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

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
                    packet.message()
        );

        Session session = Kalmia.SERVER.sessionManager()
                                       .session(packet.sessionId());

        boolean accessible = false;

        if (session != null) {
            accessible = session.accessible(packet.handler()
                                                  .uid());
        }

        if (accessible) {
            Message msg = packet.message();

            if (msg.sender() != router.uid()) {
                router.send(new SendMessageRefusedPacket("message.send.refused.wrong.sender").receipt(packet.receipt()));
                return;
            }

            long seq = Kalmia.SERVER.messageManager()
                                    .send(
                                            packet.sessionId(),
                                            msg
                                    );

            // Response to client the seq.
            router.send(new SentMessagePacket(seq).receipt(packet.receipt()));

            LOGGER.info("Sent message: {}",
                        Mathematics.radix(packet.receipt(),
                                          36
                        )
            );
        } else {
            // Unable to access the session.
            router.send(new SendMessageRefusedPacket("message.send.refused.session.unable.access").receipt(packet.receipt()));
            LOGGER.warn("The session {} is not accessible",
                        packet.sessionId()
            );
        }
    }
}
