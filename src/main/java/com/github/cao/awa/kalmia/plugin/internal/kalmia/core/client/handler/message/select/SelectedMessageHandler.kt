package com.github.cao.awa.kalmia.plugin.internal.kalmia.core.client.handler.message.select

import com.github.cao.awa.apricot.annotations.auto.Auto
import com.github.cao.awa.kalmia.annotations.plugin.PluginRegister
import com.github.cao.awa.kalmia.bootstrap.Kalmia
import com.github.cao.awa.kalmia.event.kalmiagram.handler.network.inbound.message.select.SelectedMessageEventHandler
import com.github.cao.awa.kalmia.message.Message
import com.github.cao.awa.kalmia.message.manager.MessageManager
import com.github.cao.awa.kalmia.network.packet.inbound.message.select.SelectedMessagePacket
import com.github.cao.awa.kalmia.network.router.kalmia.RequestRouter
import com.github.cao.awa.modmdo.annotation.platform.Client
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger

@Auto
@Client
@PluginRegister(name = "kalmia_client")
class SelectedMessageHandler : SelectedMessageEventHandler {
    companion object {
        private val LOGGER: Logger = LogManager.getLogger("SelectedMessageHandler")
    }

    @Auto
    @Client
    override fun handle(router: RequestRouter, packet: SelectedMessagePacket) {
        if (packet.sessionCurSeq().toInt() == -1 || packet.to().toInt() == -1) {
            LOGGER.info("The session does not have any message")
            return
        }

        if (((packet.to() - packet.from()) + 1).toInt() != packet.messages().size) {
            LOGGER.warn("Wrongly message packet")

            println(packet.messages());
            println("${packet.from()}:${packet.to()}")

            return
        }

        val manager: MessageManager = Kalmia.CLIENT.messageManager

        // TODO
        val messages: List<Message> = packet.messages()

        manager.seq(packet.sessionIdentity(), packet.sessionCurSeq())

        var databaseIndex: Long = packet.from()
        val to: Long = packet.to()
        var arrayIndex = 0
        while (databaseIndex <= to) {
            val msg: Message = messages[arrayIndex]

            manager.set(packet.sessionIdentity(), databaseIndex, msg)

            databaseIndex++
            arrayIndex++
        }

        LOGGER.info("----Test display----")

        Kalmia.CLIENT.getMessages(packet.sessionIdentity(), packet.from(), packet.to(), false).forEach { message ->
            LOGGER.info("{}: \n{}", message.identity(), message.display().coverContent())
        }
    }
}
