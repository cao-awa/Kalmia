package com.github.cao.awa.kalmia.config.kalmiagram.server.bootstrap.network

import com.alibaba.fastjson2.JSONObject
import com.github.cao.awa.kalmia.config.ConfigElement

class ServerNetworkConfig(val bindHost: String, val bindPort: Int, val useEpoll: Boolean) : ConfigElement() {
    companion object {
        @JvmStatic
        fun read(json: JSONObject?, compute: ServerNetworkConfig?): ServerNetworkConfig {
            if (compute == null) {
                throw IllegalArgumentException("Compute argument cannot be null")
            }

            if (json == null) {
                return compute
            }

            val bindHost: String = compute(json, "bind-host", compute::bindHost)
            val bindPort: Int = compute(json, "bind-port", compute::bindPort)
            val useEpoll: Boolean = compute(json, "use-epoll", compute::useEpoll)

            return ServerNetworkConfig(bindHost, bindPort, useEpoll)
        }
    }

    override fun toJSON(): JSONObject {
        val json = JSONObject();
        json["bind-host"] = this.bindHost
        json["bind-port"] = this.bindPort
        json["use-epoll"] = this.useEpoll
        return json
    }
}
