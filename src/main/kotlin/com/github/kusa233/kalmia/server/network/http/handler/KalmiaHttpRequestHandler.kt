package com.github.kusa233.kalmia.server.network.http.handler

import com.github.kusa233.kalmia.server.network.handler.KalmiaRequestHandler
import com.github.kusa233.kalmia.server.network.http.context.KalmiaHttpContext
import com.github.kusa233.kalmia.server.network.http.context.abort.KalmiaAbortHttpContext
import com.github.kusa233.kalmia.server.network.http.holder.KalmiaFullHttpRequestHolder
import com.github.kusa233.kalmia.server.network.http.exception.path.HttpPathNotRegisteredException
import com.github.kusa233.kalmia.server.network.http.url.KalmiaPlaceholderURL
import com.github.kusa233.kalmia.server.network.http.url.urlParameterRoute
import io.netty.handler.codec.http.HttpMethod
import kotlin.reflect.KClass

abstract class KalmiaHttpRequestHandler(val method: HttpMethod) :
    KalmiaRequestHandler<KalmiaFullHttpRequestHolder, KalmiaHttpContext, KalmiaAbortHttpContext>() {
    private val routes: MutableMap<KalmiaPlaceholderURL, KalmiaHttpContext.() -> Any> = HashMap()
    private val noPlaceholderRoutes: MutableMap<String, KalmiaHttpContext.() -> Any> = HashMap()
    private val exceptionHandlers: MutableMap<KClass<out Throwable>, MutableMap<KalmiaPlaceholderURL, KalmiaAbortHttpContext.(Throwable) -> Any>> =
        mutableMapOf()

    fun route(path: String, handler: KalmiaHttpContext.() -> Any): KalmiaHttpRequestHandler {
        val url = path.urlParameterRoute()
        if (url.hasPlaceholder()) {
            this.routes[url] = handler
        } else {
            this.noPlaceholderRoutes[path] = handler
        }
        return this
    }

    fun routeExceptionHandler(
        path: String,
        type: KClass<out Throwable>,
        handler: KalmiaAbortHttpContext.(Throwable) -> Any
    ): KalmiaHttpRequestHandler {
        if (!this.exceptionHandlers.containsKey(type)) {
            this.exceptionHandlers[type] = mutableMapOf()
        }
        this.exceptionHandlers[type]?.put(path.urlParameterRoute(), handler)
        return this
    }

    override fun hasRoute(path: String): Boolean {
        return this.noPlaceholderRoutes.containsKey(path) || this.routes.containsKey(path.urlParameterRoute())
    }

    override fun handle(context: KalmiaHttpContext): Any {
        val specificRoute = this.noPlaceholderRoutes[context.path()]

        // Pre-fetch to optimization route search speed.
        if (specificRoute != null) {
            return specificRoute(context)
        }

        var matchPlaceholderHandler: (KalmiaHttpContext.() -> Any)? = null
        var placeholderURL: KalmiaPlaceholderURL? = null

        // Search route.
        val url = context.path().urlParameterRoute()
        for ((routeUrl, route) in this.routes) {
            if (routeUrl == url && !routeUrl.hasPlaceholder()) {
                return route(context)
            } else if (url.matchPlaceholder(routeUrl)) {
                matchPlaceholderHandler = route
                placeholderURL = routeUrl
                break
            }
        }

        // If route has placeholder, create context with placeholder.
        if (matchPlaceholderHandler != null && placeholderURL != null) {
            return matchPlaceholderHandler(context.withPlaceholder(placeholderURL))
        }

        // If not found a specific route or placeholder route, throws 404 NOT_FOUND error.
        routeNotFound(context)
    }

    override fun hasAbortHandler(exception: Throwable): Boolean {
        return (this.exceptionHandlers[exception::class]?.size ?: 0) > 0
    }

    override fun handleAbort(
        abortScope: KalmiaAbortHttpContext,
        exception: Throwable,
        responser: (Any) -> Unit
    ): Any {
        val handlers = this.exceptionHandlers[exception::class]

        val handler = handlers?.get(abortScope.path().urlParameterRoute())

        if (handler != null) {
            return responser(handler(abortScope, exception))
        } else if (handlers == null) {
            throw exception
        } else {
            routeNotFound(abortScope)
        }
    }

    fun routeNotFound(context: KalmiaHttpContext): Nothing = HttpPathNotRegisteredException.notFound(context.path())
}