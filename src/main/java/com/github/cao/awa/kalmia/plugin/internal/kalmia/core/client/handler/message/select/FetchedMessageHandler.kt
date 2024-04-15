package com.github.cao.awa.kalmia.plugin.internal.kalmia.core.client.handler.message.select;

import com.github.cao.awa.apricot.annotations.auto.Auto;
import com.github.cao.awa.kalmia.annotations.plugin.PluginRegister;
import com.github.cao.awa.kalmia.bootstrap.Kalmia;
import com.github.cao.awa.kalmia.event.kalmiagram.handler.network.inbound.message.select.FetchedMessageEventHandler;
import com.github.cao.awa.kalmia.network.packet.inbound.message.select.FetchedMessagePacket;
import com.github.cao.awa.kalmia.network.router.kalmia.RequestRouter;
import com.github.cao.awa.modmdo.annotation.platform.Client;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Auto
@Client
@PluginRegister(name = "kalmia_client")
class FetchedMessageHandler : FetchedMessageEventHandler {
    companion object {
        private val LOGGER: Logger = LogManager.getLogger("FetchedMessageHandler")
    }

    override fun handle(router: RequestRouter, packet: FetchedMessagePacket) {
        packet.messages().forEach { message -> Kalmia.CLIENT.messageManager.set(message.identity(), message) }
    }
}
