package com.github.kusa233.kalmia.server.network.websocket.protocol.handshake

import com.github.kusa233.kalmia.server.network.websocket.config.KalmiaWebSocketServerProtocolConfig
import com.github.kusa233.kalmia.server.network.websocket.adapter.protocol.KalmiaWebSocketServerProtocolAdapter
import io.netty.channel.*
import io.netty.handler.codec.http.HttpHeaderNames
import io.netty.handler.codec.http.HttpObject
import io.netty.handler.codec.http.HttpRequest
import io.netty.handler.codec.http.websocketx.WebSocketServerHandshakeException
import io.netty.handler.codec.http.websocketx.WebSocketServerHandshakerFactory
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler.HandshakeComplete
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler.ServerHandshakeStateEvent
import io.netty.handler.ssl.SslHandler
import io.netty.util.ReferenceCountUtil
import io.netty.util.concurrent.Future
import io.netty.util.concurrent.FutureListener
import java.util.concurrent.TimeUnit

internal class KalmiaWebSocketServerProtocolHandshakeHandler(
    private val serverConfig: KalmiaWebSocketServerProtocolConfig
) : ChannelInboundHandlerAdapter() {
    companion object {
        fun resolvePath(uri: String): String {
            var endIndex = uri.indexOf('?')
            if (endIndex == -1) {
                endIndex = uri.length
            }
            return uri.substring(0, endIndex)
        }

        @JvmStatic
        fun main(args: Array<String>) {
            println(resolvePath("ws://127.0.0.1/qq"))
        }

        private fun getWebSocketLocation(cp: ChannelPipeline, req: HttpRequest, path: String): String {
            var protocol = "ws"
            if (cp.get(SslHandler::class.java) != null) {
                // SSL in use so use Secure WebSockets
                protocol = "wss"
            }
            val host = req.headers().get(HttpHeaderNames.HOST)
            return "$protocol://$host${resolvePath(path)}"
        }
    }
    private var context: ChannelHandlerContext? = null
    private var handshakePromise: ChannelPromise? = null
    private var isWebSocketPath = false

    override fun handlerAdded(ctx: ChannelHandlerContext) {
        this.context = ctx
        this.handshakePromise = ctx.newPromise()
    }

    @Throws(Exception::class)
    override fun channelRead(context: ChannelHandlerContext, msg: Any?) {
        val httpObject = msg as HttpObject?

        if (httpObject is HttpRequest) {
            this.isWebSocketPath = isWebSocketPath(httpObject)
            if (!this.isWebSocketPath) {
                context.fireChannelRead(msg)
                return
            }

            try {
                val handshaker = WebSocketServerHandshakerFactory.resolveHandshaker(
                    httpObject,
                    getWebSocketLocation(context.pipeline(), httpObject, httpObject.uri()),
                    this.serverConfig.subprotocols,
                    this.serverConfig.decoderConfig.toWebSocketDecoderConfig()
                )
                val localHandshakePromise: ChannelPromise = this.handshakePromise!!
                if (handshaker == null) {
                    WebSocketServerHandshakerFactory.sendUnsupportedVersionResponse(context.channel())
                } else {
                    KalmiaWebSocketServerProtocolAdapter.setHandshaker(context.channel(), handshaker)
                    context.pipeline().remove(this)

                    val handshakeFuture = handshaker.handshake(context.channel(), httpObject)
                    handshakeFuture.addListener(object : ChannelFutureListener {
                        override fun operationComplete(future: ChannelFuture) {
                            if (!future.isSuccess) {
                                localHandshakePromise.tryFailure(future.cause())
                                context.fireExceptionCaught(future.cause())
                            } else {
                                localHandshakePromise.trySuccess()
                                // Kept for compatibility
                                context.fireUserEventTriggered(
                                    ServerHandshakeStateEvent.HANDSHAKE_COMPLETE
                                )
                                context.fireUserEventTriggered(
                                    HandshakeComplete(
                                        httpObject.uri(),
                                        httpObject.headers(),
                                        handshaker.selectedSubprotocol()
                                    )
                                )
                            }
                        }
                    })
                    applyHandshakeTimeout()
                }
            } finally {
                ReferenceCountUtil.release(httpObject)
            }
        } else if (!this.isWebSocketPath) {
            context.fireChannelRead(msg)
        } else {
            ReferenceCountUtil.release(msg)
        }
    }

    private fun isWebSocketPath(req: HttpRequest): Boolean {
        val uri = req.uri()
        val websocketPath = resolvePath(uri)
        return if (this.serverConfig.checkStartsWith) {
            uri.startsWith(websocketPath) && ("/" == websocketPath || checkNextUri(uri, websocketPath))
        } else {
            uri == websocketPath
        }
    }

    private fun checkNextUri(uri: String, websocketPath: String): Boolean {
        val len = websocketPath.length
        if (uri.length > len) {
            val nextUri = uri[len]
            return nextUri == '/' || nextUri == '?'
        }
        return true
    }

    private fun applyHandshakeTimeout() {
        val localHandshakePromise: ChannelPromise = this.handshakePromise!!
        val handshakeTimeoutMillis = this.serverConfig.handshakeTimeoutMillis
        if (handshakeTimeoutMillis <= 0 || localHandshakePromise.isDone) {
            return
        }

        val timeoutFuture: Future<*> = this.context!!.executor().schedule({
            if (!localHandshakePromise.isDone &&
                localHandshakePromise.tryFailure(WebSocketServerHandshakeException("handshake timed out"))
            ) {
                this.context!!.flush()
                    .fireUserEventTriggered(ServerHandshakeStateEvent.HANDSHAKE_TIMEOUT)
                    .close()
            }
        }, handshakeTimeoutMillis, TimeUnit.MILLISECONDS)

        // Cancel the handshake timeout when handshake is finished.
        localHandshakePromise.addListener(object : FutureListener<Void> {
            override fun operationComplete(f: Future<Void>) {
                timeoutFuture.cancel(false)
            }
        })
    }
}
