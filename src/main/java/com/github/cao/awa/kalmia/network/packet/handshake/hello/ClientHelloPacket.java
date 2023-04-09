package com.github.cao.awa.kalmia.network.packet.handshake.hello;

import com.github.cao.awa.apricot.io.bytes.reader.BytesReader;
import com.github.cao.awa.kalmia.mathematic.base.Base256;
import com.github.cao.awa.kalmia.network.handler.PacketHandler;
import com.github.cao.awa.kalmia.network.packet.ReadonlyPacket;
import com.github.cao.awa.kalmia.network.packet.request.handshake.crypto.rsa.pubkey.HandshakeRsaPubkeyRequest;
import com.github.cao.awa.kalmia.network.packet.request.handshake.hello.ClientHelloRequest;
import com.github.cao.awa.kalmia.network.packet.unsolve.handshake.hello.UnsolvedClientHelloPacket;
import com.github.cao.awa.kalmia.network.router.UnsolvedRequestRouter;
import com.github.cao.awa.modmdo.annotation.platform.Server;
import com.github.zhuaidadaya.rikaishinikui.handler.universal.entrust.EntrustEnvironment;

/**
 * @see ClientHelloRequest
 * @see UnsolvedClientHelloPacket
 */
@Server
public class ClientHelloPacket extends ReadonlyPacket {
    public ClientHelloPacket() {
    }

    public static ClientHelloPacket create(BytesReader reader) {
        return new ClientHelloPacket();
    }

    @Override
    public void inbound(UnsolvedRequestRouter router, PacketHandler<?> handler) {
        System.out.println("Client Hello!");
        router.send(new HandshakeRsaPubkeyRequest(EntrustEnvironment.cast(handler)));
    }
}
