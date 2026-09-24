package com.github.kusa233.kalmia.server.network.websocket.config

import com.github.cao.awa.cason.obj.JSONObject
import com.github.kusa233.kalmia.config.KalmiaConfig
import com.github.kusa233.kalmia.server.network.config.KalmiaNettyServerConfig
import com.github.kusa233.kalmia.server.network.config.KalmiaNettyServerDefaultConfig
import com.github.kusa233.kalmia.server.network.http.asset.config.KalmiaAssetManagerConfig
import com.github.kusa233.kalmia.server.network.http.asset.config.KalmiaAssetManagerDefaultConfig
import java.io.File

open class KalmiaWebsocketServerConfig: KalmiaConfig() {
    companion object {
        @JvmStatic
        fun create(file: File): KalmiaWebsocketServerConfig {
            return createConfig(file) {
                val config = KalmiaWebsocketServerConfig()
                ifInt("server_port") {
                    config.serverPort = this
                }
                ifString("server_host") {
                    config.serverHost = this
                }
                ifJSON("netty") {
                    config.nettyServerConfig = KalmiaNettyServerConfig.createFromJSON(this)
                }
                config
            }
        }
    }

    private var serverPort: Int = 54321
    private var serverHost: String = "0.0.0.0"
    private var nettyServerConfig: KalmiaNettyServerConfig = KalmiaNettyServerDefaultConfig

    fun serverPort(): Int {
        return this.serverPort
    }

    open fun serverPort(port: Int): KalmiaWebsocketServerConfig {
        this.serverPort = port
        return this
    }

    fun serverHost(): String {
        return this.serverHost
    }

    open fun serverHost(host: String): KalmiaWebsocketServerConfig {
        this.serverHost = host
        return this
    }

    fun nettyServerConfig(): KalmiaNettyServerConfig {
        return this.nettyServerConfig
    }

    open fun nettyServerConfig(config: KalmiaNettyServerConfig): KalmiaWebsocketServerConfig {
        this.nettyServerConfig = config
        return this
    }

    override fun toJSON(): JSONObject {
        return JSONObject {
            "server_port" set serverPort
            "server_host" set serverHost
            "netty" set nettyServerConfig.toJSON()
        }
    }
}