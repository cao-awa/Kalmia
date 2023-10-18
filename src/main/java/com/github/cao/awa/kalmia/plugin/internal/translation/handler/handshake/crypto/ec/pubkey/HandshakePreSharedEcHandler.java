package com.github.cao.awa.kalmia.plugin.internal.translation.handler.handshake.crypto.ec.pubkey;

import com.github.cao.awa.apricot.annotation.auto.Auto;
import com.github.cao.awa.apricot.identifier.BytesRandomIdentifier;
import com.github.cao.awa.kalmia.annotation.plugin.PluginRegister;
import com.github.cao.awa.kalmia.env.KalmiaPreSharedCipher;
import com.github.cao.awa.kalmia.event.handler.network.inbound.handshake.crypto.ec.pubkey.HandshakePreSharedEcEventHandler;
import com.github.cao.awa.kalmia.network.encode.kalmiagram.crypto.asymmetric.ec.EcCrypto;
import com.github.cao.awa.kalmia.network.encode.kalmiagram.crypto.symmetric.aes.AesCrypto;
import com.github.cao.awa.kalmia.network.packet.inbound.handshake.crypto.aes.HandshakeAesCipherPacket;
import com.github.cao.awa.kalmia.network.packet.inbound.handshake.crypto.ec.pubkey.HandshakePreSharedEcPacket;
import com.github.cao.awa.kalmia.network.router.kalmia.RequestRouter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Auto
@PluginRegister(name = "kalmia_translation")
public class HandshakePreSharedEcHandler implements HandshakePreSharedEcEventHandler {
    private static final Logger LOGGER = LogManager.getLogger("PreSharedRsaHandler");
    private static final byte[] AES_CIPHER = BytesRandomIdentifier.create(32);

    @Auto
    @Override
    public void handle(RequestRouter router, HandshakePreSharedEcPacket packet) {
        if (! packet.cipherField()
                    .equals(KalmiaPreSharedCipher.expectCipherField)) {
            LOGGER.warn("The server sent cipher key is not same to client expected: " + packet.cipherField() + " (server) / " + KalmiaPreSharedCipher.expectCipherField + " (client)");
        } else {
            LOGGER.info("Server sent cipher key is: " + packet.cipherField());
        }
        router.setCrypto(new EcCrypto(KalmiaPreSharedCipher.pubkeyManager.get(packet.cipherField()),
                                      null
        ));
        router.send(new HandshakeAesCipherPacket(AES_CIPHER));
        router.setCrypto(new AesCrypto(AES_CIPHER));
    }
}
