package com.github.cao.awa.kalmia.config

import com.alibaba.fastjson2.JSONArray
import com.alibaba.fastjson2.JSONObject
import com.github.zhuaidadaya.rikaishinikui.handler.universal.entrust.EntrustEnvironment

import java.util.function.Supplier

abstract class ConfigElement {
    abstract fun toJSON(): JSONObject

    companion object {
        @JvmStatic
        fun <T> compute(json: JSONObject, key: String, compute: Supplier<T>): T {
            return json[key].let { compute.get() }
        }

        @JvmStatic
        fun subObject(json: JSONObject, key: String): JSONObject {
            return EntrustEnvironment.result(json) { jsonObject -> jsonObject.getJSONObject(key) }
        }

        @JvmStatic
        fun subArray(json: JSONObject, key: String): JSONArray {
            return EntrustEnvironment.result(json) { jsonObject -> jsonObject.getJSONArray(key) }
        }

    }
}
