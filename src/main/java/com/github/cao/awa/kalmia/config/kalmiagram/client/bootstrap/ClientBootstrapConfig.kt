package com.github.cao.awa.kalmia.config.kalmiagram.client.bootstrap

import com.alibaba.fastjson2.JSONObject
import com.github.cao.awa.kalmia.config.ConfigElement
import com.github.cao.awa.kalmia.config.kalmiagram.client.bootstrap.network.ClientNetworkConfig
import com.github.cao.awa.kalmia.config.kalmiagram.meta.ConfigMeta

class ClientBootstrapConfig(
    val meta: ConfigMeta, val clientNetwork: ClientNetworkConfig
) : ConfigElement() {

    override fun toJSON(): JSONObject {
        val json = JSONObject()
        json["config-meta"] = this.meta.toJSON()
        json["client-network"] = this.clientNetwork.toJSON()
        return json
    }

    companion object {
        @JvmStatic
        fun read(json: JSONObject?, compute: ClientBootstrapConfig?): ClientBootstrapConfig {
            if (compute == null) {
                throw IllegalArgumentException("Compute argument cannot be null");
            }

            if (json == null) {
                return compute
            }

            val meta: ConfigMeta = ConfigMeta.read(
                subObject(
                    json, "config-meta"
                ), compute.meta
            )

            val serverNetwork: ClientNetworkConfig = ClientNetworkConfig.read(
                subObject(
                    json, "client-network"
                ), compute.clientNetwork
            )

            return ClientBootstrapConfig(
                meta, serverNetwork
            )
        }
    }
}
