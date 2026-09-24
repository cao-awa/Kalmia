package com.github.kusa233.kalmia.server.network.websocket

import com.github.kusa233.kalmia.constant.KalmiaInformation
import com.github.kusa233.kalmia.server.network.group.KalmiaEventLoopGroupFactory
import com.github.kusa233.kalmia.server.network.websocket.builder.KalmiaWebsocketGraph
import com.github.kusa233.kalmia.server.network.websocket.config.KalmiaWebSocketServerProtocolConfig
import com.github.kusa233.kalmia.server.network.websocket.config.decoder.KalmiaWebSocketDecoderConfig
import com.github.kusa233.kalmia.server.network.websocket.adapter.protocol.KalmiaWebSocketServerProtocolAdapter
import com.github.kusa233.kalmia.server.network.websocket.config.KalmiaWebsocketDefaultServerConfig
import com.github.kusa233.kalmia.server.network.websocket.config.KalmiaWebsocketServerConfig
import com.github.kusa233.kalmia.status.KalmiaStatus
import com.github.kusa233.kalmia.status.locker.KalmiaHttpServerLocker
import io.netty.bootstrap.ServerBootstrap
import io.netty.channel.ChannelInitializer
import io.netty.channel.ChannelOption
import io.netty.channel.EventLoopGroup
import io.netty.channel.socket.SocketChannel
import io.netty.handler.codec.http.HttpObjectAggregator
import io.netty.handler.codec.http.HttpServerCodec
import io.netty.handler.codec.http.websocketx.WebSocketCloseStatus
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger

class KalmiaWebSocketServer {
    companion object {
        private val LOGGER: Logger = LogManager.getLogger("KalmiaWebSocketServer")
    }
    private val locker: KalmiaHttpServerLocker = KalmiaHttpServerLocker()
    private val serverBuilder: KalmiaWebsocketGraph
    private var running = false

    constructor(builder: KalmiaWebsocketGraph) {
        this.serverBuilder = builder
    }

    // TODO websocket special supports.
    fun start(
        config: KalmiaWebsocketServerConfig = KalmiaWebsocketDefaultServerConfig
    ) {
        val port = config.serverPort()
        val host = config.serverHost()
        KalmiaStatus.registerLifecycle("kalmia-wss-$port", this)

        Thread.startVirtualThread {
            val threadFactory = KalmiaEventLoopGroupFactory.remote()
            val bossGroup: EventLoopGroup = threadFactory.createEventLoopGroup(1)
            val workerGroup: EventLoopGroup =
                threadFactory.createEventLoopGroup(Runtime.getRuntime().availableProcessors() * 2)
            val nettyConfig = config.nettyServerConfig()
            try {
                val bootstrap = ServerBootstrap()
                    .group(
                        bossGroup,
                        workerGroup
                    ).channel(
                        threadFactory.channel
                    ).option(
                        ChannelOption.SO_BACKLOG, nettyConfig.backlog()
                    ).childOption(
                        ChannelOption.TCP_NODELAY, nettyConfig.tcpNoDelay()
                    ).childOption(
                        ChannelOption.SO_KEEPALIVE, nettyConfig.keepalive()
                    ).childOption(
                        ChannelOption.SO_RCVBUF, nettyConfig.rcvBuf()
                    ).childOption(
                        ChannelOption.SO_REUSEADDR, nettyConfig.reuseAddr()
                    ).childOption(
                        ChannelOption.WRITE_BUFFER_WATER_MARK, nettyConfig.writeBufferWaterMark()
                    ).childOption(
                        ChannelOption.ALLOCATOR, nettyConfig.allocator()
                    ).childHandler(object : ChannelInitializer<SocketChannel>() {
                        @Override
                        override fun initChannel(channel: SocketChannel) {
                            channel.pipeline().apply {
                                addLast(HttpServerCodec())
                                // Only aggregate 1MB http request.
                                addLast(HttpObjectAggregator(KalmiaInformation.MB))
                                addLast(
                                    KalmiaWebSocketServerProtocolAdapter(
                                        KalmiaWebSocketServerProtocolConfig(
                                            null,
                                            false,
                                            KalmiaWebSocketServerProtocolAdapter.DEFAULT_HANDSHAKE_TIMEOUT_MILLIS,
                                            0L,
                                            true,
                                            WebSocketCloseStatus.NORMAL_CLOSURE,
                                            true,
                                            KalmiaWebSocketDecoderConfig.DEFAULT
                                        ),
                                        this@KalmiaWebSocketServer.serverBuilder
                                    )
                                )
                            }
                        }
                    })

                val future = bootstrap.bind(
                    host,
                    port
                ).sync()
                this.running = true
                LOGGER.info("Kalmia WebSocket server started on port $port on $host")
                future.channel().closeFuture().addListener {
                    LOGGER.info("Stopping Kalmia WebSocket server")
                }

                KalmiaStatus.registerReloadListener {
                    this.locker.onStop()
                }

                KalmiaStatus.registerStopListener {
                    this.locker.onStop()
                }

                this.locker.await()

                this.running = false
            } finally {
                bossGroup.shutdownGracefully()
                workerGroup.shutdownGracefully()
            }

            LOGGER.info("Kalmia WebSocket server stopped")

            KalmiaStatus.completedLifecycle(this)
        }
    }
}