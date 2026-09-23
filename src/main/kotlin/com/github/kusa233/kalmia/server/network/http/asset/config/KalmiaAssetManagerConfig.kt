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
                ifBoolean("cache_assets") {
                    config.cache = this
                }

                config
            }
        }
    }

    private var enable: Boolean = true
    private var assetPath: String = "assets/"
    private var errorPage: String = ""
    private var cache: Boolean = true

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

    override fun toJSON(): JSONObject {
        return JSONObject {
            "enable" set enable
            "asset_path" set assetPath
            "error_page" set errorPage
            "cache" set cache
        }
    }
}