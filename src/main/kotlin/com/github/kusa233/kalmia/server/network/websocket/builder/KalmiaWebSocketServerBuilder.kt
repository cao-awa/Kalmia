package com.github.kusa233.kalmia.server.network.websocket.builder

import com.github.kusa233.kalmia.server.network.websocket.builder.route.KalmiaWebSocketRouteBuilder
import com.github.kusa233.kalmia.server.network.websocket.adapter.protocol.KalmiaWebSocketServerProtocolAdapter
import java.net.URLEncoder

class KalmiaWebsocketServerBuilder {
    private val routes: MutableMap<String, KalmiaWebSocketRouteBuilder> = mutableMapOf()

    constructor(builder: KalmiaWebsocketServerBuilder.() -> Unit) {
        builder(this)
    }

    fun route(targetPath: String, handler: KalmiaWebSocketRouteBuilder.() -> Unit) {
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

        // Encode the path and replace connecting symbol to '%20' .
        path = "/${URLEncoder.encode(path, "UTF-8")}"
            .replace("+", "%20")

        if (!this.routes.containsKey(path)) {
            this.routes[path] = KalmiaWebSocketRouteBuilder(path, handler)
        } else {
            error("Duplicated route path: $path")
        }
    }

    fun applyRoute(adapter: KalmiaWebSocketServerProtocolAdapter) {
        for ((path, builder) in this.routes) {
            builder.applyRoute(adapter)
        }
    }
}

fun websocket(handler: KalmiaWebsocketServerBuilder.() -> Unit): KalmiaWebsocketServerBuilder {
    return KalmiaWebsocketServerBuilder(handler)
}