package com.github.cao.awa.kalmia.config.kalmiagram.meta

import com.alibaba.fastjson2.JSONObject
import com.github.cao.awa.kalmia.config.ConfigElement

class ConfigMeta(val version: Int) : ConfigElement() {
    companion object {
        @JvmStatic
        fun read(json: JSONObject?, compute: ConfigMeta?): ConfigMeta {
            if (compute == null) {
                throw IllegalArgumentException("Compute argument cannot be null")
            }

            if (json == null) {
                return compute
            }

            val version: Int = compute(json, "version", compute::version).coerceAtLeast(compute.version)

            return ConfigMeta(version)
        }
    }

    override fun toJSON(): JSONObject {
        val json = JSONObject()
        json["version"] = this.version
        return json
    }
}
