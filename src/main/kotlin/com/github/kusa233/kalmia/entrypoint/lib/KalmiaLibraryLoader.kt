package com.github.kusa233.kalmia.entrypoint.lib

import com.github.cao.awa.cason.primary.JSONString
import com.github.cao.awa.cason.serialize.parser.JSONParser
import com.github.kusa233.kalmia.entrypoint.KalmiaEntrypoint
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import java.io.File
import java.net.URL
import java.net.URLClassLoader
import java.util.jar.JarFile
import kotlin.sequences.forEach

object KalmiaLibraryLoader {
    private val LOGGER: Logger = LogManager.getLogger("KalmiaLibraryLoader")
    lateinit var classLoader: URLClassLoader

    @JvmStatic
    fun loadJars() {
        val customLoader = addJarToClassLoader(collectJars(File("libs"), mutableListOf()))

        this.classLoader = customLoader
        Thread.currentThread().contextClassLoader = customLoader

        customLoader.close()
    }

    fun collectJars(file: File, jarFiles: MutableList<File>): MutableList<File> {
        val files = file.listFiles()
        // Only collect libraries when file list present.
        if (files != null) {
            for (file in files) {
                // If is file and extension is .jar, then add to collection.
                if (file.isFile && file.extension == "jar") {
                    jarFiles.add(file)
                } else if (file.isDirectory) {
                    // If file is a directory, load child jars.
                    collectJars(file, jarFiles)
                }
            }
        }
        return jarFiles
    }

    fun addJarToClassLoader(jarFiles: MutableList<File>): URLClassLoader {
        // Map file collection to URL array.
        val urls: Array<URL> = Array(jarFiles.size) { index ->
            jarFiles[index].toURI().toURL()
        }

        // Use custom URL class loader, because Java 9+ default AppClassLoader cannot attach new classes.
        val customLoader = URLClassLoader(urls, ClassLoader.getSystemClassLoader())

        // Load classes.
        for (jarFile in jarFiles) {
            addJarToClassLoader(jarFile, customLoader)
        }

        return customLoader
    }

    fun addJarToClassLoader(jarFile: File, classLoader: ClassLoader) {
        LOGGER.info("Loading library file: {}", jarFile.absolutePath)

        // Add every class in the jar.
        JarFile(jarFile).also { jar ->
            val pluginDefinition = jar.getJarEntry("META-INF/plugin.json")
            if (pluginDefinition != null) {
                LOGGER.info("Loading '{}' as a standard plugin", jarFile.absolutePath)
                val pluginMetadata = JSONParser.parseObject(
                    String(
                        jar.getInputStream(pluginDefinition).readAllBytes()
                    )
                )
                var pluginName = ""
                pluginMetadata.ifString("name") {
                    pluginName = this
                }
                if (pluginName == "") {
                    throw IllegalArgumentException("Plugin definition must define a name")
                }
                var entrypoint = ""
                pluginMetadata.ifString("entrypoint") {
                    entrypoint = this
                }
                if (entrypoint == "") {
                    throw IllegalArgumentException("Plugin definition must define a entrypoint")
                }
                var dependsOn = emptyArray<String>()
                pluginMetadata.ifArray("depends_on") {
                    dependsOn = this.list.map {
                        if (it.isString()) {
                            (it as JSONString).asString()
                        } else {
                            throw IllegalArgumentException("Depends data can only be plugin name string, but got ${it::class.simpleName}")
                        }
                    }.toTypedArray()
                }

                var fallback = ""
                pluginMetadata.ifString("fallback") {
                    fallback = this
                }

                var unload = ""
                pluginMetadata.ifString("unload") {
                    unload = this
                }

                KalmiaEntrypoint.DEPENDENCIES_MANAGER.addPlugin(
                    pluginName,
                    entrypoint,
                    dependsOn,
                    fallback,
                    unload
                )
            } else {
                LOGGER.info("Loading '{}' as a unnamed plugin", jarFile.absolutePath)
            }
        }.let { jar ->
            jar.entries().asSequence()
                .filter {
                    it.name.endsWith(".class")
                }
                .map { entry ->
                    entry.name.removeSuffix(".class").replace('/', '.')
                }
                .forEach { className ->
                    try {
                        classLoader.loadClass(className)
                        LOGGER.debug("Loaded class '{}'", className.replace(".", "/") + ".class")
                    } catch (e: NoClassDefFoundError) {
                        LOGGER.error("Failed to load class '{}'", className, e)
                    }
                }

            jar.close()
        }
    }
}