package com.github.kusa233.kalmia.server.network.http.builder.error

import com.github.kusa233.kalmia.server.network.http.adapter.KalmiaHttpInboundHandlerAdapter
import com.github.kusa233.kalmia.server.network.http.context.abort.KalmiaAbortHttpContext
import io.netty.handler.codec.http.HttpMethod
import kotlin.reflect.KClass

class KalmiaHttpRouteExceptionBuilder {
    private val method: HttpMethod
    private val path: String
    val routes: MutableMap<KClass<out Throwable>, KalmiaAbortHttpContext.(Throwable) -> Any> = mutableMapOf()

    constructor(method: HttpMethod, path: String) {
        this.method = method
        this.path = path
    }

    @Suppress("unchecked_cast")
    inline fun <reified T: Throwable, X: Any> ifAbort(target: KClass<T>, noinline handler: KalmiaAbortHttpContext.(T) -> X): KalmiaHttpRouteExceptionBuilder {
        if (this.routes.containsKey(target)) {
            throw IllegalStateException("Already presenting an exception handler for type '${target.simpleName}'")
        }
        this.routes[target] = handler as KalmiaAbortHttpContext.(Throwable) -> Any
        return this
    }

    fun applyRoute(adapter: KalmiaHttpInboundHandlerAdapter) {
        for ((type, handler) in this.routes) {
            adapter.pipeline.getHandler(this.method)?.routeExceptionHandler(this.path, type, handler)
        }
    }
}