package com.github.cao.awa.kalmia.network.router;

import com.github.cao.awa.kalmia.network.crypto.SymmetricTransportLayer;
import com.github.cao.awa.kalmia.network.exception.InvalidPacketException;
import com.github.cao.awa.kalmia.network.handler.PacketHandler;
import com.github.cao.awa.kalmia.network.handler.handshake.HandshakeHandler;
import com.github.cao.awa.kalmia.network.packet.ReadonlyPacket;
import com.github.cao.awa.kalmia.network.packet.UnsolvedPacket;
import com.github.cao.awa.kalmia.network.packet.WritablePacket;
import com.github.cao.awa.kalmia.network.packet.request.handshake.hello.ClientHelloRequest;
import io.netty.channel.ChannelHandlerContext;

public class UnsolvedRequestRouter extends NetworkRouter {
    private final SymmetricTransportLayer transportLayer = new SymmetricTransportLayer(false);
    private PacketHandler<?> handler = new HandshakeHandler();
    private ChannelHandlerContext context;
    private final boolean isClient;

    public UnsolvedRequestRouter(boolean isClient) {
        this.isClient = isClient;
    }

    @Override
    protected void messageReceived(ChannelHandlerContext ctx, UnsolvedPacket msg) throws Exception {
        try {
            ReadonlyPacket packet = this.handler.tryHandle(msg);
            System.out.println(packet);
            this.handler.inbound(packet, this);
        } catch (InvalidPacketException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
        this.context = ctx;
        if (this.isClient) {
            send(new ClientHelloRequest());
        }
    }

    public byte[] decode(byte[] cipherText) {
        return this.transportLayer.decode(cipherText);
    }

    public byte[] encode(byte[] plainText) {
        return this.transportLayer.encode(plainText);
    }

    public void send(WritablePacket packet) {
        this.context.writeAndFlush(packet);
    }

    public void send(byte[] bytes) {
        this.context.writeAndFlush(bytes);
    }
}
