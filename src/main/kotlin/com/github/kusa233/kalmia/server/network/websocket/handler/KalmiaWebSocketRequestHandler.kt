package com.github.kusa233.kalmia.server.network.websocket.handler

import com.github.kusa233.kalmia.server.network.handler.KalmiaRequestHandler
import com.github.kusa233.kalmia.server.network.exception.abort.UnexpectedBehaviorException
import com.github.kusa233.kalmia.server.network.websocket.context.KalmiaWebSocketContext
import com.github.kusa233.kalmia.server.network.websocket.context.abort.KalmiaAbortWebSocketContext
import com.github.kusa233.kalmia.server.network.websocket.holder.KalmiaTextWebsocketFrameHolder
import com.github.kusa233.kalmia.server.network.websocket.phase.KalmiaWebSocketPhase
import io.netty.channel.ChannelHandlerContext
import kotlin.reflect.KClass

class KalmiaWebSocketRequestHandler: KalmiaRequestHandler<KalmiaTextWebsocketFrameHolder, KalmiaWebSocketContext, KalmiaAbortWebSocketContext>() {
    private val routes: MutableMap<String, MutableMap<KalmiaWebSocketPhase, KalmiaWebSocketContext.() -> Any>> = mutableMapOf()
    private val connectionRoutes: MutableMap<KalmiaWebSocketPhase, ChannelHandlerContext.() -> Any> = mutableMapOf()
    private val exceptionHandler: MutableMap<KClass<out Throwable>, MutableMap<String, KalmiaAbortWebSocketContext.(Throwable) -> Any>> =
        mutableMapOf()

    fun route(path: String, phase: KalmiaWebSocketPhase, handler: KalmiaWebSocketContext.() -> Any): KalmiaWebSocketRequestHandler {
        if (!this.routes.containsKey(path)) {
            this.routes[path] = mutableMapOf()
        }
        this.routes[path]?.put(phase, handler)
        return this
    }

    fun connectionRoute(phase: KalmiaWebSocketPhase, builder: ChannelHandlerContext.() -> Any) {
        this.connectionRoutes[phase] = builder
    }

    fun onConnect(context: ChannelHandlerContext): Any? {
        return this.connectionRoutes[KalmiaWebSocketPhase.CONNECT]?.let {
            it(context)
        }
    }

    fun onDisconnect(context: ChannelHandlerContext): Any? {
        return this.connectionRoutes[KalmiaWebSocketPhase.DISCONNECT]?.let {
            it(context)
        }
    }

    fun routeExceptionHandler(
        path: String,
        type: KClass<out Throwable>,
        handler: KalmiaAbortWebSocketContext.(Throwable) -> Any
    ): KalmiaWebSocketRequestHandler {
        if (!this.exceptionHandler.containsKey(type)) {
            this.exceptionHandler[type] = mutableMapOf()
        }
        this.exceptionHandler[type]?.put(path, handler)
        return this
    }

    override fun handle(context: KalmiaWebSocketContext): Any {
        return this.routes[context.path()]?.let {
            it[context.phase]?.let { handler ->
                handler(context)
            }
        } ?: error("Unhandled request for pathing '${context.path()}'")
    }

    override fun hasRoute(path: String): Boolean {
        // Missing phase.
        return false
    }

    fun hasRoute(path: String, phase: KalmiaWebSocketPhase): Boolean {
        return this.routes[path]?.get(phase) != null
    }

    override fun hasAbortHandler(exception: Throwable): Boolean {
        return (this.exceptionHandler[exception::class]?.size ?: 0) > 0
    }

    override fun handleAbort(
        abortScope: KalmiaAbortWebSocketContext,
        exception: Throwable,
        responser: (Any) -> Unit
    ): Any {
        return this.exceptionHandler[exception::class]?.get(abortScope.path())?.let {
            responser(it(abortScope, exception))
        } ?: UnexpectedBehaviorException.abort()
    }
}