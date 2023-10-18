package com.github.cao.awa.kalmia.plugin.internal.kalmia.core.server.handler.handshake.crypto.aes;

import com.github.cao.awa.apricot.annotation.auto.Auto;
import com.github.cao.awa.apricot.identifier.BytesRandomIdentifier;
import com.github.cao.awa.apricot.util.digger.MessageDigger;
import com.github.cao.awa.kalmia.annotation.plugin.PluginRegister;
import com.github.cao.awa.kalmia.event.kalmiagram.handler.network.inbound.handshake.crypto.aes.HandshakeAesCipherEventHandler;
import com.github.cao.awa.kalmia.mathematic.Mathematics;
import com.github.cao.awa.kalmia.network.encode.kalmiagram.crypto.symmetric.aes.AesCrypto;
import com.github.cao.awa.kalmia.network.packet.inbound.handshake.crypto.aes.HandshakeAesCipherPacket;
import com.github.cao.awa.kalmia.network.packet.inbound.handshake.hello.server.ServerHelloPacket;
import com.github.cao.awa.kalmia.network.router.kalmia.RequestRouter;
import com.github.cao.awa.kalmia.network.router.kalmia.status.RequestState;
import com.github.cao.awa.modmdo.annotation.platform.Server;
import com.github.cao.awa.viburnum.util.bytes.BytesUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Auto
@Server
@PluginRegister(name = "kalmia_core")
public class HandshakeAesCipherHandler implements HandshakeAesCipherEventHandler {
    private static final Logger LOGGER = LogManager.getLogger("HandshakeAesCipherHandler");
    // Dev definition, the value always should be true
    private static final boolean SHOULD_SESSION_IV = true;

    @Auto
    @Server
    @Override
    public void handle(RequestRouter router, HandshakeAesCipherPacket packet) {
        try {
            // Back the cipher to client using the given cipher.
            // Let client verify the sha of decrypted text for check server cipher is same to self.
            byte[] testKey = packet.cipher()
                                   .clone();

            // Setup crypto and waiting for client login.
            router.setCrypto(new AesCrypto(packet.cipher()));
            router.setStates(RequestState.AUTH);

            // Use the different initialization vector to anyone session.
            // For prevent the latent feature extraction.
            byte[] iv = router.encode(SHOULD_SESSION_IV ? BytesRandomIdentifier.create(16) : BytesUtil.EMPTY);

            // Send request, the sha should calculate in plain text as not ciphertext.
            router.send(new ServerHelloPacket(
                    // Crypto encoded test key, use to verify.
                    router.encode(testKey),
                    // No encoded SHA-512 value, use to verify.
                    Mathematics.toBytes(MessageDigger.digest(testKey,
                                                             MessageDigger.Sha3.SHA_512
                                        ),
                                        16
                    ),
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
