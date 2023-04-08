package com.github.cao.awa.kalmia.network.packet.handshake.rsa.pubkey;

import com.github.cao.awa.apricot.io.bytes.reader.BytesReader;
import com.github.cao.awa.kalmia.network.packet.ReadonlyPacket;
import com.github.cao.awa.kalmia.network.packet.unsolve.handshake.UnsolvedHandshakePacket;
import com.github.cao.awa.kalmia.network.router.UnsolvedRequestRouter;

import java.util.Arrays;

public class HandshakeRsaPubkeyPacket extends ReadonlyPacket {
    private final byte[] key;

    public HandshakeRsaPubkeyPacket(byte[] key) {
        this.key = key;
    }

    public static HandshakeRsaPubkeyPacket create(BytesReader data) {
        return new HandshakeRsaPubkeyPacket(data.all());
    }

    @Override
    public void inbound(UnsolvedRequestRouter router) {
        System.out.println("Key: " + Arrays.toString(key));
    }
}
