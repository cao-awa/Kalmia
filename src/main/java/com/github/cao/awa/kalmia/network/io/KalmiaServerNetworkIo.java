package com.github.cao.awa.kalmia.network.io;

import com.github.cao.awa.apricot.anntations.Stable;
import com.github.cao.awa.apricot.thread.pool.*;
import com.github.cao.awa.kalmia.KalmiaServer;
import com.github.cao.awa.kalmia.network.io.channel.KalmiaServerChannelInitializer;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.epoll.Epoll;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollServerSocketChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.ServerSocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Supplier;

/**
 * Network io of apricot bot server.
 *
 * @author 草二号机
 * @author cao_awa
 * @since 1.0.0
 */
@Stable
public class KalmiaServerNetworkIo {
    private static final Logger LOGGER = LogManager.getLogger("ApricotNetworkIo");
    private static final Supplier<NioEventLoopGroup> DEFAULT_CHANNEL = () -> new NioEventLoopGroup(
            0,
            ExecutorFactor.intensiveIo()
    );
    private static final Supplier<EpollEventLoopGroup> EPOLL_CHANNEL = () -> new EpollEventLoopGroup(
            0,
            ExecutorFactor.intensiveIo()
    );

    private final KalmiaServerChannelInitializer channelInitializer;
    private final KalmiaServer server;
    private final List<ChannelFuture> channels = new CopyOnWriteArrayList<>();

    public KalmiaServerNetworkIo(KalmiaServer server) {
        this.server = server;
        this.channelInitializer = new KalmiaServerChannelInitializer(server);
    }

    public void start(final int port) throws Exception {
        boolean expectEpoll = this.server.useEpoll();
        boolean epoll = Epoll.isAvailable();

        LOGGER.info(expectEpoll ?
                    epoll ?
                    "Kalmia network io using Epoll" :
                    "Kalmia network io expected Epoll, but Epoll is not available, switch to NIO" :
                    "Kalmia network io using NIO");

        Supplier<? extends EventLoopGroup> lazy = epoll ? EPOLL_CHANNEL : DEFAULT_CHANNEL;

        Class<? extends ServerSocketChannel> channel = epoll ?
                                                       EpollServerSocketChannel.class :
                                                       NioServerSocketChannel.class;

        EventLoopGroup boss = lazy.get();
        EventLoopGroup worker = lazy.get();
        ServerBootstrap bootstrap = new ServerBootstrap();
        try {
            this.channels.add(bootstrap.channel(channel)
                                       .group(
                                               boss,
                                               worker
                                       )
                                       .option(
                                               ChannelOption.SO_BACKLOG,
                                               256
                                       )
                                       .option(
                                               // Real-time response is necessary
                                               // Enable TCP no delay to improve response speeds
                                               ChannelOption.TCP_NODELAY,
                                               true
                                       )
                                       .childHandler(this.channelInitializer)
                                       .bind(port)
                                       .syncUninterruptibly()
                                       .channel()
                                       .closeFuture()
                                       .sync());
        } finally {
            boss.shutdownGracefully();
            worker.shutdownGracefully();
        }
    }

    public void shutdown() {
        for (ChannelFuture channelFuture : this.channels) {
            try {
                channelFuture.channel()
                             .close()
                             .sync();
            } catch (InterruptedException interruptedException) {
                LOGGER.error("Interrupted whilst closing channel");
            }
        }
    }
}