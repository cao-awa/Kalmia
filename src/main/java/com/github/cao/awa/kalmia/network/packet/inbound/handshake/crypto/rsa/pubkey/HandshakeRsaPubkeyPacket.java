package com.github.cao.awa.kalmia.network.packet.inbound.handshake.crypto.rsa.pubkey;

import com.github.cao.awa.apricot.identifier.BytesRandomIdentifier;
import com.github.cao.awa.apricot.io.bytes.reader.BytesReader;
import com.github.cao.awa.kalmia.mathematic.base.Base256;
import com.github.cao.awa.kalmia.network.encode.crypto.symmetric.aes.AesCrypto;
import com.github.cao.awa.kalmia.network.handler.PacketHandler;
import com.github.cao.awa.kalmia.network.packet.ReadonlyPacket;
import com.github.cao.awa.kalmia.network.packet.request.handshake.crypto.aes.HandshakeAesCipherRequest;
import com.github.cao.awa.kalmia.network.packet.request.handshake.crypto.rsa.pubkey.HandshakeRsaPubkeyRequest;
import com.github.cao.awa.kalmia.network.packet.unsolve.handshake.crypto.rsa.pubkey.UnsolvedHandshakeRsaPubkeyPacket;
import com.github.cao.awa.kalmia.network.router.UnsolvedRequestRouter;
import com.github.cao.awa.modmdo.annotation.platform.Client;

import java.util.Arrays;

/**
 * @see HandshakeRsaPubkeyRequest
 * @see UnsolvedHandshakeRsaPubkeyPacket
 */
@Client
public class HandshakeRsaPubkeyPacket extends ReadonlyPacket {
    private static final byte[] AES_CIPHER = BytesRandomIdentifier.create(32);
    private final byte[] pubkey;

    public HandshakeRsaPubkeyPacket(byte[] pubkey) {
        this.pubkey = pubkey;
    }

    public static HandshakeRsaPubkeyPacket create(BytesReader data) {
        return new HandshakeRsaPubkeyPacket(data.read(Base256.tagFromBuf(data.read(2))));
    }

    @Override
    public void inbound(UnsolvedRequestRouter router, PacketHandler<?> handler) {
        System.out.println("RSA Key: " + Arrays.toString(this.pubkey));
        router.send(new HandshakeAesCipherRequest(this.pubkey, AES_CIPHER));
        router.setCrypto(new AesCrypto(AES_CIPHER));
    }
}
