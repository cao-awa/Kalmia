package com.github.cao.awa.kalmia.config.kalmiagram.server.bootstrap

import com.alibaba.fastjson2.JSONObject
import com.github.cao.awa.kalmia.config.ConfigElement
import com.github.cao.awa.kalmia.config.kalmiagram.meta.ConfigMeta
import com.github.cao.awa.kalmia.config.kalmiagram.server.bootstrap.network.ServerNetworkConfig
import com.github.cao.awa.kalmia.config.kalmiagram.server.bootstrap.translation.BootstrapTranslationConfig

class ServerBootstrapConfig(
    val meta: ConfigMeta,
    val serverNetwork: ServerNetworkConfig,
    val translation: BootstrapTranslationConfig,
    val serverName: String
) : ConfigElement() {
    companion object {
        @JvmStatic
        fun read(json: JSONObject?, compute: ServerBootstrapConfig?): ServerBootstrapConfig {
            if (compute == null) {
                throw IllegalArgumentException("Compute argument cannot be null")
            }

            if (json == null) {
                return compute
            }

            val meta: ConfigMeta = ConfigMeta.read(
                subObject(
                    json, "config-meta"
                ), compute.meta
            )

            val serverNetwork: ServerNetworkConfig =
                ServerNetworkConfig.read(subObject(json, "server-network"), compute.serverNetwork)
            val translation: BootstrapTranslationConfig =
                BootstrapTranslationConfig.read(subObject(json, "translation"), compute.translation)

            val serverName: String = compute(json, "server-name", compute::serverName)

            return ServerBootstrapConfig(meta, serverNetwork, translation, serverName)
        }
    }

    override fun toJSON(): JSONObject {
        val json = JSONObject()
        json["config-meta"] = this.meta.toJSON()
        json["server-network"] = this.serverNetwork.toJSON()
        json["translation"] = this.translation.toJSON()
        json["server-name"] = this.serverName
        return json
    }
}
