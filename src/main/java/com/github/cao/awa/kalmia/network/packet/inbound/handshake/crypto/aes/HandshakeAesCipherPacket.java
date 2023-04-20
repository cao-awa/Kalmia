package com.github.cao.awa.kalmia.network.packet.inbound.handshake.crypto.aes;

import com.github.cao.awa.apricot.identifier.BytesRandomIdentifier;
import com.github.cao.awa.apricot.io.bytes.reader.BytesReader;
import com.github.cao.awa.apricot.util.digger.MessageDigger;
import com.github.cao.awa.apricot.util.encryption.Crypto;
import com.github.cao.awa.kalmia.annotation.network.unsolve.AutoSolvedPacket;
import com.github.cao.awa.kalmia.mathematic.Mathematics;
import com.github.cao.awa.kalmia.mathematic.base.Base256;
import com.github.cao.awa.kalmia.network.encode.crypto.symmetric.aes.AesCrypto;
import com.github.cao.awa.kalmia.network.handler.handshake.HandshakeHandler;
import com.github.cao.awa.kalmia.network.packet.ReadonlyPacket;
import com.github.cao.awa.kalmia.network.packet.request.handshake.crypto.aes.HandshakeAesCipherRequest;
import com.github.cao.awa.kalmia.network.packet.request.handshake.hello.ServerHelloRequest;
import com.github.cao.awa.kalmia.network.router.UnsolvedRequestRouter;
import com.github.cao.awa.kalmia.network.router.status.RequestStatus;
import com.github.cao.awa.modmdo.annotation.platform.Server;
import com.github.cao.awa.viburnum.util.bytes.BytesUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @see HandshakeAesCipherRequest
 */
@Server
@AutoSolvedPacket(2)
public class HandshakeAesCipherPacket extends ReadonlyPacket<HandshakeHandler> {
    private static final Logger LOGGER = LogManager.getLogger("HandshakeAesCipher");
    // Dev definition, the value always should be true
    private static final boolean SHOULD_SESSION_IV = true;
    private final byte[] cipher;

    public HandshakeAesCipherPacket(BytesReader reader) {
        this.cipher = reader.read(Base256.tagFromBuf(reader.read(2)));
    }

    @Override
    public void inbound(UnsolvedRequestRouter router, HandshakeHandler handler) {
        try {
            // Decrypt aes cipher.
            byte[] cipher = Crypto.rsaDecrypt(this.cipher,
                                              Crypto.decodeRsaPrikey(handler.getRsaPrikey())
            );

            // Back the cipher to client using the given cipher.
            // Let client verify the sha of decrypted text for check server cipher is same to self.
            byte[] testKey = cipher.clone();

            // Setup crypto and waiting for client login.
            router.setCrypto(new AesCrypto(cipher));
            router.setStatus(RequestStatus.AUTH);

            // Use the different initialization vector to anyone session.
            // For prevent the latent feature extraction.
            byte[] iv = router.encode(SHOULD_SESSION_IV ? BytesRandomIdentifier.create(16) : BytesUtil.EMPTY);

            // Send request, the sha should calculate in plain text as not ciphertext.
            router.send(new ServerHelloRequest(
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

            // Setup IV on server, can be empty(mean use default).
            router.setIv(iv);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
