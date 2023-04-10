package com.github.cao.awa.kalmia.network.packet.inbound.handshake.hello;

import com.github.cao.awa.apricot.io.bytes.reader.BytesReader;
import com.github.cao.awa.apricot.util.digger.MessageDigger;
import com.github.cao.awa.kalmia.mathematic.Mathematics;
import com.github.cao.awa.kalmia.mathematic.base.Base256;
import com.github.cao.awa.kalmia.network.handler.PacketHandler;
import com.github.cao.awa.kalmia.network.packet.ReadonlyPacket;
import com.github.cao.awa.kalmia.network.packet.request.handshake.hello.ServerHelloRequest;
import com.github.cao.awa.kalmia.network.packet.request.login.LoginWithPasswordRequest;
import com.github.cao.awa.kalmia.network.packet.unsolve.handshake.hello.UnsolvedServerHelloPacket;
import com.github.cao.awa.kalmia.network.router.UnsolvedRequestRouter;
import com.github.cao.awa.modmdo.annotation.platform.Client;

import java.util.Arrays;

/**
 * @see ServerHelloRequest
 * @see UnsolvedServerHelloPacket
 */
@Client
public class ServerHelloPacket extends ReadonlyPacket {
    public static ServerHelloPacket create(BytesReader reader) {
        return new ServerHelloPacket(reader.read(Base256.tagFromBuf(reader.read(2))),
                                     reader.read(reader.read())
        );
    }

    private final byte[] testKey;
    private final byte[] testSha;

    public ServerHelloPacket(byte[] testKey, byte[] testSha) {
        this.testKey = testKey;
        this.testSha = testSha;
    }

    public byte[] getTestKey() {
        return this.testKey;
    }

    public byte[] getTestSha() {
        return this.testSha;
    }

    @Override
    public void inbound(UnsolvedRequestRouter router, PacketHandler<?> handler) {
        System.out.println("Server Hello!");

        byte[] provideCipher = router.decode(this.testKey);

        System.out.println("Server Sent Hello: " + Arrays.toString(Mathematics.toBytes(MessageDigger.digest(provideCipher,
                                                                                    MessageDigger.Sha3.SHA_512
                                                               ),
                                                               16
        )));
        System.out.println("Server Provide Hello: " + Arrays.toString(this.testSha));

        if (router.isCipherEquals(provideCipher)) {
            System.out.println("Server is no or skipped MITM!");
        } else {
            System.out.println("This transport are current under MITM attack!");
        }

        router.send(new LoginWithPasswordRequest());
    }
}
