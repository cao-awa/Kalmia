package com.github.kusa233.kalmia.server.network.http.asset.config

object KalmiaAssetManagerDefaultConfig: KalmiaAssetManagerConfig() {
    private fun throwWhenSet() {
        error("Cannot set config in default server config instance")
    }

    override fun enable(enabled: Boolean): KalmiaAssetManagerConfig {
        throwWhenSet()
        return this
    }

    override fun assetPath(assetPath: String): KalmiaAssetManagerConfig {
        throwWhenSet()
        return this
    }

    override fun errorPage(errorPage: String): KalmiaAssetManagerConfig {
        throwWhenSet()
        return this
    }
}