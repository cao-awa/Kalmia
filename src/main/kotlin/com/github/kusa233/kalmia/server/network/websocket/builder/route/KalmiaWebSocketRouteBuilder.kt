package com.github.kusa233.kalmia.server.network.websocket.builder.route

import com.github.kusa233.kalmia.server.network.websocket.builder.error.KalmiaWebSocketRouteExceptionBuilder
import com.github.kusa233.kalmia.server.network.websocket.context.KalmiaWebSocketContext
import com.github.kusa233.kalmia.server.network.websocket.phase.KalmiaWebSocketPhase
import com.github.kusa233.kalmia.server.network.websocket.adapter.protocol.KalmiaWebSocketServerProtocolAdapter

class KalmiaWebSocketRouteBuilder {
    private val path: String
    val routes: MutableMap<KalmiaWebSocketPhase, KalmiaWebSocketContext.() -> Any> = mutableMapOf()
    val exceptionHandlers: MutableMap<KalmiaWebSocketPhase, KalmiaWebSocketRouteExceptionBuilder> = mutableMapOf()

    constructor(path: String, builder: KalmiaWebSocketRouteBuilder.() -> Unit) {
        this.path = path
        builder(this)
    }

    fun onMessage(builder: KalmiaWebSocketContext.() -> Any) {
        this.routes[KalmiaWebSocketPhase.MESSAGE] = builder
    }

    fun applyRoute(adapter: KalmiaWebSocketServerProtocolAdapter) {
        for ((phase, router) in this.routes) {
            adapter.pipeline.route(this.path, phase, router)
        }
    }
}