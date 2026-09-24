package com.github.kusa233.kalmia.server.network.websocket.entrypoint.service

import com.github.kusa233.kalmia.launch.config.KalmiaLaunchConfig
import com.github.kusa233.kalmia.server.network.websocket.KalmiaWebSocketServer
import com.github.kusa233.kalmia.server.network.websocket.builder.KalmiaWebsocketGraph

object KalmiaWebSocketService {
    @JvmStatic
    fun start(graph: KalmiaWebsocketGraph) {
        KalmiaWebSocketServer(graph).start()
    }
}