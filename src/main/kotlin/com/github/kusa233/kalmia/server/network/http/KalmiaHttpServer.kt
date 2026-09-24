package com.github.kusa233.kalmia.server.network.http

import com.github.kusa233.kalmia.constant.KalmiaInformation
import com.github.kusa233.kalmia.server.network.group.KalmiaEventLoopGroupFactory
import com.github.kusa233.kalmia.server.network.http.builder.KalmiaHttpGraph
import com.github.kusa233.kalmia.server.network.http.adapter.KalmiaHttpInboundHandlerAdapter
import com.github.kusa233.kalmia.server.network.http.config.KalmiaHttpDefaultServerConfig
import com.github.kusa233.kalmia.server.network.http.config.KalmiaHttpServerConfig
import com.github.kusa233.kalmia.status.KalmiaStatus
import com.github.kusa233.kalmia.status.locker.KalmiaHttpServerLocker
import io.netty.bootstrap.ServerBootstrap
import io.netty.channel.ChannelInitializer
import io.netty.channel.ChannelOption
import io.netty.channel.EventLoopGroup
import io.netty.channel.socket.SocketChannel
import io.netty.handler.codec.http.HttpObjectAggregator
import io.netty.handler.codec.http.HttpRequestDecoder
import io.netty.handler.codec.http.HttpResponseEncoder
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import java.io.File

class KalmiaHttpServer {
    companion object {
        private val LOGGER: Logger = LogManager.getLogger("KalmiaHttpServer")
        var instructHttpMetadata: Boolean = true
        var instructHttpStatusCode: Boolean = true
        var instructHttpVersionCode: Boolean = true
        var instructRequestType: Boolean = true
        var instructRequestPath: Boolean = true
    }

    private val locker: KalmiaHttpServerLocker = KalmiaHttpServerLocker()
    private val serverBuilder: KalmiaHttpGraph
    private var running = false

    constructor(builder: KalmiaHttpGraph) {
        this.serverBuilder = builder
    }

    fun start() {
        val serverConfig = KalmiaHttpServerConfig.create(File("configs/kalmia_http.json"))
        start(serverConfig)
    }

    fun start(
        httpServerConfig: KalmiaHttpServerConfig = KalmiaHttpDefaultServerConfig
    ) {
        KalmiaStatus.registerLifecycle("kalmia-http-" + httpServerConfig.serverPort(), this)

        Thread.startVirtualThread {
            val nettyConfig = httpServerConfig.nettyServerConfig()
            val threadFactory = KalmiaEventLoopGroupFactory.validate(nettyConfig.io())
            val bossGroup: EventLoopGroup = threadFactory.createEventLoopGroup(2)
            val workerGroup: EventLoopGroup = threadFactory.createEventLoopGroup(
                Runtime.getRuntime().availableProcessors() * 2
            )
            val adapter = KalmiaHttpInboundHandlerAdapter(this.serverBuilder, httpServerConfig)
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
                        ChannelOption.SO_SNDBUF, nettyConfig.sndBuf()
                    ).childOption(
                        ChannelOption.WRITE_BUFFER_WATER_MARK, nettyConfig.writeBufferWaterMark()
                    ).childOption(
                        ChannelOption.ALLOCATOR, nettyConfig.allocator()
                    ).childHandler(object : ChannelInitializer<SocketChannel>() {
                        @Override
                        override fun initChannel(channel: SocketChannel) {
                            channel.pipeline().apply {
                                addLast(HttpRequestDecoder())
                                addLast(HttpResponseEncoder())
                                // Only aggregate 1MB http request.
                                addLast(HttpObjectAggregator(KalmiaInformation.MB))
                                addLast(adapter)
                            }
                        }
                    })

                val future = bootstrap.bind(
                    httpServerConfig.serverHost(),
                    httpServerConfig.serverPort()
                ).sync()
                this.running = true
                LOGGER.info("Kalmia HTTP server started on port {} on {}", httpServerConfig.serverPort(), httpServerConfig.serverHost())
                future.channel().closeFuture().addListener {
                    LOGGER.info("Stopping Kalmia HTTP server")
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
                bossGroup.shutdownGracefully().sync()
                workerGroup.shutdownGracefully().sync()
            }

            LOGGER.info("Kalmia HTTP server stopped")

            KalmiaStatus.completedLifecycle(this)
        }
    }
}