package com.github.cao.awa.kalmia.network.router;

import com.github.cao.awa.apricot.io.bytes.reader.BytesReader;
import com.github.cao.awa.apricot.util.collection.ApricotCollectionFactor;
import com.github.cao.awa.kalmia.bootstrap.Kalmia;
import com.github.cao.awa.kalmia.bug.BugTrace;
import com.github.cao.awa.kalmia.function.provider.Consumers;
import com.github.cao.awa.kalmia.mathematic.base.Base256;
import com.github.cao.awa.kalmia.network.encode.compress.RequestCompressor;
import com.github.cao.awa.kalmia.network.encode.compress.RequestCompressorType;
import com.github.cao.awa.kalmia.network.encode.crypto.CryptoTransportLayer;
import com.github.cao.awa.kalmia.network.encode.crypto.LayerCrypto;
import com.github.cao.awa.kalmia.network.exception.InvalidPacketException;
import com.github.cao.awa.kalmia.network.handler.PacketHandler;
import com.github.cao.awa.kalmia.network.handler.handshake.HandshakeHandler;
import com.github.cao.awa.kalmia.network.handler.inbound.AuthedRequestHandler;
import com.github.cao.awa.kalmia.network.handler.inbound.disabled.DisabledRequestHandler;
import com.github.cao.awa.kalmia.network.handler.login.LoginHandler;
import com.github.cao.awa.kalmia.network.handler.stateless.StatelessHandler;
import com.github.cao.awa.kalmia.network.packet.Packet;
import com.github.cao.awa.kalmia.network.packet.UnsolvedPacket;
import com.github.cao.awa.kalmia.network.packet.inbound.invalid.operation.OperationInvalidPacket;
import com.github.cao.awa.kalmia.network.router.meta.RouterMetadata;
import com.github.cao.awa.kalmia.network.router.status.RequestState;
import com.github.cao.awa.viburnum.util.bytes.BytesUtil;
import com.github.zhuaidadaya.rikaishinikui.handler.universal.affair.Affair;
import com.github.zhuaidadaya.rikaishinikui.handler.universal.entrust.EntrustEnvironment;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.concurrent.Future;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

import java.net.SocketException;
import java.util.Map;
import java.util.function.Consumer;

public class RequestRouter extends NetworkRouter {
    private static final Logger LOGGER = LogManager.getLogger("RequestRouter");
    private final Map<RequestState, PacketHandler<?>> handlers = EntrustEnvironment.operation(ApricotCollectionFactor.hashMap(),
                                                                                              handlers -> {
                                                                                                  handlers.put(RequestState.HELLO,
                                                                                                               new HandshakeHandler()
                                                                                                  );
                                                                                                  handlers.put(RequestState.AUTH,
                                                                                                               new LoginHandler()
                                                                                                  );
                                                                                                  handlers.put(RequestState.AUTHED,
                                                                                                               new AuthedRequestHandler()
                                                                                                  );
                                                                                                  handlers.put(RequestState.DISABLED,
                                                                                                               new DisabledRequestHandler()
                                                                                                  );
                                                                                              }
    );
    private final CryptoTransportLayer transportLayer = new CryptoTransportLayer();
    private RequestState states;
    private PacketHandler<?> allowedHandler;
    private final StatelessHandler statelessHandler = new StatelessHandler();
    private ChannelHandlerContext context;
    private final Consumer<RequestRouter> activeCallback;
    private final RequestCompressor compressor = new RequestCompressor();
    private final Affair funeral = Affair.empty();
    private long uid;
    private final RouterMetadata metadata = RouterMetadata.create();

    public long getUid() {
        return this.uid;
    }

    public void setUid(long uid) {
        this.uid = uid;
    }

    public RequestCompressor getCompressor() {
        return this.compressor;
    }

    public void setCompressor(RequestCompressorType type) {
        this.compressor.setCompressor(type);
    }

    public RequestRouter() {
        this(Consumers.doNothing());
    }

    public RequestRouter(Consumer<RequestRouter> activeCallback) {
        this.activeCallback = activeCallback;
        setStates(RequestState.HELLO);
    }

    public RequestState getStates() {
        return this.states;
    }

    public void setStates(RequestState states) {
        this.states = states;
        this.allowedHandler = this.handlers.get(states);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, UnsolvedPacket<?> msg) throws Exception {
        try {
            if (! this.statelessHandler.tryInbound(msg,
                                                   this
            )) {
                this.allowedHandler.tryInbound(msg,
                                               this
                );
            }
        } catch (InvalidPacketException e) {
            // TODO
            e.printStackTrace();

            send(new OperationInvalidPacket("Server internal error"));
        } catch (Exception e) {
            BugTrace.trace(e,
                           "Event pipeline happened exception or packet deserialize not completed, please check last bug trace and report theses trace"
            );
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        if (cause instanceof SocketException) {
            disconnect();
        } else {
            LOGGER.error("Unhandled exception",
                         cause
            );
        }
    }

    @Override
    public void channelActive(@NotNull ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
        this.context = ctx;
        this.activeCallback.accept(this);
        this.context.channel()
                    .closeFuture()
                    .addListener(this :: disconnect);
    }

    public void disconnect() {
        this.context.disconnect();
    }

    public void disconnect(Future<? super Void> future) {
        this.funeral.done();
        Kalmia.SERVER.logout(
                this.uid,
                this
        );
    }

    public RequestRouter funeral(Runnable action) {
        this.funeral.add(action);
        return this;
    }

    public RequestRouter funeral(Consumer<RequestRouter> action) {
        this.funeral.add(() -> action.accept(this));
        return this;
    }

    public byte[] decode(byte[] cipherText) {
        // Decode the data with transport layer.
        byte[] decodeResult = this.transportLayer.decode(cipherText);

        BytesReader reader = BytesReader.of(decodeResult);

        // Decoded data included compress mark, used to decompress.
        int compressId = Base256.tagFromBuf(reader.read(2));

        // Decompress the packet data.
        return RequestCompressorType.TYPES.get(compressId)
                                          .getCompressor()
                                          .decompress(reader.all());
    }

    public byte[] encode(byte[] sourceText) {
        // Compress the packet data.
        int compressId = getCompressor()
                .id();

        byte[] compressResult = getCompressor()
                .compress(sourceText);

        LOGGER.debug("Trying compress with {}",
                     RequestCompressorType.TYPES.get(getCompressor()
                                                             .id())
        );

        // If data length is not reduced, then do not use the compress result.
        if (sourceText.length > compressResult.length) {
            // Use the compressed result.
            LOGGER.debug("Success to compress, {} > {}",
                         compressResult.length,
                         sourceText.length
            );
        } else {
            // Use the source.
            compressResult = sourceText;

            compressId = RequestCompressorType.NONE.id();

            LOGGER.debug("Failed to compress, {} <= {}",
                         compressResult.length,
                         sourceText.length
            );
        }

        // Encode the data with transport layer.
        return this.transportLayer.encode(
                BytesUtil.concat(
                        // Include the compress mark in the encoded data.
                        Base256.tagToBuf(compressId),
                        compressResult
                )
        );
    }

    public boolean isCipherEquals(byte[] cipher) {
        return this.transportLayer.isCipherEquals(cipher);
    }

    public void send(Packet<?> packet) {
        this.context.writeAndFlush(packet);
    }

    @Deprecated
    public void send(byte[] bytes) {
        this.context.writeAndFlush(bytes);
    }

    public void setCrypto(LayerCrypto crypto) {
        this.transportLayer.setCrypto(crypto);
    }

    public void setIv(byte[] iv) {
        this.transportLayer.setIv(iv);
    }

    public PacketHandler<?> getHandler() {
        return this.handlers.get(this.states);
    }

    public boolean shouldApplyBase36() {
        return false;
    }

    public RouterMetadata metadata() {
        return this.metadata;
    }
}
