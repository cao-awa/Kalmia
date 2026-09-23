package com.github.kusa233.kalmia.server.network.websocket.builder.error

import com.github.kusa233.kalmia.server.network.exception.abort.UnexpectedBehaviorException
import com.github.kusa233.kalmia.server.network.websocket.context.abort.KalmiaAbortWebSocketContext
import com.github.kusa233.kalmia.server.network.websocket.adapter.protocol.KalmiaWebSocketServerProtocolAdapter
import io.netty.handler.codec.http.HttpMethod
import kotlin.reflect.KClass

class KalmiaWebSocketRouteExceptionBuilder {
    private val path: String
    val routes: MutableMap<KClass<out Throwable>, KalmiaAbortWebSocketContext.(Throwable) -> Any> = mutableMapOf()

    constructor(method: HttpMethod, path: String) {
        this.path = path
    }

    @Suppress("unchecked_cast")
    inline fun <reified T: Throwable, X: Any> abort(target: KClass<T>, noinline handler: KalmiaAbortWebSocketContext.(T) -> X): KalmiaWebSocketRouteExceptionBuilder {
        this.routes[target] = handler as KalmiaAbortWebSocketContext.(Throwable) -> Any
        return this
    }

    @Suppress("unchecked_cast")
    fun abort(handler: KalmiaAbortWebSocketContext.(reason: UnexpectedBehaviorException) -> Any): KalmiaWebSocketRouteExceptionBuilder {
        this.routes[UnexpectedBehaviorException::class] = handler as KalmiaAbortWebSocketContext.(Throwable) -> Any
        return this
    }

    fun applyRoute(adapter: KalmiaWebSocketServerProtocolAdapter) {
        for ((type, handler) in this.routes) {
            adapter.pipeline.routeExceptionHandler(this.path, type, handler)
        }
    }
}