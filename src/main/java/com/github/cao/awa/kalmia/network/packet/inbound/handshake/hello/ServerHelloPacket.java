package com.github.cao.awa.kalmia.network.packet.inbound.handshake.hello;

import com.github.cao.awa.apricot.io.bytes.reader.BytesReader;
import com.github.cao.awa.apricot.util.digger.MessageDigger;
import com.github.cao.awa.apricot.util.encryption.Crypto;
import com.github.cao.awa.kalmia.annotation.crypto.NotDecoded;
import com.github.cao.awa.kalmia.mathematic.Mathematics;
import com.github.cao.awa.kalmia.mathematic.base.Base256;
import com.github.cao.awa.kalmia.network.handler.handshake.HandshakeHandler;
import com.github.cao.awa.kalmia.network.packet.ReadonlyPacket;
import com.github.cao.awa.kalmia.network.packet.request.handshake.hello.ServerHelloRequest;
import com.github.cao.awa.kalmia.network.packet.request.login.LoginWithPasswordRequest;
import com.github.cao.awa.kalmia.network.packet.unsolve.handshake.hello.UnsolvedServerHelloPacket;
import com.github.cao.awa.kalmia.network.router.UnsolvedRequestRouter;
import com.github.cao.awa.modmdo.annotation.platform.Client;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @see ServerHelloRequest
 * @see UnsolvedServerHelloPacket
 */
@Client
public class ServerHelloPacket extends ReadonlyPacket<HandshakeHandler> {
    private static final Logger LOGGER = LogManager.getLogger("ServerHello");

    public static ServerHelloPacket create(BytesReader reader) {
        return new ServerHelloPacket(reader.read(Base256.tagFromBuf(reader.read(2))),
                                     reader.read(reader.read()),
                                     reader.read(reader.read())
        );
    }

    @NotDecoded
    private final byte[] testKey;
    private final byte[] testSha;
    @NotDecoded
    private final byte[] iv;

    public ServerHelloPacket(@NotDecoded byte[] testKey, byte[] testSha, @NotDecoded byte[] iv) {
        this.testKey = testKey;
        this.testSha = testSha;
        this.iv = iv;
    }

    public byte[] getTestKey() {
        return this.testKey;
    }

    public byte[] getTestSha() {
        return this.testSha;
    }

    @Override
    public void inbound(UnsolvedRequestRouter router, HandshakeHandler handler) {
        LOGGER.info("Server Hello!");

        byte[] provideCipher = router.decode(this.testKey);

        LOGGER.info("Server Sent Hello: " + Mathematics.radix(MessageDigger.digest(provideCipher,
                                                                                   MessageDigger.Sha3.SHA_512
                                                              ),
                                                              16,
                                                              36
        ));
        LOGGER.info("Server Provide Hello: " + Mathematics.radix(this.testSha,
                                                                 36
        ));

        if (router.isCipherEquals(provideCipher)) {
            LOGGER.info("Server is no or skipped MITM!");
        } else {
            LOGGER.info("This transport are current under MITM attack!");
        }

        if (this.iv.length == 16) {
            LOGGER.info("Server IV: " + Mathematics.radix(router.decode(this.iv),
                                                          36
            ));

            router.setIv(router.decode(this.iv));
        } else {
            router.setIv(Crypto.defaultIv());
        }

        router.send(new LoginWithPasswordRequest());
    }
}
