package com.github.cao.awa.kalmia.network.io.server.channel.kalmiagram;

import com.github.cao.awa.apricot.annotations.Stable;
import com.github.cao.awa.kalmia.bootstrap.Kalmia;
import com.github.cao.awa.kalmia.constant.IntegerConstants;
import com.github.cao.awa.kalmia.network.encode.kalmiagram.RequestDecoder;
import com.github.cao.awa.kalmia.network.encode.kalmiagram.RequestEncoder;
import com.github.cao.awa.kalmia.network.io.server.channel.KalmiaServerChannelInitializer;
import com.github.cao.awa.kalmia.network.router.kalmia.RequestRouter;
import com.github.cao.awa.kalmia.server.KalmiaServer;
import io.netty.channel.Channel;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.FixedRecvByteBufAllocator;
import io.netty.channel.socket.SocketChannel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

/**
 * Channel initializer of kalmia server network.
 *
 * @author 草二号机
 * @since 1.0.0
 */
@Stable
public class KalmiagramServerChannelInitializer extends KalmiaServerChannelInitializer {
    private static final Logger LOGGER = LogManager.getLogger("KalmiagramServerChannelInitializer");
    private List<RequestRouter> subscriber;

    public KalmiagramServerChannelInitializer(KalmiaServer server) {
        super(server);
    }

    public KalmiagramServerChannelInitializer subscribe(List<RequestRouter> routers) {
        this.subscriber = routers;
        return this;
    }

    public KalmiagramServerChannelInitializer active(RequestRouter router) {
        if (this.subscriber != null) {
            this.subscriber.add(router);
            LOGGER.info(
                    "Active connection for {}, current count: {}",
                    router.metadata()
                          .formatConnectionId(),
                    this.subscriber.size()
            );
        }
        return this;
    }

    public KalmiagramServerChannelInitializer unsubscribe() {
        this.subscriber = null;
        return this;
    }

    public KalmiagramServerChannelInitializer inactive(RequestRouter router) {
        if (this.subscriber != null) {
            this.subscriber.remove(router);
            LOGGER.info(
                    "Inactive connection for {}, current count: {}",
                    router.metadata()
                          .formatConnectionId(),
                    this.subscriber.size()
            );
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
          .setRecvByteBufAllocator(new FixedRecvByteBufAllocator(IntegerConstants.K_16));
        ChannelPipeline pipeline = ch.pipeline();
        // Do decode.
        RequestRouter router = new RequestRouter().funeral(this :: inactive);
        pipeline.addLast(new RequestDecoder(router));
        pipeline.addLast(new RequestEncoder(router));
        // Do handle.
        pipeline.addLast(router);

        // Do final handle.
        router.funeral(Kalmia.SERVER :: logout);

        // Add to subscriber list.
        active(router);
    }
}
