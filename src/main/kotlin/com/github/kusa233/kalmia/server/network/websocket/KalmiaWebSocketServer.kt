package com.github.kusa233.kalmia.server.network.websocket

import com.github.kusa233.kalmia.constant.KalmiaInformation
import com.github.kusa233.kalmia.server.network.group.KalmiaEventLoopGroupFactory
import com.github.kusa233.kalmia.server.network.http.config.KalmiaHttpDefaultServerConfig
import com.github.kusa233.kalmia.server.network.http.config.KalmiaHttpServerConfig
import com.github.kusa233.kalmia.server.network.websocket.builder.KalmiaWebsocketServerBuilder
import com.github.kusa233.kalmia.server.network.websocket.config.KalmiaWebSocketServerProtocolConfig
import com.github.kusa233.kalmia.server.network.websocket.config.decoder.KalmiaWebSocketDecoderConfig
import com.github.kusa233.kalmia.server.network.websocket.adapter.protocol.KalmiaWebSocketServerProtocolAdapter
import io.netty.bootstrap.ServerBootstrap
import io.netty.channel.ChannelInitializer
import io.netty.channel.ChannelOption
import io.netty.channel.EventLoopGroup
import io.netty.channel.socket.SocketChannel
import io.netty.handler.codec.http.HttpObjectAggregator
import io.netty.handler.codec.http.HttpServerCodec
import io.netty.handler.codec.http.websocketx.WebSocketCloseStatus

class KalmiaWebSocketServer {
    private val serverBuilder: KalmiaWebsocketServerBuilder

    constructor(builder: KalmiaWebsocketServerBuilder) {
        this.serverBuilder = builder
    }

    // TODO websocket special supports.
    fun start(
        port: Int,
        address: String = "localhost",
        useEpoll: Boolean = true,
        config: KalmiaHttpServerConfig = KalmiaHttpDefaultServerConfig
    ) {
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
                address,
                port
            ).sync()
            println("Kalmia WebSocket server started on port $port on $address")
            future.channel().closeFuture().sync()
        } finally {
            bossGroup.shutdownGracefully()
            workerGroup.shutdownGracefully()
        }
    }
}