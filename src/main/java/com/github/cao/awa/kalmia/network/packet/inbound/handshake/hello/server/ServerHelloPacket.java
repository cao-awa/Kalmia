package com.github.cao.awa.kalmia.network.packet.inbound.handshake.hello.server;

import com.github.cao.awa.apricot.annotation.auto.Auto;
import com.github.cao.awa.apricot.io.bytes.reader.BytesReader;
import com.github.cao.awa.kalmia.annotation.auto.event.NetworkEventTarget;
import com.github.cao.awa.kalmia.annotation.auto.network.unsolve.AutoData;
import com.github.cao.awa.kalmia.annotation.auto.network.unsolve.AutoSolvedPacket;
import com.github.cao.awa.kalmia.annotation.crypto.CryptoEncoded;
import com.github.cao.awa.kalmia.annotation.crypto.NotDecoded;
import com.github.cao.awa.kalmia.event.network.inbound.handshake.hello.server.ServerHelloEvent;
import com.github.cao.awa.kalmia.network.handler.handshake.HandshakeHandler;
import com.github.cao.awa.kalmia.network.packet.Packet;
import com.github.cao.awa.modmdo.annotation.platform.Client;
import com.github.cao.awa.modmdo.annotation.platform.Server;

@AutoSolvedPacket(3)
@NetworkEventTarget(ServerHelloEvent.class)
public class ServerHelloPacket extends Packet<HandshakeHandler> {
    @AutoData
    @NotDecoded
    private byte[] testKey;
    @AutoData
    private byte[] testSha;
    @AutoData
    @NotDecoded
    private byte[] iv;

    @Server
    public ServerHelloPacket(@CryptoEncoded byte[] testKey, byte[] testSha, @CryptoEncoded byte[] iv) {
        try {
            this.testKey = testKey;
            this.testSha = testSha;
            this.iv = iv;
        } catch (Exception e) {
            //TODO
            throw new RuntimeException(e);
        }
    }

    @Auto
    @Client
    public ServerHelloPacket(BytesReader reader) {
        super(reader);
    }

    public byte[] testKey() {
        return this.testKey;
    }

    public byte[] testSha() {
        return this.testSha;
    }

    public byte[] iv() {
        return this.iv;
    }
}
