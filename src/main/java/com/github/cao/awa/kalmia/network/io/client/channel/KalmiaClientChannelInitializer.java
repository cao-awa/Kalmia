package com.github.cao.awa.kalmia.network.io.client.channel;

import com.github.cao.awa.apricot.annotation.Stable;
import com.github.cao.awa.kalmia.client.KalmiaClient;
import com.github.cao.awa.kalmia.constant.IntegerConstants;
import com.github.cao.awa.kalmia.network.encode.kalmiagram.RequestDecoder;
import com.github.cao.awa.kalmia.network.encode.kalmiagram.RequestEncoder;
import com.github.cao.awa.kalmia.network.router.kalmia.RequestRouter;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.FixedRecvByteBufAllocator;
import io.netty.channel.socket.SocketChannel;

/**
 * Channel initializer of kalmia client network.
 *
 * @author 草二号机
 * @since 1.0.0
 */
@Stable
public class KalmiaClientChannelInitializer extends ChannelInitializer<SocketChannel> {
    private final KalmiaClient client;

    public KalmiaClientChannelInitializer(KalmiaClient client) {
        this.client = client;
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
        RequestRouter router = new RequestRouter(this.client.activeCallback());
        pipeline.addLast(new RequestDecoder(router));
        pipeline.addLast(new RequestEncoder(router));
        // Do handle.
        pipeline.addLast(router);
    }
}
