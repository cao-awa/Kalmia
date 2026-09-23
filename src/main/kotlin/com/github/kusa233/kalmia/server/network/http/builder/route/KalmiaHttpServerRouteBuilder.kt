package com.github.kusa233.kalmia.server.network.http.builder.route

import com.github.kusa233.kalmia.server.network.http.builder.error.KalmiaHttpRouteExceptionBuilder
import com.github.kusa233.kalmia.server.network.http.adapter.KalmiaHttpInboundHandlerAdapter
import com.github.kusa233.kalmia.server.network.http.argument.type.TypedHttpArgument
import com.github.kusa233.kalmia.server.network.http.context.KalmiaHttpContext
import io.netty.handler.codec.http.HttpMethod

class KalmiaHttpServerRouteBuilder {
    companion object {
        fun KalmiaHttpContext.validateArguments(arguments: Array<out TypedHttpArgument<*>>) {
            arguments.forEach { argument: TypedHttpArgument<*> ->
                argument.get(this)
            }
        }
    }

    val path: String
    val routes: MutableMap<HttpMethod, KalmiaHttpContext.() -> Any> = mutableMapOf()
    val exceptionHandlers: MutableMap<HttpMethod, KalmiaHttpRouteExceptionBuilder> = mutableMapOf()

    constructor(path: String, builder: KalmiaHttpServerRouteBuilder.() -> Unit) {
        if (path.startsWith("/")) {
            this.path = path.substringAfter("/")
        } else {
            this.path = path
        }
        builder(this)
    }

    inline fun <reified T : Any> post(noinline handler: KalmiaHttpContext.() -> T): KalmiaHttpRouteExceptionBuilder {
        if (this.routes.containsKey(HttpMethod.POST)) {
            error("Duplicated HTTP POST handler")
        }
        this.routes[HttpMethod.POST] = handler
        return KalmiaHttpRouteExceptionBuilder(HttpMethod.POST, this.path).also {
            this.exceptionHandlers[HttpMethod.POST] = it
        }
    }

    inline fun <reified T : Any> get(noinline handler: KalmiaHttpContext.() -> T): KalmiaHttpRouteExceptionBuilder {
        if (this.routes.containsKey(HttpMethod.POST)) {
            error("Duplicated HTTP GET handler")
        }
        this.routes[HttpMethod.GET] = handler
        return KalmiaHttpRouteExceptionBuilder(HttpMethod.GET, this.path).also {
            this.exceptionHandlers[HttpMethod.GET] = it
        }
    }

    fun applyRoute(adapter: KalmiaHttpInboundHandlerAdapter) {
        this.routes[HttpMethod.POST]?.let { route ->
            routePost(adapter, route)
        }

        this.routes[HttpMethod.GET]?.let { route ->
            routeGet(adapter, route)
        }

        this.exceptionHandlers[HttpMethod.POST]?.let { route ->
            routeExceptionHandler(adapter, route)
        }

        this.exceptionHandlers[HttpMethod.GET]?.let { route ->
            routeExceptionHandler(adapter, route)
        }
    }

    fun routePost(
        adapter: KalmiaHttpInboundHandlerAdapter,
        handler: KalmiaHttpContext.() -> Any
    ) {
        adapter.pipeline.getHandler(HttpMethod.POST)?.route(this.path, handler)
    }

    fun routeExceptionHandler(
        adapter: KalmiaHttpInboundHandlerAdapter,
        handler: KalmiaHttpRouteExceptionBuilder
    ) {
        handler.applyRoute(adapter)
    }

    fun routeGet(
        adapter: KalmiaHttpInboundHandlerAdapter,
        handler: KalmiaHttpContext.() -> Any
    ) {
        adapter.pipeline.getHandler(HttpMethod.GET)?.route(this.path, handler)
    }
}
