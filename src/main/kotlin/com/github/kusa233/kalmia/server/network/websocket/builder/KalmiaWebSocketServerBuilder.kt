package com.github.kusa233.kalmia.server.network.websocket.builder

import com.github.kusa233.kalmia.server.network.websocket.builder.route.KalmiaWebSocketRouteBuilder
import com.github.kusa233.kalmia.server.network.websocket.adapter.protocol.KalmiaWebSocketServerProtocolAdapter
import com.github.kusa233.kalmia.server.network.websocket.context.KalmiaWebSocketContext
import com.github.kusa233.kalmia.server.network.websocket.phase.KalmiaWebSocketPhase
import io.netty.channel.ChannelHandlerContext
import java.net.URLEncoder

class KalmiaWebsocketGraph {
    private val routes: MutableMap<String, KalmiaWebSocketRouteBuilder> = mutableMapOf()
    private val connectionRoute: MutableMap<KalmiaWebSocketPhase, ChannelHandlerContext.() -> Unit> = mutableMapOf()

    constructor(builder: KalmiaWebsocketGraph.() -> Unit) {
        builder(this)
    }

    fun onConnection(builder: ChannelHandlerContext.() -> Unit) {
        this.connectionRoute[KalmiaWebSocketPhase.CONNECT] = builder
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
        for ((_, builder) in this.routes) {
            builder.applyRoute(adapter)
        }

        for ((phase, builder) in this.connectionRoute) {
            adapter.pipeline.connectionRoute(phase, builder)
        }
    }
}

fun websocket(handler: KalmiaWebsocketGraph.() -> Unit): KalmiaWebsocketGraph {
    return KalmiaWebsocketGraph(handler)
}