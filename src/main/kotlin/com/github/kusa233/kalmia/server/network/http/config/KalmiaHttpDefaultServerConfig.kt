package com.github.kusa233.kalmia.server.network.http.config

import com.github.kusa233.kalmia.server.network.config.KalmiaNettyServerConfig
import com.github.kusa233.kalmia.server.network.http.asset.config.KalmiaAssetManagerConfig

object KalmiaHttpDefaultServerConfig: KalmiaHttpServerConfig() {
    private fun throwWhenSet(): Nothing {
        error("Cannot set config in default server config instance")
    }

    override fun serverPort(port: Int): KalmiaHttpServerConfig {
        throwWhenSet()
    }

    override fun serverHost(host: String): KalmiaHttpServerConfig {
        throwWhenSet()
    }

    override fun assetManagerConfig(config: KalmiaAssetManagerConfig): KalmiaHttpServerConfig {
        throwWhenSet()
    }

    override fun nettyServerConfig(config: KalmiaNettyServerConfig): KalmiaHttpServerConfig {
        throwWhenSet()
    }
}