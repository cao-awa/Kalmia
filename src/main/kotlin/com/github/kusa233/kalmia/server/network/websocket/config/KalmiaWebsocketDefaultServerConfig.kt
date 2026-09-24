package com.github.kusa233.kalmia.server.network.websocket.config

import com.github.kusa233.kalmia.server.network.config.KalmiaNettyServerConfig
import com.github.kusa233.kalmia.server.network.http.asset.config.KalmiaAssetManagerConfig
import com.github.kusa233.kalmia.server.network.http.config.KalmiaHttpServerConfig

object KalmiaWebsocketDefaultServerConfig: KalmiaWebsocketServerConfig() {
    private fun throwWhenSet(): Nothing {
        error("Cannot set config in default server config instance")
    }

    override fun serverPort(port: Int): KalmiaWebsocketServerConfig {
        throwWhenSet()
    }

    override fun serverHost(host: String): KalmiaWebsocketServerConfig {
        throwWhenSet()
    }

    override fun nettyServerConfig(config: KalmiaNettyServerConfig): KalmiaWebsocketServerConfig {
        throwWhenSet()
    }
}