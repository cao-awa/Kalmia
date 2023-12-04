package com.github.cao.awa.kalmia.network.packet.inbound.handshake.hello.client;

import com.github.cao.awa.apricot.annotations.auto.Auto;
import com.github.cao.awa.apricot.io.bytes.reader.BytesReader;
import com.github.cao.awa.kalmia.annotations.actor.Getter;
import com.github.cao.awa.kalmia.annotations.auto.event.network.NetworkEventTarget;
import com.github.cao.awa.kalmia.annotations.auto.network.unsolve.AutoData;
import com.github.cao.awa.kalmia.annotations.auto.network.unsolve.AutoSolvedPacket;
import com.github.cao.awa.kalmia.annotations.inaction.DoNotSet;
import com.github.cao.awa.kalmia.env.KalmiaPreSharedCipher;
import com.github.cao.awa.kalmia.event.kalmiagram.network.inbound.handshake.hello.client.ClientHelloEvent;
import com.github.cao.awa.kalmia.network.handler.handshake.HandshakeHandler;
import com.github.cao.awa.kalmia.network.packet.Packet;
import com.github.cao.awa.kalmia.protocol.RequestProtocol;
import com.github.cao.awa.modmdo.annotation.platform.Client;
import com.github.cao.awa.modmdo.annotation.platform.Server;

@AutoSolvedPacket(id = 0, crypto = false)
@NetworkEventTarget(ClientHelloEvent.class)
public class ClientHelloPacket extends Packet<HandshakeHandler> {
    @AutoData
    @DoNotSet
    private RequestProtocol majorProtocol;
    @AutoData
    @DoNotSet
    private String clientVersion;
    @AutoData
    @DoNotSet
    private String expectCipherField;

    @Client
    public ClientHelloPacket(RequestProtocol majorProtocol, String clientVersion, String expectCipherField) {
        this.majorProtocol = majorProtocol;
        this.clientVersion = clientVersion;
        this.expectCipherField = expectCipherField;
    }

    @Client
    public ClientHelloPacket(RequestProtocol majorProtocol, String clientVersion) {
        this(majorProtocol,
             clientVersion,
             KalmiaPreSharedCipher.expectCipherField
        );
    }

    @Auto
    @Server
    public ClientHelloPacket(BytesReader reader) {
        super(reader);
    }

    @Getter
    public RequestProtocol majorProtocol() {
        return this.majorProtocol;
    }

    @Getter
    public String clientVersion() {
        return this.clientVersion;
    }

    @Getter
    public String expectCipherField() {
        return this.expectCipherField;
    }
}
