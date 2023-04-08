package com.github.cao.awa.kalmia.network.packet.handshake.crypto.aes;

import com.github.cao.awa.apricot.io.bytes.reader.BytesReader;
import com.github.cao.awa.apricot.util.encryption.Crypto;
import com.github.cao.awa.kalmia.mathematic.base.Base256;
import com.github.cao.awa.kalmia.network.handler.PacketHandler;
import com.github.cao.awa.kalmia.network.handler.handshake.HandshakeHandler;
import com.github.cao.awa.kalmia.network.packet.ReadonlyPacket;
import com.github.cao.awa.kalmia.network.packet.request.handshake.crypto.aes.HandshakeAesKeyRequest;
import com.github.cao.awa.kalmia.network.packet.unsolve.handshake.crypto.aes.UnsolvedHandshakeAesKeyPacket;
import com.github.cao.awa.kalmia.network.router.UnsolvedRequestRouter;
import com.github.cao.awa.modmdo.annotation.platform.Client;

/**
 * @see HandshakeAesKeyRequest
 * @see UnsolvedHandshakeAesKeyPacket
 */
@Client
public class HandshakeAesKeyPacket extends ReadonlyPacket {
    private final byte[] key;

    public HandshakeAesKeyPacket(byte[] key) {
        this.key = key;
    }

    public static HandshakeAesKeyPacket create(BytesReader reader) {
        int length = Base256.tagFromBuf(reader.read(2));
        return new HandshakeAesKeyPacket(reader.read(length));
    }

    @Override
    public void inbound(UnsolvedRequestRouter router, PacketHandler<?> handler) {
        try {
            System.out.println("AES Key: " + new String(Crypto.rsaDecrypt(this.key,
                                                               Crypto.decodeRsaPrikey(((HandshakeHandler) handler).getRsaPrikey())
            )));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
