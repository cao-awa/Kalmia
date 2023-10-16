package com.github.cao.awa.kalmia.plugin.internal.kalmia.core.client.handler.message.send;

import com.github.cao.awa.apricot.annotation.auto.Auto;
import com.github.cao.awa.kalmia.annotation.plugin.PluginRegister;
import com.github.cao.awa.kalmia.event.handler.network.inbound.message.send.SendMessageRefusedEventHandler;
import com.github.cao.awa.kalmia.mathematic.Mathematics;
import com.github.cao.awa.kalmia.network.packet.inbound.message.send.SendMessageRefusedPacket;
import com.github.cao.awa.kalmia.network.router.RequestRouter;
import com.github.cao.awa.modmdo.annotation.platform.Client;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Auto
@Client
@PluginRegister(name = "kalmia_core")
public class SendMessageRefusedHandler implements SendMessageRefusedEventHandler {
    private static final Logger LOGGER = LogManager.getLogger("SendMessageRefusedHandler");

    @Override
    public void handle(RequestRouter router, SendMessageRefusedPacket packet) {
        LOGGER.info("#{} sending refused by server, reason: {}",
                    Mathematics.radix(packet.receipt(),
                                      36
                    ),
                    packet.reason()
        );
    }
}
