package com.github.kusa233.kalmia.server.network.websocket.entrypoint.service

import com.github.kusa233.kalmia.server.network.websocket.KalmiaWebSocketServer
import com.github.kusa233.kalmia.server.network.websocket.builder.KalmiaWebsocketGraph

abstract class KalmiaWebSocketService {
    abstract fun service(): KalmiaWebsocketGraph

    fun start() {
        KalmiaWebSocketServer(service()).start()
    }
}