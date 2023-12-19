package com.github.cao.awa.kalmia.plugin.internal.kalmia.core.client.handler.message.send;

import com.github.cao.awa.apricot.annotations.auto.Auto;
import com.github.cao.awa.kalmia.annotations.plugin.PluginRegister;
import com.github.cao.awa.kalmia.bootstrap.Kalmia;
import com.github.cao.awa.kalmia.event.kalmiagram.handler.network.inbound.message.send.SentMessageEventHandler;
import com.github.cao.awa.kalmia.mathematic.Mathematics;
import com.github.cao.awa.kalmia.network.packet.inbound.message.send.SentMessagePacket;
import com.github.cao.awa.kalmia.network.router.kalmia.RequestRouter;
import com.github.cao.awa.modmdo.annotation.platform.Client;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Auto
@Client
@PluginRegister(name = "kalmia_client")
public class SentMessageHandler implements SentMessageEventHandler {
    private static final Logger LOGGER = LogManager.getLogger("SentMessageHandler");

    @Client
    @Override
    public void handle(RequestRouter router, SentMessagePacket packet) {
        LOGGER.info("""
                            --Sent message--
                            SEQ:{}
                            UID: {}
                            IDT: {}""",
                    packet.seq(),
                    packet.handler()
                          .accessIdentity(),
                    Mathematics.radix(packet.receipt(),
                                      36
                    )
        );

        Kalmia.CLIENT.messageManager()
                     .set(packet.sessionIdentity(),
                          packet.seq(),
                          packet.message()
                     );
    }
}
