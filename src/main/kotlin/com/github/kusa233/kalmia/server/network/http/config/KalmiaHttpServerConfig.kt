package com.github.kusa233.kalmia.server.network.http.config

import com.github.cao.awa.cason.obj.JSONObject
import com.github.kusa233.kalmia.config.KalmiaConfig
import com.github.kusa233.kalmia.server.network.config.KalmiaNettyServerConfig
import com.github.kusa233.kalmia.server.network.config.KalmiaNettyServerDefaultConfig
import com.github.kusa233.kalmia.server.network.http.asset.config.KalmiaAssetManagerConfig
import com.github.kusa233.kalmia.server.network.http.asset.config.KalmiaAssetManagerDefaultConfig
import java.io.File

open class KalmiaHttpServerConfig: KalmiaConfig() {
    companion object {
        @JvmStatic
        fun create(file: File): KalmiaHttpServerConfig {
            return createConfig(file) {
                val config = KalmiaHttpServerConfig()
                ifInt("server_port") {
                    config.serverPort = this
                }
                ifString("server_host") {
                    config.serverHost = this
                }
                ifJSON("asset_manager") {
                    config.assetManagerConfig = KalmiaAssetManagerConfig.createFromJSON(this)
                }
                ifJSON("netty") {
                    config.nettyServerConfig = KalmiaNettyServerConfig.createFromJSON(this)
                }
                config
            }
        }
    }

    private var serverPort: Int = 12345
    private var serverHost: String = "0.0.0.0"
    private var assetManagerConfig: KalmiaAssetManagerConfig = KalmiaAssetManagerDefaultConfig
    private var nettyServerConfig: KalmiaNettyServerConfig = KalmiaNettyServerDefaultConfig

    fun serverPort(): Int {
        return this.serverPort
    }

    open fun serverPort(port: Int): KalmiaHttpServerConfig {
        this.serverPort = port
        return this
    }

    fun serverHost(): String {
        return this.serverHost
    }

    open fun serverHost(host: String): KalmiaHttpServerConfig {
        this.serverHost = host
        return this
    }

    fun assetManagerConfig(): KalmiaAssetManagerConfig {
        return this.assetManagerConfig
    }

    open fun assetManagerConfig(config: KalmiaAssetManagerConfig): KalmiaHttpServerConfig {
        this.assetManagerConfig = config
        return this
    }

    fun nettyServerConfig(): KalmiaNettyServerConfig {
        return this.nettyServerConfig
    }

    open fun nettyServerConfig(config: KalmiaNettyServerConfig): KalmiaHttpServerConfig {
        this.nettyServerConfig = config
        return this
    }

    override fun toJSON(): JSONObject {
        return JSONObject {
            "server_port" set serverPort
            "server_host" set serverHost
            "asset_manager" set assetManagerConfig.toJSON()
            "netty" set nettyServerConfig.toJSON()
        }
    }
}