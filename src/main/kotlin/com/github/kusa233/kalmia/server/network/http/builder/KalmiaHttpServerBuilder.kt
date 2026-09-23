package com.github.kusa233.kalmia.server.network.http.builder

import com.github.kusa233.kalmia.server.network.http.builder.route.KalmiaHttpServerRouteBuilder
import com.github.kusa233.kalmia.server.network.http.adapter.KalmiaHttpInboundHandlerAdapter
import com.github.kusa233.kalmia.server.network.http.context.abort.KalmiaAbortHttpContext
import com.github.kusa233.kalmia.server.network.http.placeholder.url.type.TypedHttpUrlPlaceholder
import java.net.URLEncoder
import kotlin.reflect.KClass

class KalmiaHttpServerBuilder {
    private val routes: MutableMap<String, KalmiaHttpServerRouteBuilder> = mutableMapOf()
    private var assetsPath: String = ""
    val abortHandlers: MutableMap<KClass<out Throwable>, KalmiaAbortHttpContext.(Throwable) -> Any> = mutableMapOf()

    constructor(builder: KalmiaHttpServerBuilder.() -> Unit) {
        builder(this)
    }

    fun route(vararg inputs: Any, handler: KalmiaHttpServerRouteBuilder.() -> Unit) {
        val builder = StringBuilder()
        for (input in inputs) {
            when (input) {
                is String -> {
                    builder.append(input)
                }
                is TypedHttpUrlPlaceholder<*> -> {
                    builder.append("{${input.name}}")
                }
                else -> {
                    throw IllegalArgumentException("Unsupported input type: ${input::class}, can only input String or TypedHttpUrlPlaceholder")
                }
            }
            builder.append("/")
        }
        route(builder.toString(), handler)
    }

    fun route(targetPath: String, handler: KalmiaHttpServerRouteBuilder.() -> Unit) {
        var path = targetPath

        path = if (path.endsWith("/")) {
            path.substring(0, path.length - 1)
        } else {
            path
        }

        path = if (path.startsWith("/")) {
            path.substring(1, path.length)
        } else {
            path
        }

        while (path.contains("//")) {
            path = path.replace("//", "/")
        }

        // Encode the path and replace connecting symbol to '%20' .
        path = "${URLEncoder.encode(path, "UTF-8")}"
            .replace("+", "%20")
            .replace("%2F", "/")
            .replace("%7B","{")
            .replace("%7D","}")

        if (!this.routes.containsKey(path)) {
            this.routes[path] = KalmiaHttpServerRouteBuilder(path, handler)
        } else {
            error("Duplicated route path: $path")
        }
    }

    fun assets(path: String) {
        this.assetsPath = path
    }

    fun applyRoute(adapter: KalmiaHttpInboundHandlerAdapter) {
        if (this.assetsPath.isNotEmpty()) {
            adapter.pipeline.setAssetsPath(this.assetsPath)
        }
        for ((key, builder) in this.routes) {
            builder.applyRoute(adapter)
        }
    }

    @Suppress("unchecked_cast")
    fun <T : Throwable> ifAbort(type: KClass<out T>, context: KalmiaAbortHttpContext.(T) -> Any) {
        if (this.abortHandlers.containsKey(type)) {
            throw IllegalStateException("Already presenting an exception handler for type '${type.simpleName}'")
        }
        this.abortHandlers[type] = context as KalmiaAbortHttpContext.(Throwable) -> Any
    }
}

fun http(handler: KalmiaHttpServerBuilder.() -> Unit): KalmiaHttpServerBuilder {
    return KalmiaHttpServerBuilder(handler)
}