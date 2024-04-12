package com.github.cao.awa.kalmia.config.kalmiagram.server.bootstrap.translation

import com.alibaba.fastjson2.JSONObject
import com.github.cao.awa.kalmia.config.ConfigElement

class BootstrapTranslationConfig(val enable: Boolean) : ConfigElement() {
    override fun toJSON(): JSONObject {
        val json = JSONObject()
        json["enable"] = this.enable
        return json
    }

    companion object {
        fun read(json: JSONObject?, compute: BootstrapTranslationConfig?): BootstrapTranslationConfig {
            if (compute == null) {
                throw IllegalArgumentException("Compute argument cannot be null")
            }

            if (json == null) {
                return compute
            }

            val enable: Boolean = compute(json, "enable", compute::enable)

            return BootstrapTranslationConfig(enable)
        }
    }
}
