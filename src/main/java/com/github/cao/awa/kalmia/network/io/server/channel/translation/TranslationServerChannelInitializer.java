package com.github.cao.awa.kalmia.network.io.server.channel.translation;

import com.github.cao.awa.kalmia.network.io.server.channel.KalmiaServerChannelInitializer;
import com.github.cao.awa.kalmia.network.router.translation.TranslationRouter;
import com.github.cao.awa.kalmia.server.KalmiaServer;
import io.netty.channel.Channel;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.codec.http.websocketx.extensions.compression.WebSocketServerCompressionHandler;

public class TranslationServerChannelInitializer extends KalmiaServerChannelInitializer {
    public TranslationServerChannelInitializer(KalmiaServer server) {
        super(server);
    }

    /**
     * This method will be called once the {@link Channel} was registered. After the method returns this instance
     * will be removed from the {@link ChannelPipeline} of the {@link Channel}.
     *
     * @param ch the {@link Channel} which was registered.
     */
    @Override
    protected void initChannel(SocketChannel ch) {
        ChannelPipeline pipeline = ch.pipeline();
        // Do decodes
        pipeline.addLast(new HttpServerCodec());
        pipeline.addLast(new HttpObjectAggregator(65536));
        pipeline.addLast(new WebSocketServerCompressionHandler());
        pipeline.addLast(new WebSocketServerProtocolHandler(
                "/",
                null,
                true,
                65536 * 16
        ));

        TranslationRouter router = new TranslationRouter();

        // Do handle
        pipeline.addLast(router);

        System.out.println("Active");

//        // Add to subscriber list.
//        active(router);
    }
}
