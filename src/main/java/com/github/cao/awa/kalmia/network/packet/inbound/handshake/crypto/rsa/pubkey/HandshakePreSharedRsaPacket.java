package com.github.cao.awa.kalmia.network.packet.inbound.handshake.crypto.rsa.pubkey;

import com.github.cao.awa.apricot.identifier.BytesRandomIdentifier;
import com.github.cao.awa.apricot.io.bytes.reader.BytesReader;
import com.github.cao.awa.kalmia.annotation.network.unsolve.AutoSolvedPacket;
import com.github.cao.awa.kalmia.env.KalmiaEnv;
import com.github.cao.awa.kalmia.network.encode.crypto.symmetric.aes.AesCrypto;
import com.github.cao.awa.kalmia.network.handler.handshake.HandshakeHandler;
import com.github.cao.awa.kalmia.network.packet.ReadonlyPacket;
import com.github.cao.awa.kalmia.network.packet.request.handshake.crypto.aes.HandshakeAesCipherRequest;
import com.github.cao.awa.kalmia.network.packet.request.handshake.crypto.rsa.pubkey.HandshakePreSharedRsaRequest;
import com.github.cao.awa.kalmia.network.router.RequestRouter;
import com.github.cao.awa.modmdo.annotation.platform.Client;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.nio.charset.StandardCharsets;

/**
 * @see HandshakePreSharedRsaRequest
 */
@Client
@AutoSolvedPacket(1)
public class HandshakePreSharedRsaPacket extends ReadonlyPacket<HandshakeHandler> {
    private static final Logger LOGGER = LogManager.getLogger("PreSharedRsaPacket");
    private static final byte[] AES_CIPHER = BytesRandomIdentifier.create(32);
    private final String cipherKey;

    public HandshakePreSharedRsaPacket(BytesReader reader) {
        this.cipherKey = new String(reader.all(),
                                    StandardCharsets.US_ASCII
        );
    }

    @Override
    public void inbound(RequestRouter router, HandshakeHandler handler) {
        if (! this.cipherKey.equals(KalmiaEnv.expectCipherKey)) {
            LOGGER.warn("The server sent cipher key is not same to client expected: " + this.cipherKey + " (server) / " + KalmiaEnv.expectCipherKey + " (client)");
        }
        router.send(new HandshakeAesCipherRequest(KalmiaEnv.DEFAULT_PRE_PUBKEY.get(this.cipherKey)
                                                                              .cipher(),
                                                  AES_CIPHER
        ));
        router.setCrypto(new AesCrypto(AES_CIPHER));
    }
}
