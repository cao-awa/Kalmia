package com.github.cao.awa.kalmia.network.packet.dual.handshake.crypto.ec.pubkey;

import com.github.cao.awa.apricot.identifier.BytesRandomIdentifier;
import com.github.cao.awa.apricot.io.bytes.reader.BytesReader;
import com.github.cao.awa.kalmia.annotation.network.unsolve.AutoSolvedPacket;
import com.github.cao.awa.kalmia.env.KalmiaPreSharedKey;
import com.github.cao.awa.kalmia.mathematic.base.Base256;
import com.github.cao.awa.kalmia.mathematic.base.SkippedBase256;
import com.github.cao.awa.kalmia.network.encode.crypto.asymmetric.ac.EcCrypto;
import com.github.cao.awa.kalmia.network.encode.crypto.symmetric.aes.AesCrypto;
import com.github.cao.awa.kalmia.network.handler.handshake.HandshakeHandler;
import com.github.cao.awa.kalmia.network.packet.dual.DualPacket;
import com.github.cao.awa.kalmia.network.packet.dual.handshake.crypto.aes.HandshakeAesCipherPacket;
import com.github.cao.awa.kalmia.network.router.RequestRouter;
import com.github.cao.awa.modmdo.annotation.platform.Generic;
import com.github.cao.awa.viburnum.util.bytes.BytesUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.nio.charset.StandardCharsets;

@Generic
@AutoSolvedPacket(1)
public class HandshakePreSharedEcPacket extends DualPacket<HandshakeHandler> {
    private static final Logger LOGGER = LogManager.getLogger("PreSharedRsaPacket");
    public static final byte[] ID = SkippedBase256.longToBuf(1);
    private static final byte[] AES_CIPHER = BytesRandomIdentifier.create(32);
    private final String cipherKey;

    public HandshakePreSharedEcPacket(BytesReader reader) {
        this.cipherKey = new String(reader.read(Base256.tagFromBuf(reader.read(2))),
                                    StandardCharsets.US_ASCII
        );
    }

    @Override
    public void inbound(RequestRouter router, HandshakeHandler handler) {
        if (! this.cipherKey.equals(KalmiaPreSharedKey.expectCipherKey)) {
            LOGGER.warn("The server sent cipher key is not same to client expected: " + this.cipherKey + " (server) / " + KalmiaPreSharedKey.expectCipherKey + " (client)");
        } else {
            LOGGER.info("Server sent cipher key is: " + this.cipherKey);
        }
        router.setCrypto(new EcCrypto(KalmiaPreSharedKey.pubkeyManager.get(this.cipherKey),
                                      null
        ));
        router.send(new HandshakeAesCipherPacket(AES_CIPHER));
        router.setCrypto(new AesCrypto(AES_CIPHER));
    }


    public HandshakePreSharedEcPacket(String cipher) {
        this.cipherKey = cipher;
    }

    @Override
    public byte[] data() {
        try {
            return BytesUtil.concat(Base256.tagToBuf(this.cipherKey.length()),
                                    this.cipherKey.getBytes(StandardCharsets.US_ASCII)
            );
        } catch (Exception e) {
            return null;
        }
    }
}
