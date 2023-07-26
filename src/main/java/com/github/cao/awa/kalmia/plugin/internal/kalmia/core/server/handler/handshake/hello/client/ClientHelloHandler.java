package com.github.cao.awa.kalmia.plugin.internal.kalmia.core.server.handler.handshake.hello.client;

import com.github.cao.awa.apricot.annotation.auto.Auto;
import com.github.cao.awa.kalmia.annotation.auto.event.AutoHandler;
import com.github.cao.awa.kalmia.annotation.plugin.PluginRegister;
import com.github.cao.awa.kalmia.constant.KalmiaConstant;
import com.github.cao.awa.kalmia.env.KalmiaPreSharedKey;
import com.github.cao.awa.kalmia.event.handler.network.inbound.handshake.hello.client.ClientHelloEventHandler;
import com.github.cao.awa.kalmia.event.network.inbound.handshake.hello.client.ClientHelloEvent;
import com.github.cao.awa.kalmia.network.encode.crypto.asymmetric.ac.EcCrypto;
import com.github.cao.awa.kalmia.network.packet.inbound.disconnet.DisconnectPacket;
import com.github.cao.awa.kalmia.network.packet.inbound.handshake.crypto.ec.pubkey.HandshakePreSharedEcPacket;
import com.github.cao.awa.kalmia.network.packet.inbound.handshake.hello.client.ClientHelloPacket;
import com.github.cao.awa.kalmia.network.router.RequestRouter;
import com.github.cao.awa.modmdo.annotation.platform.Server;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Auto
@Server
@PluginRegister(name = "kalmia_core")
@AutoHandler(ClientHelloEvent.class)
public class ClientHelloHandler extends ClientHelloEventHandler {
    private static final Logger LOGGER = LogManager.getLogger("ClientHelloHandler");

    @Auto
    @Server
    public void handle(RequestRouter router, ClientHelloPacket packet) {
        LOGGER.info("Client Hello???");
        LOGGER.info("Client using major protocol " + packet.majorProtocol()
                                                           .name() + " version " + packet.majorProtocol()
                                                                                         .version() + " by client: " + packet.clientVersion());
        LOGGER.info("Client expected pre shared cipher key: " + packet.expectCipherField());
        if (! packet.majorProtocol()
                    .forceUse() && packet.majorProtocol()
                                         .compatible() > KalmiaConstant.STANDARD_REQUEST_PROTOCOL.version()) {
            LOGGER.warn("The client protocol is not compatible to server, unable to handshake");

            router.send(new DisconnectPacket("Not compatible protocol version"));

            router.disconnect();
            return;
        }

        String usingCipherKey = KalmiaPreSharedKey.prikeyManager.has(packet.expectCipherField()) ? packet.expectCipherField() : KalmiaPreSharedKey.defaultCipherKey;
        router.setCrypto(
                new EcCrypto(
                        // Server side only do decrypt in this step, so do not need public key.
                        null,
                        // Just use the private key to decrypt client data.
                        KalmiaPreSharedKey.prikeyManager.get(usingCipherKey)
                ));
        // Should tell client what field server are used.
        router.send(new HandshakePreSharedEcPacket(usingCipherKey));
    }
}
