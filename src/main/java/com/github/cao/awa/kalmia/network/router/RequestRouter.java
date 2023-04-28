package com.github.cao.awa.kalmia.network.router;

import com.github.cao.awa.apricot.util.collection.ApricotCollectionFactor;
import com.github.cao.awa.kalmia.bug.BugTrace;
import com.github.cao.awa.kalmia.network.encode.crypto.SymmetricTransportLayer;
import com.github.cao.awa.kalmia.network.encode.crypto.symmetric.SymmetricCrypto;
import com.github.cao.awa.kalmia.network.exception.InvalidPacketException;
import com.github.cao.awa.kalmia.network.handler.PacketHandler;
import com.github.cao.awa.kalmia.network.handler.handshake.HandshakeHandler;
import com.github.cao.awa.kalmia.network.handler.inbound.AuthedRequestHandler;
import com.github.cao.awa.kalmia.network.handler.inbound.disabled.DisabledRequestHandler;
import com.github.cao.awa.kalmia.network.handler.login.LoginHandler;
import com.github.cao.awa.kalmia.network.handler.ping.PingHandler;
import com.github.cao.awa.kalmia.network.packet.Request;
import com.github.cao.awa.kalmia.network.packet.UnsolvedPacket;
import com.github.cao.awa.kalmia.network.packet.request.invalid.operation.OperationInvalidRequest;
import com.github.cao.awa.kalmia.network.packet.unsolve.ping.UnsolvedPingPacket;
import com.github.cao.awa.kalmia.network.router.status.RequestStatus;
import com.github.zhuaidadaya.rikaishinikui.handler.universal.entrust.EntrustEnvironment;
import io.netty.channel.ChannelHandlerContext;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.function.Consumer;

public class RequestRouter extends NetworkRouter {
    private final Map<RequestStatus, PacketHandler<?>> handlers = EntrustEnvironment.operation(ApricotCollectionFactor.newHashMap(),
                                                                                               handlers -> {
                                                                                                   handlers.put(RequestStatus.HELLO,
                                                                                                                new HandshakeHandler()
                                                                                                   );
                                                                                                   handlers.put(RequestStatus.AUTH,
                                                                                                                new LoginHandler()
                                                                                                   );
                                                                                                   handlers.put(RequestStatus.AUTHED,
                                                                                                                new AuthedRequestHandler()
                                                                                                   );
                                                                                                   handlers.put(RequestStatus.DISABLED,
                                                                                                                new DisabledRequestHandler()
                                                                                                   );
                                                                                               }
    );
    private final SymmetricTransportLayer transportLayer = new SymmetricTransportLayer();
    private RequestStatus status;
    private PacketHandler<?> handler;
    private final PingHandler pingHandler = new PingHandler();
    private ChannelHandlerContext context;
    private final Consumer<RequestRouter> activeCallback;

    public RequestRouter(Consumer<RequestRouter> activeCallback) {
        this.activeCallback = activeCallback;
        setStatus(RequestStatus.HELLO);
    }

    public RequestStatus getStatus() {
        return this.status;
    }

    public void setStatus(RequestStatus status) {
        this.status = status;
        this.handler = this.handlers.get(status);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, UnsolvedPacket<?> msg) throws Exception {
        try {
            if (msg instanceof UnsolvedPingPacket<?> pingPacket) {
                this.pingHandler.tryInbound(pingPacket,
                                            this
                );
            } else {
                this.handler.tryInbound(msg,
                                        this
                );
            }
        } catch (InvalidPacketException e) {
            // TODO
            e.printStackTrace();

            send(new OperationInvalidRequest("Server internal error"));
        } catch (Exception e) {
            BugTrace.trace(e,
                           "Event pipeline happened exception or packet deserialize not completed, please check last bug trace and report theses trace"
            );
        }
    }

    @Override
    public void channelActive(@NotNull ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
        this.context = ctx;
        this.activeCallback.accept(this);
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

    public void send(Request packet) {
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

    public PacketHandler<?> getHandler() {
        return this.handlers.get(this.status);
    }
}
