package com.github.cao.awa.kalmia.plugin.internal.kalmia.core.client.handler.message.notice

import com.github.cao.awa.apricot.annotations.auto.Auto
import com.github.cao.awa.kalmia.annotations.plugin.PluginRegister
import com.github.cao.awa.kalmia.bootstrap.Kalmia
import com.github.cao.awa.kalmia.event.kalmiagram.handler.network.inbound.message.notice.NewMessageNoticeEventHandler
import com.github.cao.awa.kalmia.message.Message
import com.github.cao.awa.kalmia.message.deleted.DeletedMessage
import com.github.cao.awa.kalmia.message.unknown.UnknownMessage
import com.github.cao.awa.kalmia.network.packet.inbound.message.notice.NewMessageNoticePacket
import com.github.cao.awa.kalmia.network.router.kalmia.RequestRouter
import com.github.cao.awa.modmdo.annotation.platform.Client
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger

@Auto
@Client
@PluginRegister(name = "kalmia_client")
class NewMessageNoticeHandler : NewMessageNoticeEventHandler {
    companion object {
        private val LOGGER: Logger = LogManager.getLogger("NewMessageNoticeHandler")
    }

    @Client
    override fun handle(router: RequestRouter, packet: NewMessageNoticePacket) {
        val message: Message = packet.message()

        Kalmia.CLIENT.messageManager.set(packet.sessionIdentity(), packet.seq(), packet.message())
        Kalmia.CLIENT.messageManager.seq(packet.sessionIdentity(), packet.seq())

        if (message is DeletedMessage) {
            LOGGER.info("New message from ${packet.message().sender()}: ${message.digest().value36()}")
        } else if (message is UnknownMessage) {
            LOGGER.info("New message from unknown: ${message.details().contentToString()}")
        }
    }
}
