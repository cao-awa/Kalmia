package com.github.cao.awa.kalmia.network.router.translation;

import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.JSONWriter;
import com.github.cao.awa.kalmia.function.provider.Consumers;
import com.github.cao.awa.kalmia.network.router.kalmia.NetworkRouter;
import com.github.zhuaidadaya.rikaishinikui.handler.universal.affair.Affair;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.websocketx.ContinuationWebSocketFrame;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import io.netty.util.concurrent.Future;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

public class TranslationRouter extends NetworkRouter<WebSocketFrame> {
    private static final Logger LOGGER = LogManager.getLogger("TranslationRouter");
    private final @NotNull StringBuilder stitching = new StringBuilder();
    private ChannelHandlerContext context;
    private final Consumer<TranslationRouter> activeCallback;
    private final Affair funeral = Affair.empty();

    public TranslationRouter() {
        this(Consumers.doNothing());
    }

    public TranslationRouter(Consumer<TranslationRouter> activeCallback) {
        this.activeCallback = activeCallback;
    }

    @Override
    public void channelActive(@NotNull ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
        this.context = ctx;
        this.activeCallback.accept(this);
        this.context.channel()
                    .closeFuture()
                    .addListener(this :: disconnect);

        System.out.println("Sending hello");

        send(new TextWebSocketFrame("""
                                            {"test-hello": "hi"}
                                            """));
    }

    public void disconnect() {
        this.context.disconnect();
    }

    public void disconnect(Future<? super Void> future) {
        this.funeral.done();
    }

    public TranslationRouter funeral(Runnable action) {
        this.funeral.add(action);
        return this;
    }

    public TranslationRouter funeral(Consumer<TranslationRouter> action) {
        this.funeral.add(() -> action.accept(this));
        return this;
    }

    public void send(TextWebSocketFrame frame) {
        this.context.writeAndFlush(frame);
    }

    @Deprecated
    public void send(byte[] bytes) {
        this.context.writeAndFlush(bytes);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, WebSocketFrame msg) throws Exception {
        handleFragment(msg);
    }

    private void handleFragment(WebSocketFrame frame) {
        if (frame instanceof TextWebSocketFrame textFrame) {
            // Handle final or not finals fragment happens.
            if (frame.isFinalFragment()) {
                // Final fragment should be direct handle.
                handleFrame(textFrame.text());

                // Aftermath for wrongly append fragment.
                // In normally, this is redundancy plan, do not wish it be happens.
                if (this.stitching.length() > 0) {
                    // Handle the wrong frame forcefully.
                    LOGGER.debug("Aftermath for wrongly append fragment");
                    handleFrame(this.stitching.toString());
                    // Let stitching be clear.
                    this.stitching.setLength(0);
                }
            } else {
                // Not final fragment should be stitching to one.
                // Usually the fragment stitching length must 0, else then mean it is a wrong.
                if (this.stitching.length() == 0) {
                    // Append this fragment.
                    this.stitching.append(textFrame.text());
                } else {
                    // Do not handle it if wrongly.
                    LOGGER.warn("Occurs unexpected fragment appends");
                }
            }
        } else if (frame instanceof ContinuationWebSocketFrame continuationFrame) {
            // Handle the continuation fragments.
            this.stitching.append(continuationFrame.text());

            // Let it build to a completed fragment when continuation frame is final.
            if (continuationFrame.isFinalFragment()) {
                // Handle the completed frame.
                handleFrame(this.stitching.toString());
                // Let stitching be clear.
                this.stitching.setLength(0);
            }
        } else {
            // The frame is unsupported, do not process this.
            LOGGER.warn("Occurs unexpected fragment appender received");
            this.stitching.setLength(0);
        }
    }

    private void handleFrame(String content) {
        System.out.println("C: " + content);
        System.out.println("---");
        System.out.println(JSONObject.parseObject(content)
                                     .toString(JSONWriter.Feature.PrettyFormat));

        send(new TextWebSocketFrame("""
                                            {"test-hello": "hi~"}
                                            """));
    }
}
