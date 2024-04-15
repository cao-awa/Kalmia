package com.github.cao.awa.kalmia.plugin.internal.kalmia.core.client.handler.message.send

import com.github.cao.awa.apricot.annotations.auto.Auto
import com.github.cao.awa.kalmia.annotations.plugin.PluginRegister
import com.github.cao.awa.kalmia.event.kalmiagram.handler.network.inbound.message.send.SendMessageRefusedEventHandler
import com.github.cao.awa.kalmia.mathematic.Mathematics
import com.github.cao.awa.kalmia.network.packet.inbound.message.send.SendMessageRefusedPacket
import com.github.cao.awa.kalmia.network.router.kalmia.RequestRouter
import com.github.cao.awa.modmdo.annotation.platform.Client
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger

@Auto
@Client
@PluginRegister(name = "kalmia_client")
class SendMessageRefusedHandler : SendMessageRefusedEventHandler {
    companion object {
        private val LOGGER: Logger = LogManager.getLogger("SendMessageRefusedHandler")
    }

    override fun handle(router: RequestRouter, packet: SendMessageRefusedPacket) {
        LOGGER.info("#${Mathematics.radix(packet.receipt(), 36)} sending refused by server, reason: ${packet.reason()}")
    }
}
