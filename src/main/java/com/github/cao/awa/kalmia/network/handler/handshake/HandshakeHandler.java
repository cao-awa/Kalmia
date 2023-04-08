package com.github.cao.awa.kalmia.network.handler.handshake;

import com.github.cao.awa.kalmia.network.handler.PacketHandler;
import com.github.cao.awa.kalmia.network.packet.ReadonlyPacket;
import com.github.cao.awa.kalmia.network.packet.unsolve.handshake.UnsolvedHandshakePacket;
import com.github.cao.awa.kalmia.network.router.UnsolvedRequestRouter;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

public class HandshakeHandler extends PacketHandler<UnsolvedHandshakePacket<?>> {
    private final byte[] rsaPrikey;
    private final byte[] rsaPubkey;

    public byte[] getRsaPrikey() {
        return this.rsaPrikey;
    }

    public byte[] getRsaPubkey() {
        return this.rsaPubkey;
    }

    public HandshakeHandler() {
        try {
            KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("RSA");
            keyPairGen.initialize(4096);
            KeyPair keyPair = keyPairGen.generateKeyPair();
            RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
            RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();

            this.rsaPrikey = privateKey.getEncoded();
            this.rsaPubkey = publicKey.getEncoded();
        } catch (Exception e) {
            // TODO
            throw new RuntimeException(e);
        }
    }

    @Override
    public ReadonlyPacket handle(UnsolvedHandshakePacket<?> packet) {
        return packet.toPacket();
    }

    @Override
    public void inbound(ReadonlyPacket packet, UnsolvedRequestRouter router) {
        packet.inbound(router, this);
    }
}
