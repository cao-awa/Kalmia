package com.github.kusa233.kalmia.server.network.websocket.adapter.protocol

import com.github.kusa233.kalmia.server.network.websocket.builder.KalmiaWebsocketServerBuilder
import com.github.kusa233.kalmia.server.network.websocket.config.KalmiaWebSocketServerProtocolConfig
import com.github.kusa233.kalmia.server.network.websocket.config.decoder.KalmiaWebSocketDecoderConfig
import com.github.kusa233.kalmia.server.network.websocket.context.KalmiaWebSocketContext
import com.github.kusa233.kalmia.server.network.websocket.holder.KalmiaTextWebsocketFrameHolder
import com.github.kusa233.kalmia.server.network.websocket.phase.KalmiaWebSocketPhase
import com.github.kusa233.kalmia.server.network.websocket.pipeline.KalmiaWebSocketRequestPipeline
import com.github.kusa233.kalmia.server.network.websocket.protocol.handshake.KalmiaWebSocketServerProtocolHandshakeHandler
import io.netty.buffer.Unpooled
import io.netty.channel.Channel
import io.netty.channel.ChannelFuture
import io.netty.channel.ChannelFutureListener
import io.netty.channel.ChannelHandlerContext
import io.netty.channel.ChannelInboundHandlerAdapter
import io.netty.channel.ChannelOutboundHandler
import io.netty.channel.ChannelPromise
import io.netty.handler.codec.http.websocketx.CloseWebSocketFrame
import io.netty.handler.codec.http.websocketx.PingWebSocketFrame
import io.netty.handler.codec.http.websocketx.PongWebSocketFrame
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame
import io.netty.handler.codec.http.websocketx.Utf8FrameValidator
import io.netty.handler.codec.http.websocketx.WebSocketCloseStatus
import io.netty.handler.codec.http.websocketx.WebSocketHandshakeException
import io.netty.handler.codec.http.websocketx.WebSocketServerHandshakeException
import io.netty.handler.codec.http.websocketx.WebSocketServerHandshaker
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler
import io.netty.util.AttributeKey
import io.netty.util.ReferenceCountUtil
import io.netty.util.concurrent.Future
import io.netty.util.concurrent.PromiseNotifier
import java.net.SocketAddress
import java.nio.channels.ClosedChannelException
import java.util.concurrent.TimeUnit

class KalmiaWebSocketServerProtocolAdapter(
    private val serverConfig: KalmiaWebSocketServerProtocolConfig,
    val pipeline: KalmiaWebSocketRequestPipeline
) : ChannelInboundHandlerAdapter(), ChannelOutboundHandler {
    companion object {
        const val DEFAULT_HANDSHAKE_TIMEOUT_MILLIS: Long = 10000L

        private val HANDSHAKER_ATTR_KEY: AttributeKey<WebSocketServerHandshaker> =
            AttributeKey.valueOf(WebSocketServerHandshaker::class.java, "HANDSHAKER")

        fun getHandshaker(channel: Channel): WebSocketServerHandshaker? {
            return channel.attr(HANDSHAKER_ATTR_KEY).get()
        }

        fun setHandshaker(channel: Channel, handshaker: WebSocketServerHandshaker) {
            channel.attr(HANDSHAKER_ATTR_KEY).set(handshaker)
        }
    }

    private val dropPongFrames: Boolean = this.serverConfig.dropPongFrames
    private val closeStatus: WebSocketCloseStatus = this.serverConfig.sendCloseFrame
    private val forceCloseTimeoutMillis: Long = this.serverConfig.forceCloseTimeoutMillis
    private var closeSent: ChannelPromise? = null
    private var uri: String? = null

    constructor(
        subprotocols: String?,
        forceCloseTimeoutMillis: Long,
        handleCloseFrames: Boolean,
        sendCloseFrame: WebSocketCloseStatus,
        checkStartsWith: Boolean,
        dropPongFrames: Boolean,
        decoderConfig: KalmiaWebSocketDecoderConfig,
        handshakeTimeoutMillis: Long,
        builder: KalmiaWebsocketServerBuilder
    ) : this(
        KalmiaWebSocketServerProtocolConfig(
            subprotocols,
            checkStartsWith,
            handshakeTimeoutMillis,
            forceCloseTimeoutMillis,
            handleCloseFrames,
            sendCloseFrame,
            dropPongFrames,
            decoderConfig
        ),
        builder
    )

    constructor(
        config: KalmiaWebSocketServerProtocolConfig,
        builder: KalmiaWebsocketServerBuilder
    ) : this(config, KalmiaWebSocketRequestPipeline()) {
        builder.applyRoute(this)
    }

    override fun handlerAdded(ctx: ChannelHandlerContext) {
        val pipeline = ctx.pipeline()
        if (pipeline.get(KalmiaWebSocketServerProtocolHandshakeHandler::class.java.getName()) == null) {
            // Add the WebSocketHandshakeHandler before this one.
            pipeline.addBefore(
                ctx.name(),
                KalmiaWebSocketServerProtocolHandshakeHandler::class.java.getName(),
                KalmiaWebSocketServerProtocolHandshakeHandler(this.serverConfig)
            )
        }
        if (
            this.serverConfig.decoderConfig.withUTF8Validator &&
            pipeline.get(Utf8FrameValidator::class.java) == null
        ) {
            // Add the UFT8 checking before this one.
            pipeline.addBefore(
                ctx.name(),
                Utf8FrameValidator::class.java.getName(),
                Utf8FrameValidator(this.serverConfig.decoderConfig.closeOnProtocolViolation)
            )
        }
    }

    override fun userEventTriggered(ctx: ChannelHandlerContext, evt: Any) {
        if (evt is WebSocketServerProtocolHandler.HandshakeComplete) {
            this.uri = evt.requestUri()
        }
    }

    override fun channelRead(ctx: ChannelHandlerContext, frame: Any) {
        if (this.serverConfig.handleCloseFrames && frame is CloseWebSocketFrame) {
            val handshaker: WebSocketServerHandshaker? = getHandshaker(ctx.channel())
            if (handshaker != null) {
                frame.retain()
                val promise = ctx.newPromise()
                this.closeSent = promise
                handshaker.close(ctx, frame, promise)
            } else {
                ctx.writeAndFlush(Unpooled.EMPTY_BUFFER).addListener(ChannelFutureListener.CLOSE)
            }
            return
        }
        if (frame is PingWebSocketFrame) {
            frame.content().retain()
            ctx.writeAndFlush(PongWebSocketFrame(frame.content()))
            readIfNeeded(ctx)
            return
        }
        if (frame is PongWebSocketFrame && this.dropPongFrames) {
            readIfNeeded(ctx)
            return
        }

        if (frame is TextWebSocketFrame) {
            val holder = KalmiaTextWebsocketFrameHolder(frame.retain(), this.uri!!)

            this.pipeline.handle(
                ctx,
                KalmiaWebSocketContext(
                    holder,
                    KalmiaWebSocketPhase.MESSAGE
                )
            )
        }
    }

    override fun close(ctx: ChannelHandlerContext, promise: ChannelPromise?) {
        if (!ctx.channel().isActive) {
            ctx.close(promise)
        } else {
            if (this.closeSent == null) {
                write(ctx, CloseWebSocketFrame(this.closeStatus), ctx.newPromise())
            }
            flush(ctx)
            applyCloseSentTimeout(ctx)
            this.closeSent!!.addListener(object : ChannelFutureListener {
                override fun operationComplete(future: ChannelFuture?) {
                    ctx.close(promise)
                }
            })
        }
    }

    private fun applyCloseSentTimeout(ctx: ChannelHandlerContext) {
        if (this.closeSent!!.isDone || this.forceCloseTimeoutMillis < 0) {
            return
        }

        val timeoutTask: Future<*> = ctx.executor().schedule({
            if (!this.closeSent!!.isDone) {
                this.closeSent!!.tryFailure(buildHandshakeException("send close frame timed out"))
            }
        }, this.forceCloseTimeoutMillis, TimeUnit.MILLISECONDS)

        this.closeSent!!.addListener(object : ChannelFutureListener {
            override fun operationComplete(future: ChannelFuture?) {
                timeoutTask.cancel(false)
            }
        })
    }

    override fun write(ctx: ChannelHandlerContext, msg: Any?, promise: ChannelPromise) {
        if (this.closeSent != null) {
            ReferenceCountUtil.release(msg)
            promise.setFailure(ClosedChannelException())
        } else if (msg is CloseWebSocketFrame) {
            this.closeSent = promise.unvoid()
            ctx.write(msg).addListener(PromiseNotifier(false, this.closeSent))
        } else {
            ctx.write(msg, promise)
        }
    }

    private fun readIfNeeded(ctx: ChannelHandlerContext) {
        if (!ctx.channel().config().isAutoRead) {
            ctx.read()
        }
    }

    fun buildHandshakeException(message: String?): WebSocketServerHandshakeException {
        return WebSocketServerHandshakeException(message)
    }

    override fun bind(
        ctx: ChannelHandlerContext, localAddress: SocketAddress?,
        promise: ChannelPromise?
    ) {
        ctx.bind(localAddress, promise)
    }

    override fun connect(
        ctx: ChannelHandlerContext, remoteAddress: SocketAddress?,
        localAddress: SocketAddress?, promise: ChannelPromise?
    ) {
        ctx.connect(remoteAddress, localAddress, promise)
    }

    override fun disconnect(ctx: ChannelHandlerContext, promise: ChannelPromise?) {
        ctx.disconnect(promise)
    }

    override fun deregister(ctx: ChannelHandlerContext, promise: ChannelPromise?) {
        ctx.deregister(promise)
    }

    override fun read(ctx: ChannelHandlerContext) {
        ctx.read()
    }

    override fun flush(ctx: ChannelHandlerContext) {
        ctx.flush()
    }

    override fun exceptionCaught(ctx: ChannelHandlerContext, cause: Throwable) {
        if (cause is WebSocketHandshakeException) {
            // TODO: handle handshake errors.
        } else {
            ctx.fireExceptionCaught(cause)
            ctx.close()
        }
    }
}