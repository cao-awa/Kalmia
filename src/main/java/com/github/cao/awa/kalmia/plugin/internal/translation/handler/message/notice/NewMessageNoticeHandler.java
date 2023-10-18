package com.github.cao.awa.kalmia.plugin.internal.translation.handler.message.notice;

import com.github.cao.awa.apricot.annotation.auto.Auto;
import com.github.cao.awa.kalmia.annotation.plugin.PluginRegister;
import com.github.cao.awa.kalmia.event.handler.network.inbound.message.notice.NewMessageNoticeEventHandler;
import com.github.cao.awa.kalmia.message.DeletedMessage;
import com.github.cao.awa.kalmia.message.Message;
import com.github.cao.awa.kalmia.message.plains.PlainsMessage;
import com.github.cao.awa.kalmia.message.unknown.UnknownMessage;
import com.github.cao.awa.kalmia.network.packet.inbound.message.notice.NewMessageNoticePacket;
import com.github.cao.awa.kalmia.network.router.kalmia.RequestRouter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Arrays;

@Auto
@PluginRegister(name = "kalmia_translation")
public class NewMessageNoticeHandler implements NewMessageNoticeEventHandler {
    private static final Logger LOGGER = LogManager.getLogger("NewMessageNoticeHandler");

    @Override
    public void handle(RequestRouter router, NewMessageNoticePacket packet) {
        Message message = packet.message();
        if (message instanceof PlainsMessage plain) {
            LOGGER.info("New message from {}: {}",
                        packet.message()
                              .getSender(),
                        plain.msg()
            );
        } else if (message instanceof DeletedMessage deleted) {
            LOGGER.info("New message from {}: {}",
                        packet.message()
                              .getSender(),
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
