package com.github.kusa233.kalmia.server.network.http.asset.config

import com.github.cao.awa.cason.obj.JSONObject
import com.github.kusa233.kalmia.config.KalmiaConfig

open class KalmiaAssetManagerConfig : KalmiaConfig() {
    companion object {
        @JvmStatic
        fun createFromJSON(json: JSONObject): KalmiaAssetManagerConfig {
            return createConfig(json) {
                val config = KalmiaAssetManagerConfig()

                ifBoolean("enable") {
                    config.enable = this
                }
                ifString("asset_path") {
                    config.assetPath = this
                }
                ifString("error_page") {
                    config.errorPage = this
                }
                ifBoolean("cache") {
                    config.cache = this
                }
                ifJSON("headers") {
                    forEach { name, value ->
                        config.headers[name] = value.toString()
                    }
                }

                config
            }
        }
    }

    private var enable: Boolean = true
    private var assetPath: String = "assets/"
    private var errorPage: String = ""
    private var cache: Boolean = true
    private var headers: MutableMap<String, String> = hashMapOf()

    fun enable(): Boolean {
        return this.enable
    }

    open fun enable(enabled: Boolean): KalmiaAssetManagerConfig {
        this.enable = enabled
        return this
    }

    fun assetPath(): String {
        return this.assetPath
    }

    open fun assetPath(assetPath: String): KalmiaAssetManagerConfig {
        this.assetPath = assetPath
        return this
    }

    fun errorPage(): String {
        return this.errorPage
    }

    open fun errorPage(errorPage: String): KalmiaAssetManagerConfig {
        this.errorPage = errorPage
        return this;
    }

    fun cache(): Boolean {
        return this.cache
    }

    open fun cache(cache: Boolean): KalmiaAssetManagerConfig {
        this.cache = cache
        return this
    }

    fun headers(): Map<String, String> {
        return this.headers
    }

    open fun headers(headers: MutableMap<String, String>): KalmiaAssetManagerConfig {
        this.headers = headers
        return this
    }

    fun header(name: String): String? {
        return this.headers[name]
    }

    open fun header(name: String, value: String): KalmiaAssetManagerConfig {
        this.headers[name] = value
        return this
    }

    override fun toJSON(): JSONObject {
        return JSONObject {
            "enable" set enable
            "asset_path" set assetPath
            "error_page" set errorPage
            "cache" set cache
            "headers" set JSONObject {
                for ((name, value) in headers) {
                    name set value
                }
            }
        }
    }
}