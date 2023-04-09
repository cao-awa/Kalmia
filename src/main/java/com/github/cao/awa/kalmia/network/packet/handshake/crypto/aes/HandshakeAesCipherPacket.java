package com.github.cao.awa.kalmia.network.packet.handshake.crypto.aes;

import com.github.cao.awa.apricot.identifier.BytesRandomIdentifier;
import com.github.cao.awa.apricot.io.bytes.reader.BytesReader;
import com.github.cao.awa.apricot.util.digger.MessageDigger;
import com.github.cao.awa.apricot.util.encryption.Crypto;
import com.github.cao.awa.kalmia.mathematic.Mathematics;
import com.github.cao.awa.kalmia.mathematic.base.Base256;
import com.github.cao.awa.kalmia.network.encode.crypto.symmetric.aes.AesCrypto;
import com.github.cao.awa.kalmia.network.handler.PacketHandler;
import com.github.cao.awa.kalmia.network.handler.handshake.HandshakeHandler;
import com.github.cao.awa.kalmia.network.packet.ReadonlyPacket;
import com.github.cao.awa.kalmia.network.packet.request.handshake.crypto.aes.HandshakeAesCipherRequest;
import com.github.cao.awa.kalmia.network.packet.request.handshake.hello.ServerHelloRequest;
import com.github.cao.awa.kalmia.network.packet.unsolve.handshake.crypto.aes.UnsolvedHandshakeAesCipherPacket;
import com.github.cao.awa.kalmia.network.router.UnsolvedRequestRouter;
import com.github.cao.awa.modmdo.annotation.platform.Server;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Arrays;

/**
 * @see HandshakeAesCipherRequest
 * @see UnsolvedHandshakeAesCipherPacket
 */
@Server
public class HandshakeAesCipherPacket extends ReadonlyPacket {
    private static final Logger LOGGER = LogManager.getLogger("HandshakeAesCipher");
    private final byte[] cipher;

    public HandshakeAesCipherPacket(byte[] cipher) {
        this.cipher = cipher;
    }

    public static HandshakeAesCipherPacket create(BytesReader reader) {
        int length = Base256.tagFromBuf(reader.read(2));
        return new HandshakeAesCipherPacket(reader.read(length));
    }

    @Override
    public void inbound(UnsolvedRequestRouter router, PacketHandler<?> handler) {
        try {
            // Decrypt aes cipher.
            byte[] cipher = Crypto.rsaDecrypt(this.cipher,
                                              Crypto.decodeRsaPrikey(((HandshakeHandler) handler).getRsaPrikey())
            );

            // Create a random identifier for cipher testing.
            // Let client verify the sha of decrypted text for check server cipher is same to self.
            byte[] testKey = BytesRandomIdentifier.create(16);

            router.setCrypto(new AesCrypto(cipher));

            // Send request, the sha should calculate in plain text as not ciphertext.
            router.send(new ServerHelloRequest(router.encode(testKey),
                                               Mathematics.toBytes(MessageDigger.digest(testKey,
                                                                                        MessageDigger.Sha3.SHA_512
                                                                   ),
                                                                   16
                                               )
            ));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
