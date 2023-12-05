package com.github.cao.awa.kalmia.plugin.internal.kalmia.core.client.handler.message.notice;

import com.github.cao.awa.apricot.annotations.auto.Auto;
import com.github.cao.awa.kalmia.annotations.plugin.PluginRegister;
import com.github.cao.awa.kalmia.bootstrap.Kalmia;
import com.github.cao.awa.kalmia.event.kalmiagram.handler.network.inbound.message.notice.NewMessageNoticeEventHandler;
import com.github.cao.awa.kalmia.message.Message;
import com.github.cao.awa.kalmia.message.deleted.DeletedMessage;
import com.github.cao.awa.kalmia.message.plains.PlainsMessage;
import com.github.cao.awa.kalmia.message.unknown.UnknownMessage;
import com.github.cao.awa.kalmia.network.packet.inbound.message.notice.NewMessageNoticePacket;
import com.github.cao.awa.kalmia.network.router.kalmia.RequestRouter;
import com.github.cao.awa.modmdo.annotation.platform.Client;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Arrays;

@Auto
@Client
@PluginRegister(name = "kalmia_client")
public class NewMessageNoticeHandler implements NewMessageNoticeEventHandler {
    private static final Logger LOGGER = LogManager.getLogger("NewMessageNoticeHandler");

    @Client
    @Override
    public void handle(RequestRouter router, NewMessageNoticePacket packet) {
        Message message = packet.message();

        Kalmia.CLIENT.messageManager()
                     .set(
                             packet.sessionId(),
                             packet.seq(),
                             packet.message()
                     );

        Kalmia.CLIENT.messageManager()
                     .curSeq(packet.sessionId(),
                             packet.seq()
                     );

        if (message instanceof PlainsMessage plain) {
            LOGGER.info("New message from {}: {}",
                        packet.message()
                              .sender(),
                        plain.msg()
            );
        } else if (message instanceof DeletedMessage deleted) {
            LOGGER.info("New message from {}: {}",
                        packet.message()
                              .sender(),
                        deleted.digest()
                               .value36()
            );
        } else if (message instanceof UnknownMessage unknown) {
            LOGGER.info("New message from unknown: {}",
                        Arrays.toString(unknown.details())
            );
        }
    }
}
