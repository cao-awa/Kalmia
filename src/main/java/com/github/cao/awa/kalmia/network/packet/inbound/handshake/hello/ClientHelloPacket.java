package com.github.cao.awa.kalmia.network.packet.inbound.handshake.hello;

import com.github.cao.awa.kalmia.annotation.network.unsolve.AutoSolvedPacket;
import com.github.cao.awa.kalmia.network.handler.handshake.HandshakeHandler;
import com.github.cao.awa.kalmia.network.packet.ReadonlyPacket;
import com.github.cao.awa.kalmia.network.packet.request.handshake.crypto.rsa.pubkey.HandshakeRsaPubkeyRequest;
import com.github.cao.awa.kalmia.network.packet.request.handshake.hello.ClientHelloRequest;
import com.github.cao.awa.kalmia.network.router.UnsolvedRequestRouter;
import com.github.cao.awa.modmdo.annotation.platform.Server;

/**
 * @see ClientHelloRequest
 */
@Server
@AutoSolvedPacket(0)
public class ClientHelloPacket extends ReadonlyPacket<HandshakeHandler> {
    public ClientHelloPacket() {
    }

    @Override
    public void inbound(UnsolvedRequestRouter router, HandshakeHandler handler) {
        System.out.println("Client Hello!");
        handler.setupRsa();
        router.send(new HandshakeRsaPubkeyRequest(handler));
    }
}
