package com.github.cao.awa.kalmia.config.kalmiagram.meta.network

import com.alibaba.fastjson2.JSONObject
import com.github.cao.awa.kalmia.config.ConfigElement
import com.github.cao.awa.kalmia.config.kalmiagram.meta.ConfigMeta

class RouterNetworkConfig(
    val meta: ConfigMeta, val compressThreshold: Int
) : ConfigElement() {
    override fun toJSON(): JSONObject {
        val json = JSONObject()
        json["config-meta"] = this.meta.toJSON()
        json["compress-threshold"] = this.compressThreshold
        return json
    }

    companion object {
        @JvmStatic
        fun read(json: JSONObject?, compute: RouterNetworkConfig?): RouterNetworkConfig {
            if (compute == null) {
                throw IllegalArgumentException("Compute argument cannot be null")
            }

            if (json == null) {
                return compute
            }

            val meta: ConfigMeta = ConfigMeta.read(subObject(json, "config-meta"), compute.meta)

            val compressThreshold: Int = compute(json, "compress-threshold", compute::compressThreshold)

            return RouterNetworkConfig(meta, compressThreshold)
        }
    }
}
