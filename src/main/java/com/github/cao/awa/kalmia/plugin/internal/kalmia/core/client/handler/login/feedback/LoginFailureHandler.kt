package com.github.cao.awa.kalmia.plugin.internal.kalmia.core.client.handler.login.feedback

import com.github.cao.awa.apricot.annotations.auto.Auto
import com.github.cao.awa.kalmia.annotations.plugin.PluginRegister
import com.github.cao.awa.kalmia.event.kalmiagram.handler.network.inbound.login.feedback.LoginFailureEventHandler
import com.github.cao.awa.kalmia.mathematic.Mathematics
import com.github.cao.awa.kalmia.network.packet.inbound.login.feedback.LoginFailurePacket
import com.github.cao.awa.kalmia.network.router.kalmia.RequestRouter
import com.github.cao.awa.modmdo.annotation.platform.Client

@Auto
@Client
@PluginRegister(name = "kalmia_client")
class LoginFailureHandler : LoginFailureEventHandler {
    @Auto
    @Client
    override fun handle(router: RequestRouter, packet: LoginFailurePacket) {
        println("---Login failed---")
        println("UID: " + packet.accessIdentity())
        println(Mathematics.radix(packet.receipt(), 36))
    }
}
