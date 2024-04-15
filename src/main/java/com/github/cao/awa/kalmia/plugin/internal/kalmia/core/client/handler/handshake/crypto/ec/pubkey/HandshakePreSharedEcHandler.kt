package com.github.cao.awa.kalmia.plugin.internal.kalmia.core.client.handler.handshake.crypto.ec.pubkey

import com.github.cao.awa.apricot.annotations.auto.Auto
import com.github.cao.awa.apricot.identifier.BytesRandomIdentifier
import com.github.cao.awa.kalmia.annotations.plugin.PluginRegister
import com.github.cao.awa.kalmia.annotations.threading.ForceMainThread
import com.github.cao.awa.kalmia.env.KalmiaPreSharedCipher
import com.github.cao.awa.kalmia.event.kalmiagram.handler.network.inbound.handshake.crypto.ec.pubkey.HandshakePreSharedEcEventHandler
import com.github.cao.awa.kalmia.network.encode.kalmiagram.crypto.asymmetric.ec.EcCrypto
import com.github.cao.awa.kalmia.network.encode.kalmiagram.crypto.symmetric.aes.AesCrypto
import com.github.cao.awa.kalmia.network.packet.inbound.handshake.crypto.aes.HandshakeAesCipherPacket
import com.github.cao.awa.kalmia.network.packet.inbound.handshake.crypto.ec.pubkey.HandshakePreSharedEcPacket
import com.github.cao.awa.kalmia.network.router.kalmia.RequestRouter
import com.github.cao.awa.modmdo.annotation.platform.Client
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger

@Auto
@Client
@PluginRegister(name = "kalmia_client")
@ForceMainThread
class HandshakePreSharedEcHandler : HandshakePreSharedEcEventHandler {
    companion object {
        private val LOGGER: Logger = LogManager.getLogger("PreSharedRsaHandler")
        private val AES_CIPHER: ByteArray = BytesRandomIdentifier.create(32)
    }

    @Auto
    @Client
    override fun handle(router: RequestRouter, packet: HandshakePreSharedEcPacket) {
        if (!packet.cipherField().equals(KalmiaPreSharedCipher.expectCipherField)) {
            LOGGER.warn("The server sent cipher key is not same to client expected: " + packet.cipherField() + " (server) / " + KalmiaPreSharedCipher.expectCipherField + " (client)")
        } else {
            LOGGER.info("Server sent cipher key is: " + packet.cipherField())
        }
        router.setCrypto(EcCrypto(KalmiaPreSharedCipher.pubkeyManager.get(packet.cipherField()), null))
        router.sendImmediately(HandshakeAesCipherPacket(AES_CIPHER))
        router.setCrypto(AesCrypto(AES_CIPHER))
    }
}
