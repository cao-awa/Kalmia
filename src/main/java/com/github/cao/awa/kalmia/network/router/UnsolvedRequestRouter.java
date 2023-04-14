package com.github.cao.awa.kalmia.network.router;

import com.github.cao.awa.apricot.util.collection.ApricotCollectionFactor;
import com.github.cao.awa.kalmia.network.encode.crypto.SymmetricTransportLayer;
import com.github.cao.awa.kalmia.network.encode.crypto.symmetric.SymmetricCrypto;
import com.github.cao.awa.kalmia.network.exception.InvalidPacketException;
import com.github.cao.awa.kalmia.network.handler.PacketHandler;
import com.github.cao.awa.kalmia.network.handler.handshake.HandshakeHandler;
import com.github.cao.awa.kalmia.network.handler.login.LoginHandler;
import com.github.cao.awa.kalmia.network.packet.UnsolvedPacket;
import com.github.cao.awa.kalmia.network.packet.WritablePacket;
import com.github.cao.awa.kalmia.network.packet.request.handshake.hello.ClientHelloRequest;
import com.github.cao.awa.kalmia.network.router.status.RequestStatus;
import com.github.zhuaidadaya.rikaishinikui.handler.universal.entrust.EntrustEnvironment;
import io.netty.channel.ChannelHandlerContext;

import java.util.Map;

public class UnsolvedRequestRouter extends NetworkRouter {
    private final Map<RequestStatus, PacketHandler<?, ?>> handlers = EntrustEnvironment.operation(ApricotCollectionFactor.newHashMap(),
                                                                                                  handlers -> {
                                                                                                      handlers.put(RequestStatus.HELLO,
                                                                                                                   new HandshakeHandler()
                                                                                                      );
                                                                                                      handlers.put(RequestStatus.AUTH,
                                                                                                                   new LoginHandler()
                                                                                                      );
                                                                                                  }
    );
    private final SymmetricTransportLayer transportLayer = new SymmetricTransportLayer();
    private RequestStatus status;
    private PacketHandler<?, ?> handler;
    private ChannelHandlerContext context;
    private final boolean isClient;

    public UnsolvedRequestRouter(boolean isClient) {
        this.isClient = isClient;
        setStatus(RequestStatus.HELLO);
    }

    public RequestStatus getStatus() {
        return this.status;
    }

    public void setStatus(RequestStatus status) {
        this.status = status;
        this.handler = handlers.get(status);
    }

    @Override
    protected void messageReceived(ChannelHandlerContext ctx, UnsolvedPacket msg) throws Exception {
        try {
            this.handler.tryInbound(msg,
                                 this
            );
        } catch (InvalidPacketException e) {
            // TODO
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

    public boolean isCipherEquals(byte[] cipher) {
        return this.transportLayer.isCipherEquals(cipher);
    }

    public void send(WritablePacket packet) {
        this.context.writeAndFlush(packet);
    }

    public void send(byte[] bytes) {
        this.context.writeAndFlush(bytes);
    }

    public void setCrypto(SymmetricCrypto crypto) {
        this.transportLayer.setCrypto(crypto);
    }

    public void setIv(byte[] iv) {
        this.transportLayer.setIv(iv);
    }
}
