package com.github.cao.awa.kalmia.network.packet.handshake.hello;

import com.github.cao.awa.apricot.io.bytes.reader.BytesReader;
import com.github.cao.awa.kalmia.network.packet.ReadonlyPacket;
import com.github.cao.awa.kalmia.network.packet.request.handshake.rsa.pubkey.HandshakeRsaPubkeyRequest;
import com.github.cao.awa.kalmia.network.router.UnsolvedRequestRouter;

import java.util.Arrays;

public class ClientHelloPacket extends ReadonlyPacket {
    private final String helloKey;

    public ClientHelloPacket(String helloKey) {
        this.helloKey = helloKey;
    }

    public static ClientHelloPacket create(BytesReader data) {
        return new ClientHelloPacket(new String(data.all()));
    }

    @Override
    public void inbound(UnsolvedRequestRouter router) {
        System.out.println("Hello Key: " + this.helloKey);
        router.send(new HandshakeRsaPubkeyRequest());
    }
}
