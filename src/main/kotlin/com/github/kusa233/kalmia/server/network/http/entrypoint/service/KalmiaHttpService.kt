package com.github.kusa233.kalmia.server.network.http.entrypoint.service

import com.github.kusa233.kalmia.launch.config.KalmiaLaunchConfig
import com.github.kusa233.kalmia.server.network.http.KalmiaHttpServer
import com.github.kusa233.kalmia.server.network.http.builder.KalmiaHttpGraph

object KalmiaHttpService {
    fun start(graph: KalmiaHttpGraph) {
        KalmiaHttpServer(graph).start()
    }
}