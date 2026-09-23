package com.github.kusa233.kalmia.server.network.http.entrypoint.service

import com.github.kusa233.kalmia.server.network.http.KalmiaHttpServer
import com.github.kusa233.kalmia.server.network.http.builder.KalmiaHttpServerBuilder

abstract class KalmiaHttpService {
    abstract fun service(): KalmiaHttpServerBuilder

    fun start() {
        KalmiaHttpServer(service()).start()
    }
}