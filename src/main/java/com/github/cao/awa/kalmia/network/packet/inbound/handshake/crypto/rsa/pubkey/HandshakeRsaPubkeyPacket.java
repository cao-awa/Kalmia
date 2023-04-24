package com.github.cao.awa.kalmia.network.packet.inbound.handshake.crypto.rsa.pubkey;

import com.github.cao.awa.apricot.identifier.BytesRandomIdentifier;
import com.github.cao.awa.apricot.io.bytes.reader.BytesReader;
import com.github.cao.awa.kalmia.annotation.network.unsolve.AutoSolvedPacket;
import com.github.cao.awa.kalmia.mathematic.base.Base256;
import com.github.cao.awa.kalmia.network.encode.crypto.symmetric.aes.AesCrypto;
import com.github.cao.awa.kalmia.network.handler.handshake.HandshakeHandler;
import com.github.cao.awa.kalmia.network.packet.ReadonlyPacket;
import com.github.cao.awa.kalmia.network.packet.request.handshake.crypto.aes.HandshakeAesCipherRequest;
import com.github.cao.awa.kalmia.network.packet.request.handshake.crypto.rsa.pubkey.HandshakeRsaPubkeyRequest;
import com.github.cao.awa.kalmia.network.router.RequestRouter;
import com.github.cao.awa.modmdo.annotation.platform.Client;

/**
 * @see HandshakeRsaPubkeyRequest
 */
@Client
@AutoSolvedPacket(1)
public class HandshakeRsaPubkeyPacket extends ReadonlyPacket<HandshakeHandler> {
    private static final byte[] AES_CIPHER = BytesRandomIdentifier.create(32);
    private final byte[] pubkey;

    public HandshakeRsaPubkeyPacket(BytesReader reader) {
        this.pubkey = reader.read(Base256.tagFromBuf(reader.read(2)));
    }

    @Override
    public void inbound(RequestRouter router, HandshakeHandler handler) {
        router.send(new HandshakeAesCipherRequest(this.pubkey,
                                                  AES_CIPHER
        ));
        router.setCrypto(new AesCrypto(AES_CIPHER));
    }
}
