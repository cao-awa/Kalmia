package com.github.cao.awa.kalmia.network.packet.inbound.handshake.hello;

import com.github.cao.awa.apricot.io.bytes.reader.BytesReader;
import com.github.cao.awa.kalmia.network.handler.handshake.HandshakeHandler;
import com.github.cao.awa.kalmia.network.packet.ReadonlyPacket;
import com.github.cao.awa.kalmia.network.packet.request.handshake.crypto.rsa.pubkey.HandshakeRsaPubkeyRequest;
import com.github.cao.awa.kalmia.network.packet.request.handshake.hello.ClientHelloRequest;
import com.github.cao.awa.kalmia.network.packet.unsolve.handshake.hello.UnsolvedClientHelloPacket;
import com.github.cao.awa.kalmia.network.router.UnsolvedRequestRouter;
import com.github.cao.awa.modmdo.annotation.platform.Server;

/**
 * @see ClientHelloRequest
 * @see UnsolvedClientHelloPacket
 */
@Server
public class ClientHelloPacket extends ReadonlyPacket<HandshakeHandler> {
    public ClientHelloPacket() {
    }

    public static ClientHelloPacket create(BytesReader reader) {
        return new ClientHelloPacket();
    }

    @Override
    public void inbound(UnsolvedRequestRouter router, HandshakeHandler handler) {
        System.out.println("Client Hello!");
        handler.setupRsa();
        router.send(new HandshakeRsaPubkeyRequest(handler));
    }
}
