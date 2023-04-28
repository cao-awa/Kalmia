package com.github.cao.awa.kalmia.network.io.server.channel;

import com.github.cao.awa.apricot.anntation.Stable;
import com.github.cao.awa.kalmia.network.encode.RequestDecoder;
import com.github.cao.awa.kalmia.network.encode.RequestEncoder;
import com.github.cao.awa.kalmia.network.router.RequestRouter;
import com.github.cao.awa.kalmia.server.KalmiaServer;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.FixedRecvByteBufAllocator;
import io.netty.channel.socket.SocketChannel;

import java.util.List;

/**
 * Channel initializer of kalmia server network.
 *
 * @author 草二号机
 * @since 1.0.0
 */
@Stable
public class KalmiaServerChannelInitializer extends ChannelInitializer<SocketChannel> {
    private final KalmiaServer server;
    private List<RequestRouter> subscriber;

    public KalmiaServerChannelInitializer(KalmiaServer server) {
        this.server = server;
    }

    public KalmiaServerChannelInitializer subscribe(List<RequestRouter> routers) {
        this.subscriber = routers;
        return this;
    }

    public KalmiaServerChannelInitializer active(RequestRouter router) {
        if (this.subscriber != null) {
            this.subscriber.add(router);
            System.out.println("Active connection, current: " + this.subscriber.size());
        }
        return this;
    }

    public KalmiaServerChannelInitializer unsubscribe() {
        this.subscriber = null;
        return this;
    }

    public KalmiaServerChannelInitializer inactive(RequestRouter router) {
        if (this.subscriber != null) {
            this.subscriber.remove(router);
            System.out.println("Inactive connection, current: " + this.subscriber.size());
        }
        return this;
    }

    /**
     * This method will be called once the {@link Channel} was registered. After the method returns this instance
     * will be removed from the {@link ChannelPipeline} of the {@link Channel}.
     *
     * @param ch the {@link Channel} which was registered.
     */
    @Override
    protected void initChannel(SocketChannel ch) {
        ch.config()
          .setRecvByteBufAllocator(new FixedRecvByteBufAllocator(16 * 1024));
        ChannelPipeline pipeline = ch.pipeline();
        // Do decodes
//        pipeline.addLast(new RequestCodec());
        RequestRouter router = new RequestRouter(r -> {
        }).funeral(this :: inactive);
        pipeline.addLast(new RequestDecoder(router));
        pipeline.addLast(new RequestEncoder(router));
        // Do handle
        pipeline.addLast(router);

        active(router);
    }
}
