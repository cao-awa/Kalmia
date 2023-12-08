package com.github.cao.awa.kalmia.plugin.internal.kalmia.core.client.handler.message.select;

import com.github.cao.awa.apricot.annotations.auto.Auto;
import com.github.cao.awa.kalmia.annotations.plugin.PluginRegister;
import com.github.cao.awa.kalmia.bootstrap.Kalmia;
import com.github.cao.awa.kalmia.env.KalmiaEnv;
import com.github.cao.awa.kalmia.event.kalmiagram.handler.network.inbound.message.select.SelectedMessageEventHandler;
import com.github.cao.awa.kalmia.mathematic.Mathematics;
import com.github.cao.awa.kalmia.message.Message;
import com.github.cao.awa.kalmia.message.deleted.DeletedMessage;
import com.github.cao.awa.kalmia.message.manager.MessageManager;
import com.github.cao.awa.kalmia.message.plains.PlainsMessage;
import com.github.cao.awa.kalmia.message.unknown.UnknownMessage;
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

            if (msg instanceof DeletedMessage deletedMessage) {
                LOGGER.info("Received deleted message at seq {}, id {}, sender {}, is: {}",
                            databaseIndex,
                            Mathematics.radix(msg.identity()
                                                 .toBytes(),
                                              36
                            ),
                            deletedMessage.sender(),
                            deletedMessage.digest()
                                          .value36()
                );
            } else if (msg instanceof PlainsMessage plainsMessage) {
                LOGGER.info("Received plains message at seq {}, id {}, sender {}, is: {}",
                            databaseIndex,
                            Mathematics.radix(msg.identity()
                                                 .toBytes(),
                                              36
                            ),
                            plainsMessage.sender(),
                            plainsMessage.msg()
                );
            } else if (msg instanceof UnknownMessage unknownMessage) {
                LOGGER.info("Received unknown message at seq {}, id {}, is: {}",
                            databaseIndex,
                            Mathematics.radix(msg.identity()
                                                 .toBytes(),
                                              36
                            ),
                            unknownMessage.digest()
                                          .value36()
                );
            }
        }

        KalmiaEnv.awaitManager.notice(packet.receipt());

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
