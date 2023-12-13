package com.github.cao.awa.kalmia.plugin.internal.kalmia.core.client.handler.message.select;

import com.github.cao.awa.apricot.annotations.auto.Auto;
import com.github.cao.awa.kalmia.annotations.plugin.PluginRegister;
import com.github.cao.awa.kalmia.bootstrap.Kalmia;
import com.github.cao.awa.kalmia.event.kalmiagram.handler.network.inbound.message.select.SelectedMessageEventHandler;
import com.github.cao.awa.kalmia.message.Message;
import com.github.cao.awa.kalmia.message.manager.MessageManager;
import com.github.cao.awa.kalmia.network.packet.inbound.message.select.SelectedMessagePacket;
import com.github.cao.awa.kalmia.network.router.kalmia.RequestRouter;
import com.github.cao.awa.modmdo.annotation.platform.Client;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Auto
@Client
@PluginRegister(name = "kalmia_client")
public class SelectedMessageHandler implements SelectedMessageEventHandler {
    private static final Logger LOGGER = LogManager.getLogger("SelectedMessageHandler");

    @Auto
    @Client
    @Override
    public void handle(RequestRouter router, SelectedMessagePacket packet) {
        if (packet.sessionCurSeq() == 0) {
            return;
        }

        if (((packet.to() - packet.from()) + 1) != packet.messages()
                                                         .size()) {
            LOGGER.warn("Wrongly message packet");

            return;
        }

        MessageManager manager = Kalmia.CLIENT.messageManager();

        Message[] messages = packet.messages()
                                   .toArray(Message[] :: new);

        manager.seq(packet.sessionId(),
                    packet.sessionCurSeq()
        );

        long databaseIndex = packet.from();
        long to = packet.to();
        int arrayIndex = 0;
        for (; databaseIndex <= to; databaseIndex++, arrayIndex++) {
            Message msg = messages[arrayIndex];

            if (msg == null) {
                continue;
            }

            manager.set(
                    packet.sessionId(),
                    databaseIndex,
                    msg
            );
        }

//        LOGGER.info("----Test display----");
//
//        Kalmia.CLIENT.getMessages(packet.sessionId(),
//                                  packet.from(),
//                                  packet.to(),
//                                  false
//              )
//                     .forEach(message -> {
//                         LOGGER.info("---{}: {}---\n{}\n{}",
//                                     message.sessionId(),
//                                     message.seq(),
//                                     message.sourceContent(),
//                                     message.coverContent()
//                         );
//                     });
    }
}
