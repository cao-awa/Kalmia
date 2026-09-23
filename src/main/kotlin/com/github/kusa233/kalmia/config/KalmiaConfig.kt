package com.github.kusa233.kalmia.config

import com.github.cao.awa.cason.obj.JSONObject
import com.github.cao.awa.cason.serialize.parser.JSONParser
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import java.io.File

abstract class KalmiaConfig {
    companion object {
        private val LOGGER: Logger = LogManager.getLogger("KalmiaConfig")
        private val EMPTY_CONFIG: JSONObject = JSONObject()

        fun <T : KalmiaConfig> createConfig(file: File, builder: JSONObject.() -> T): T {
            val config: T =
                if (file.isFile) {
                    LOGGER.info("Creating config from file '{}'", file.absolutePath)
                    try {
                        val config = JSONParser.parseObject(file.readText(Charsets.UTF_8))
                        builder(config)
                    } catch (_: Exception) {
                        configNotFound(file, builder)
                    }
                } else {
                    configNotFound(file, builder)
                }
            file.writeText(config.toJSON().toString(true, "    "))
            return config
        }

        private fun <T : KalmiaConfig> configNotFound(file: File, builder: JSONObject.() -> T): T {
            LOGGER.info("Config not found, creating config to file '{}'", file.absolutePath)
            file.parentFile.let {
                if (!it.exists()) {
                    it.mkdirs()
                }
            }
            return builder(EMPTY_CONFIG)
        }

        fun <T : KalmiaConfig> createConfig(json: JSONObject, builder: JSONObject.() -> T): T {
            return builder(json)
        }
    }

    abstract fun toJSON(): JSONObject
}