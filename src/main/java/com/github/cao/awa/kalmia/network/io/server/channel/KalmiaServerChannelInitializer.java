package com.github.cao.awa.kalmia.network.io.server.channel;

import com.github.cao.awa.kalmia.network.router.kalmia.RequestRouter;
import com.github.cao.awa.kalmia.server.KalmiaServer;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

public abstract class KalmiaServerChannelInitializer extends ChannelInitializer<SocketChannel> {
    private static final Logger LOGGER = LogManager.getLogger("KalmiaServerChannelInitializer");
    private final KalmiaServer server;
    private List<RequestRouter> subscriber;

    public KalmiaServerChannelInitializer(KalmiaServer server) {
        this.server = server;
    }

    public KalmiaServer server() {
        return this.server;
    }

    public KalmiaServerChannelInitializer subscribe(List<RequestRouter> routers) {
        this.subscriber = routers;
        return this;
    }

    public KalmiaServerChannelInitializer active(RequestRouter router) {
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

    public KalmiaServerChannelInitializer unsubscribe() {
        this.subscriber = null;
        return this;
    }

    public KalmiaServerChannelInitializer inactive(RequestRouter router) {
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
}
