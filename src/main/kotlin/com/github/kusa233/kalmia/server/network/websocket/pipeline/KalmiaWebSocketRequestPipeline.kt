package com.github.kusa233.kalmia.server.network.websocket.pipeline

import com.github.cao.awa.cason.codec.encoder.JSONEncoder
import com.github.cao.awa.cason.obj.JSONObject
import com.github.kusa233.kalmia.server.network.http.error.KalmiaHttpErrors
import com.github.kusa233.kalmia.server.network.pipeline.KalmiaRequestPipeline
import com.github.kusa233.kalmia.server.network.websocket.context.KalmiaWebSocketContext
import com.github.kusa233.kalmia.server.network.websocket.context.abort.KalmiaAbortWebSocketContext
import com.github.kusa233.kalmia.server.network.websocket.handler.KalmiaWebSocketRequestHandler
import com.github.kusa233.kalmia.server.network.websocket.holder.KalmiaTextWebsocketFrameHolder
import com.github.kusa233.kalmia.server.network.websocket.phase.KalmiaWebSocketPhase
import com.github.kusa233.kalmia.server.network.websocket.response.KalmiaWebSocketResponses
import io.netty.channel.ChannelFutureListener
import io.netty.channel.ChannelHandlerContext
import io.netty.handler.codec.http.HttpVersion
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlin.reflect.KClass

class KalmiaWebSocketRequestPipeline :
    KalmiaRequestPipeline<KalmiaTextWebsocketFrameHolder, KalmiaWebSocketContext, KalmiaAbortWebSocketContext, KalmiaWebSocketRequestHandler>() {
    private val handler: KalmiaWebSocketRequestHandler = KalmiaWebSocketRequestHandler()
    private val executionScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    fun route(path: String, phase: KalmiaWebSocketPhase, handler: KalmiaWebSocketContext.() -> Any) {
        this.handler.route(path, phase, handler)
    }

    fun routeExceptionHandler(
        path: String,
        type: KClass<out Throwable>,
        handler: KalmiaAbortWebSocketContext.(Throwable) -> Any
    ) {
        this.handler.routeExceptionHandler(path, type, handler)
    }

    fun handle(handlerContext: ChannelHandlerContext, kalmiaContext: KalmiaWebSocketContext) {
        // Launch on coroutine scope.
        this.executionScope.launch {
            val handler: KalmiaWebSocketRequestHandler = this@KalmiaWebSocketRequestPipeline.handler
            abortable(handlerContext, kalmiaContext, handler) {
                if (handler.hasRoute(kalmiaContext.path(), kalmiaContext.phase)) {
                    response(
                        handlerContext = handlerContext,
                        kalmiaContext = kalmiaContext,
                        response = handler.handle(kalmiaContext)
                    )
                } else {
                    error("Unhandlable on path '${kalmiaContext.path()}'")
                }
            }
        }
    }

    override fun handleException(
        exception: Throwable,
        handlerContext: ChannelHandlerContext,
        kalmiaContext: KalmiaWebSocketContext
    ) {
        // TODO: websocket exception handler, don't use HTTP way.
        handleExceptionCaught(handlerContext, exception)
    }

    fun handleExceptionCaught(handlerContext: ChannelHandlerContext, cause: Throwable) {
        cause.printStackTrace()
        // Response an error message.
        handlerContext.writeAndFlush(
            KalmiaHttpErrors.INTERNAL_SERVER_ERROR(HttpVersion.HTTP_1_0, cause, "Unhandleable request", "{UNKNOWN}", null)
        ).addListener(ChannelFutureListener.CLOSE)
    }

    override fun response(handlerContext: ChannelHandlerContext, kalmiaContext: KalmiaWebSocketContext, response: Any) {
        when (response) {
            is JSONObject -> {
                responseJSON(handlerContext, kalmiaContext) {
                    response
                }
            }

            is Unit -> {
                // Do nothing.
            }

            else -> {
                responseJSON(handlerContext, kalmiaContext) {
                    JSONEncoder.encode(response)
                }
            }
        }
    }

    private fun response(
        handlerContext: ChannelHandlerContext,
        kalmiaContext: KalmiaWebSocketContext,
        response: KalmiaWebSocketContext.() -> String
    ) {
        val msg: String = response(kalmiaContext)

        handlerContext.writeAndFlush(
            KalmiaWebSocketResponses.createDefaultResponse(
                msg
            )
        ).also {
            if (kalmiaContext.isPromiseClose()) {
                it.addListener(ChannelFutureListener.CLOSE)
            }
        }
    }

    private fun responseJSON(
        handlerContext: ChannelHandlerContext,
        kalmiaContext: KalmiaWebSocketContext,
        responser: KalmiaWebSocketContext.() -> JSONObject
    ) {
        val sendingContext = kalmiaContext.createInherited()

        val msg: JSONObject = responser(sendingContext)

        response(handlerContext, sendingContext) {
            JSONEncoder.renderJSON(msg)
        }
    }
}