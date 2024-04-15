package com.github.cao.awa.kalmia.plugin.internal.kalmia.core.client.handler.message.delete

import com.github.cao.awa.apricot.annotations.auto.Auto
import com.github.cao.awa.kalmia.annotations.plugin.PluginRegister
import com.github.cao.awa.kalmia.event.kalmiagram.handler.network.inbound.message.delete.DeletedMessageEventHandler
import com.github.cao.awa.kalmia.network.packet.inbound.message.delete.DeletedMessagePacket
import com.github.cao.awa.kalmia.network.router.kalmia.RequestRouter
import com.github.cao.awa.modmdo.annotation.platform.Client

@Auto
@Client
@PluginRegister(name = "kalmia_client")
class DeletedMessageHandler : DeletedMessageEventHandler {
    @Auto
    @Client
    override fun handle(router: RequestRouter, packet: DeletedMessagePacket) {
        println("---Message deleted---")
        println("UID: " + router.accessIdentity())
        println("SID: " + packet.sessionIdentity())
        println("SEQ: " + packet.seq())
    }
}
