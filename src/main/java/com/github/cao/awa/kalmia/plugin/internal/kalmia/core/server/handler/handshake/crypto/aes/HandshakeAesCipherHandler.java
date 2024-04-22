package com.github.cao.awa.kalmia.plugin.internal.kalmia.core.server.handler.handshake.crypto.aes;

import com.github.cao.awa.apricot.annotations.auto.Auto;
import com.github.cao.awa.apricot.identifier.BytesRandomIdentifier;
import com.github.cao.awa.kalmia.annotations.plugin.PluginRegister;
import com.github.cao.awa.kalmia.bootstrap.Kalmia;
import com.github.cao.awa.kalmia.constant.KalmiaConstant;
import com.github.cao.awa.kalmia.event.kalmiagram.handler.network.inbound.handshake.crypto.aes.HandshakeAesCipherEventHandler;
import com.github.cao.awa.kalmia.network.encode.kalmiagram.crypto.symmetric.aes.AesCrypto;
import com.github.cao.awa.kalmia.network.packet.inbound.handshake.crypto.aes.HandshakeAesCipherPacket;
import com.github.cao.awa.kalmia.network.packet.inbound.handshake.hello.server.ServerHelloPacket;
import com.github.cao.awa.kalmia.network.router.kalmia.RequestRouter;
import com.github.cao.awa.kalmia.network.router.status.RequestState;
import com.github.cao.awa.modmdo.annotation.platform.Server;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Auto
@Server
@PluginRegister(name = "kalmia_core")
public class HandshakeAesCipherHandler implements HandshakeAesCipherEventHandler {
    private static final Logger LOGGER = LogManager.getLogger("HandshakeAesCipherHandler");

    @Auto
    @Server
    @Override
    public void handle(RequestRouter router, HandshakeAesCipherPacket packet) {
        try {
            // Setup crypto and waiting for client login.
            router.setCrypto(new AesCrypto(packet.cipher()));
            router.setStates(RequestState.AUTH);

            // Use the different initialization vector to anyone session.
            // For prevent the latent feature extraction.
            byte[] iv = router.encode(BytesRandomIdentifier.create(16));

            // Send server hello.
            router.send(new ServerHelloPacket(
                    Kalmia.SERVER.serverBootstrapConfig.get().name.get(),
                    KalmiaConstant.BUILD_NAME,
                    // Crypto encoded IV, use to sync server session IV.
                    iv
            ));

            // Setup IV on server, the IV can be empty (mean use default IV).
            router.setIv(iv);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
