package com.github.cao.awa.kalmia.network.packet.inbound.handshake.hello.client;

import com.github.cao.awa.apricot.annotation.auto.Auto;
import com.github.cao.awa.apricot.io.bytes.reader.BytesReader;
import com.github.cao.awa.kalmia.annotation.auto.event.NetworkEventTarget;
import com.github.cao.awa.kalmia.annotation.auto.network.unsolve.AutoData;
import com.github.cao.awa.kalmia.annotation.auto.network.unsolve.AutoSolvedPacket;
import com.github.cao.awa.kalmia.env.KalmiaPreSharedCipher;
import com.github.cao.awa.kalmia.event.network.inbound.handshake.hello.client.ClientHelloEvent;
import com.github.cao.awa.kalmia.network.handler.handshake.HandshakeHandler;
import com.github.cao.awa.kalmia.network.packet.Packet;
import com.github.cao.awa.kalmia.protocol.RequestProtocol;
import com.github.cao.awa.modmdo.annotation.platform.Client;
import com.github.cao.awa.modmdo.annotation.platform.Server;

@AutoSolvedPacket(0)
@NetworkEventTarget(ClientHelloEvent.class)
public class ClientHelloPacket extends Packet<HandshakeHandler> {
    @AutoData
    private RequestProtocol majorProtocol;
    @AutoData
    private String clientVersion;
    @AutoData
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

    public RequestProtocol majorProtocol() {
        return this.majorProtocol;
    }

    public String clientVersion() {
        return this.clientVersion;
    }

    public String expectCipherField() {
        return this.expectCipherField;
    }
}
