package com.github.kusa233.kalmia.launch.config

import com.github.cao.awa.cason.obj.JSONObject
import com.github.cao.awa.cason.primary.JSONString
import com.github.kusa233.kalmia.config.KalmiaConfig
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import java.io.File
import java.util.LinkedList

open class KalmiaLaunchConfig: KalmiaConfig() {
    companion object {
        private val LOGGER: Logger = LogManager.getLogger("KalmiaLaunchConfig")

        @JvmStatic
        fun createConfig(file: File): KalmiaLaunchConfig {
            return createConfig(file)  {
                val config = KalmiaLaunchConfig()
                ifBoolean("print_config_details") {
                    config.printConfigDetails = this
                }
                ifString("entrypoint") {
                    if (this != "") {
                        config.entrypoint.clear()
                        config.entrypoint.add(this)
                    }
                }
                ifArray("entrypoint") {
                    if (!isEmpty()) {
                        config.entrypoint.clear()
                        forEach { entrypoint ->
                            if (!entrypoint.isString()) {
                                throw IllegalArgumentException("Entrypoint definition must be string")
                            }
                            if (entrypoint is JSONString) {
                                config.entrypoint.add(entrypoint.asString())
                            }
                        }
                    } else{
                        LOGGER.warn("Entrypoint definition is empty, will use default entrypoint 'com.github.kusa233.kalmia.server.network.http.entrypoint.KalmiaHttpServerEntrypoint#entry'")
                    }
                }

                config
            }
        }
    }

    private var printConfigDetails: Boolean = true
    private var entrypoint: LinkedList<String> =
        LinkedList<String>().also {
            it.add("com.github.kusa233.kalmia.server.network.http.entrypoint.KalmiaHttpServerEntrypoint#start")
        }
    private var error: Throwable? = null
    private var sharedContext: MutableMap<String, String> = mutableMapOf()

    fun printConfigDetails(): Boolean {
        return this.printConfigDetails
    }

    open fun printConfigDetails(print: Boolean): KalmiaLaunchConfig {
        this.printConfigDetails = print
        return this
    }

    fun entrypoint(): LinkedList<String> {
        return this.entrypoint
    }

    open fun entrypoint(entrypoint: LinkedList<String>): KalmiaLaunchConfig {
        this.entrypoint = entrypoint
        return this
    }

    fun error(): Throwable {
        return this.error!!
    }

    open fun error(error: Throwable): KalmiaLaunchConfig {
        this.error = error
        return this
    }

    fun resetError(): KalmiaLaunchConfig {
        this.error = null
        return this
    }

    operator fun set(key: String, data: String) {
        this.sharedContext[key] = data
    }

    operator fun get(key: String): String? {
        return this.sharedContext[key]
    }

    override fun toJSON(): JSONObject {
        return JSONObject {
            "print_config_details" set printConfigDetails
            arr("entrypoint") {
                for (entry in entrypoint) {
                    +entry
                }
            }
        }
    }
}