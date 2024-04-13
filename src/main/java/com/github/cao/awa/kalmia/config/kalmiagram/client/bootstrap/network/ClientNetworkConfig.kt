package com.github.cao.awa.kalmia.config.kalmiagram.client.bootstrap.network

import com.alibaba.fastjson2.JSONObject
import com.github.cao.awa.kalmia.config.ConfigElement

class ClientNetworkConfig(
    val connectHost: String, val connectPort: Int, val useEpoll: Boolean
) : ConfigElement() {
    override fun toJSON(): JSONObject {
        val json = JSONObject()
        json["connect-host"] = this.connectHost
        json["connect-port"] = this.connectPort
        json["use-epoll"] = this.useEpoll
        return json
    }

    companion object {
        @JvmStatic
        fun read(json: JSONObject?, compute: ClientNetworkConfig?): ClientNetworkConfig {
            if (compute == null) {
                throw IllegalArgumentException("Compute argument cannot be null")
            }

            if (json == null) {
                return compute
            }

            val bindHost: String = compute(json, "connect-host", compute::connectHost)
            val bindPort: Int = compute(json, "connect-port", compute::connectPort)
            val useEpoll: Boolean = compute(json, "use-epoll", compute::useEpoll)

            return ClientNetworkConfig(bindHost, bindPort, useEpoll)
        }
    }
}
