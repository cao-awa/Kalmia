package com.github.cao.awa.kalmia.network.packet.inbound.handshake.hello.server;

import com.github.cao.awa.apricot.annotations.auto.Auto;
import com.github.cao.awa.apricot.io.bytes.reader.BytesReader;
import com.github.cao.awa.kalmia.annotations.actor.Getter;
import com.github.cao.awa.kalmia.annotations.auto.event.network.NetworkEventTarget;
import com.github.cao.awa.kalmia.annotations.auto.network.unsolve.AutoData;
import com.github.cao.awa.kalmia.annotations.auto.network.unsolve.AutoSolvedPacket;
import com.github.cao.awa.kalmia.annotations.crypto.CryptoEncoded;
import com.github.cao.awa.kalmia.annotations.crypto.NotDecoded;
import com.github.cao.awa.kalmia.annotations.inaction.DoNotSet;
import com.github.cao.awa.kalmia.event.kalmiagram.network.inbound.handshake.hello.server.ServerHelloEvent;
import com.github.cao.awa.kalmia.network.handler.handshake.HandshakeHandler;
import com.github.cao.awa.kalmia.network.packet.Packet;
import com.github.cao.awa.modmdo.annotation.platform.Client;
import com.github.cao.awa.modmdo.annotation.platform.Server;

@AutoSolvedPacket(id = 3, crypto = true)
@NetworkEventTarget(ServerHelloEvent.class)
public class ServerHelloPacket extends Packet<HandshakeHandler> {
    @AutoData
    @DoNotSet
    private String serverName;
    @AutoData
    @DoNotSet
    private String serverVersion;
    @AutoData
    @DoNotSet
    @NotDecoded
    private byte[] iv;

    @Server
    public ServerHelloPacket(String serverName, String serverVersion, @CryptoEncoded byte[] iv) {
        this.serverName = serverName;
        this.serverVersion = serverVersion;
        this.iv = iv;
    }

    @Auto
    @Client
    public ServerHelloPacket(BytesReader reader) {
        super(reader);
    }

    @Getter
    public String serverName() {
        return this.serverName;
    }

    @Getter
    public String serverVersion() {
        return this.serverVersion;
    }

    @Getter
    public byte[] iv() {
        return this.iv;
    }
}
