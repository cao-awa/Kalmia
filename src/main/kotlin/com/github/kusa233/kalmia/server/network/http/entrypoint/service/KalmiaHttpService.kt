package com.github.kusa233.kalmia.server.network.http.entrypoint.service

import com.github.kusa233.kalmia.server.network.http.KalmiaHttpServer
import com.github.kusa233.kalmia.server.network.http.builder.KalmiaHttpGraph

abstract class KalmiaHttpService {
    abstract fun service(): KalmiaHttpGraph

    fun start() {
        KalmiaHttpServer(service()).start()
    }
}