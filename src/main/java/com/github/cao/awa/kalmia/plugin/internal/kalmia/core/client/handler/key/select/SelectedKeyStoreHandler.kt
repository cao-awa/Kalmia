package com.github.cao.awa.kalmia.plugin.internal.kalmia.core.client.handler.key.select

import com.github.cao.awa.apricot.annotations.auto.Auto
import com.github.cao.awa.kalmia.annotations.plugin.PluginRegister
import com.github.cao.awa.kalmia.bootstrap.Kalmia
import com.github.cao.awa.kalmia.event.kalmiagram.handler.network.inbound.key.select.SelectedKeyStoreEventHandler
import com.github.cao.awa.kalmia.network.packet.inbound.key.select.SelectedKeyStorePacket
import com.github.cao.awa.kalmia.network.router.kalmia.RequestRouter
import com.github.cao.awa.modmdo.annotation.platform.Client

@Auto
@Client
@PluginRegister(name = "kalmia_client")
class SelectedKeyStoreHandler : SelectedKeyStoreEventHandler {
    @Auto
    @Client
    override fun handle(router: RequestRouter, packet: SelectedKeyStorePacket) {
        packet.keys().forEach { (id, store) ->
            println("Selected public key: $id ${store.type()}")
            Kalmia.CLIENT.keypairManager.set(id, store)
        }
    }
}
