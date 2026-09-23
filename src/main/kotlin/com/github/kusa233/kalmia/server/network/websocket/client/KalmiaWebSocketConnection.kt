package com.github.kusa233.kalmia.server.network.websocket.client

import com.github.kusa233.kalmia.server.network.group.KalmiaEventLoopGroupFactory
import com.github.kusa233.kalmia.server.network.websocket.client.adapter.KalmiaWebSocketClientAdapter
import io.netty.bootstrap.Bootstrap
import io.netty.channel.*
import io.netty.channel.socket.SocketChannel
import io.netty.channel.socket.nio.NioSocketChannel
import io.netty.handler.codec.http.DefaultHttpHeaders
import io.netty.handler.codec.http.HttpClientCodec
import io.netty.handler.codec.http.HttpObjectAggregator
import io.netty.handler.codec.http.websocketx.*
import io.netty.handler.codec.http.websocketx.extensions.compression.WebSocketClientCompressionHandler
import java.net.URI

class KalmiaWebSocketConnection(
    private val client: KalmiaWebSocketClient
) {
    lateinit var channel: Channel

    fun connect(host: String, port: Int) {
        val uri = URI("ws://$host:$port")
        val threadFactory = KalmiaEventLoopGroupFactory.remote()
        val group: EventLoopGroup = threadFactory.createEventLoopGroup(1)

        try {
//            val sslCtx = if (uri.scheme == "wss") {
//                SslContextBuilder.forClient()
//                    .trustManager(InsecureTrustManagerFactory.INSTANCE)
//                    .build()
//            } else null

            val handshaker = WebSocketClientHandshakerFactory.newHandshaker(
                uri,
                WebSocketVersion.V13,
                null,
                true,
                DefaultHttpHeaders()
            )

            val bootstrap = Bootstrap()
                .group(group)
                .channel(NioSocketChannel::class.java)
                .handler(object : ChannelInitializer<SocketChannel>() {
                    override fun initChannel(ch: SocketChannel) {
                        val pipeline = ch.pipeline()
//                        sslCtx?.let {
//                            pipeline.addFirst(
//                                it.newHandler(
//                                    ch.alloc(),
//                                    uri.host,
//                                    uri.port
//                                )
//                            )
//                        }

                        pipeline.addLast(
                            HttpClientCodec(),
                            HttpObjectAggregator(8192),
                            WebSocketClientCompressionHandler(8192),
                            WebSocketClientProtocolHandler(
                                uri,
                                WebSocketVersion.V13,
                                null,
                                true,
                                DefaultHttpHeaders(),
                                65536
                            ),
                            KalmiaWebSocketClientAdapter(this@KalmiaWebSocketConnection.client)
                        )
                    }
                })

            val channelFuture = bootstrap.connect(uri.host, uri.port).sync()
            this.channel = channelFuture.channel()
            channelFuture.channel().closeFuture().sync()
        } finally {
            group.shutdownGracefully()
        }
    }

    fun close() {
        this.channel.close()
    }
}