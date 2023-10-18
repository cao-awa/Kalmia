package com.github.cao.awa.kalmia.plugin.internal.translation.handler.message.send;

import com.github.cao.awa.apricot.annotation.auto.Auto;
import com.github.cao.awa.kalmia.annotation.plugin.PluginRegister;
import com.github.cao.awa.kalmia.event.handler.network.inbound.message.send.SentMessageEventHandler;
import com.github.cao.awa.kalmia.mathematic.Mathematics;
import com.github.cao.awa.kalmia.network.packet.inbound.message.send.SentMessagePacket;
import com.github.cao.awa.kalmia.network.router.kalmia.RequestRouter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Auto
@PluginRegister(name = "kalmia_translation")
public class SentMessageHandler implements SentMessageEventHandler {
    private static final Logger LOGGER = LogManager.getLogger("SentMessageHandler");

    @Override
    public void handle(RequestRouter router, SentMessagePacket packet) {
        LOGGER.info("""
                            --Sent message--
                            SEQ:{}
                            UID: {}
                            IDT: {}""",
                    packet.seq(),
                    packet.handler()
                          .uid(),
                    Mathematics.radix(packet.receipt(),
                                      36
                    )
        );
    }
}
