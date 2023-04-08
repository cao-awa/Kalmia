package com.github.cao.awa.kalmia.network.packet.handshake.hello;

import com.github.cao.awa.apricot.io.bytes.reader.BytesReader;
import com.github.cao.awa.kalmia.mathematic.base.Base256;
import com.github.cao.awa.kalmia.network.handler.PacketHandler;
import com.github.cao.awa.kalmia.network.packet.ReadonlyPacket;
import com.github.cao.awa.kalmia.network.packet.request.handshake.crypto.rsa.pubkey.HandshakeRsaPubkeyRequest;
import com.github.cao.awa.kalmia.network.packet.request.handshake.hello.ClientHelloRequest;
import com.github.cao.awa.kalmia.network.router.UnsolvedRequestRouter;
import com.github.cao.awa.modmdo.annotation.platform.Server;
import com.github.zhuaidadaya.rikaishinikui.handler.universal.entrust.EntrustEnvironment;

/**
 * @see ClientHelloRequest
 */
@Server
public class ClientHelloPacket extends ReadonlyPacket {
    private final String helloKey;

    public ClientHelloPacket(String helloKey) {
        this.helloKey = helloKey;
    }

    public static ClientHelloPacket create(BytesReader data) {
        int length = Base256.tagFromBuf(data.read(2));
        return new ClientHelloPacket(new String(data.read(length)));
    }

    @Override
    public void inbound(UnsolvedRequestRouter router, PacketHandler<?> handler) {
        System.out.println("Hello Key: " + this.helloKey);
        router.send(new HandshakeRsaPubkeyRequest(EntrustEnvironment.cast(handler)));
    }
}
