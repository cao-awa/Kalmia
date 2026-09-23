package com.github.kusa233.kalmia.plugin

import com.github.kusa233.kalmia.entrypoint.KalmiaEntrypoint
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger

class KalmiaPluginDependenciesManager {
    companion object {
        private val LOGGER: Logger = LogManager.getLogger("KalmiaPluginDependenciesManager")
        private val EMPTY_DEPENDENCIES: Array<String> = emptyArray()
    }

    private val plugins: MutableMap<String, KalmiaPlugin> = HashMap()
    private val loadedPlugin: MutableMap<String, KalmiaPlugin> = HashMap()
    private val loadedEntrypoint: MutableMap<String, KalmiaPlugin?> = HashMap()
    private val cleaners: MutableMap<String, () -> Unit> = HashMap()

    fun onPluginLoad(plugin: KalmiaPlugin) {
        this.loadedPlugin[plugin.name] = plugin
    }

    fun onEntrypointLoad(name: String, plugin: KalmiaPlugin?) {
        this.loadedEntrypoint[name] = plugin
    }

    fun isPluginLoaded(name: String): Boolean {
        return loadedPlugin[name] != null
    }

    fun isPluginLoaded(plugin: KalmiaPlugin): Boolean {
        return isPluginLoaded(plugin.name)
    }

    fun addPlugin(name: String, entrypoint: String, dependency: Array<String>, fallback: String, reload: String) {
        this.plugins[name] = KalmiaPlugin(name, entrypoint, dependency, fallback, reload)
    }

    fun getPlugins(): Map<String, KalmiaPlugin> {
        return this.plugins
    }

    fun getLoadedPlugins(): Map<String, KalmiaPlugin> {
        return this.loadedPlugin
    }

    fun getLoadedEntrypoint(): Map<String, KalmiaPlugin?> {
        return this.loadedEntrypoint
    }

    fun isDependsOn(plugin: String, dependency: String): Boolean {
        val dependencies = this.plugins[plugin]
        if (dependencies != null) {
            return dependencies.dependsOn.contains(dependency)
        }
        return false
    }

    fun getDependsOn(plugin: String): Array<String> {
        return this.plugins[plugin]?.dependsOn ?: EMPTY_DEPENDENCIES
    }

    fun getPlugin(name: String): KalmiaPlugin? {
        return this.plugins[name]
    }

    fun registerCleaner(name: String, cleaner: () -> Unit) {
        LOGGER.info("Registering resource cleaner for '$name'")
        this.cleaners[name] = cleaner
    }

    fun getCleaners(): Map<String, () -> Unit> {
        return this.cleaners
    }

    fun clearCleaners() {
        this.cleaners.clear()
    }
}

fun markPluginLoaded(name: String) {
    val dependenciesManager = KalmiaEntrypoint.DEPENDENCIES_MANAGER
    val plugin = dependenciesManager.getPlugin(name)
    if (plugin != null) {
        markPluginLoaded(plugin)
    } else {
        throw IllegalStateException("Plugin '$name' doesn't be loaded with completed plugin, it's unnamed plugin?")
    }
}

fun markPluginLoaded(plugin: KalmiaPlugin) {
    KalmiaEntrypoint.DEPENDENCIES_MANAGER.onPluginLoad(plugin)
}

fun markEntrypointLoaded(name: String, plugin: KalmiaPlugin?) {
    KalmiaEntrypoint.DEPENDENCIES_MANAGER.onEntrypointLoad(name, plugin)
}

fun isPluginLoaded(name: String): Boolean {
    return KalmiaEntrypoint.DEPENDENCIES_MANAGER.isPluginLoaded(name)
}

fun registerCleaner(name: String, cleaner: () -> Unit) {
    KalmiaEntrypoint.DEPENDENCIES_MANAGER.registerCleaner(name, cleaner)
}