package com.github.cao.awa.kalmia.network.packet.unsolve.handshake.hello;

import com.github.cao.awa.kalmia.network.packet.handshake.hello.ClientHelloPacket;
import com.github.cao.awa.kalmia.network.packet.request.hello.ClientHelloRequest;
import com.github.cao.awa.kalmia.network.packet.unsolve.handshake.UnsolvedHandshakePacket;

public class UnsolvedClientHelloPacket extends UnsolvedHandshakePacket<ClientHelloPacket> {
    public UnsolvedClientHelloPacket(byte[] data) {
        super(data);
    }

    @Override
    public ClientHelloPacket toPacket() {
        return ClientHelloPacket.create(reader());
    }
}
