package com.github.kusa233.kalmia.server.network.http.asset.manager

import com.github.kusa233.kalmia.constant.KalmiaInformation
import com.github.kusa233.kalmia.server.network.http.asset.KalmiaAsset
import com.github.kusa233.kalmia.server.network.http.asset.KalmiaBinaryAsset
import com.github.kusa233.kalmia.server.network.http.content.type.HttpContentTypes
import com.github.kusa233.kalmia.server.network.http.context.KalmiaHttpContext
import com.github.kusa233.kalmia.server.network.http.file.header.KalmiaHttpFileExtentions
import com.github.kusa233.kalmia.server.network.http.exception.path.HttpPathNotRegisteredException
import com.github.kusa233.kalmia.server.network.http.php.KalmiaHttpPHPNotFoundException
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import java.io.File
import java.io.IOException
import java.nio.charset.StandardCharsets

class KalmiaHttpAssetsManager {
    companion object {
        private val LOGGER: Logger = LogManager.getLogger("KalmiaHttpAssetsManager")
    }

    private var path: String = ""
    private var available: Boolean = false
    private var enableCache: Boolean = true
    private val caches: MutableMap<String, KalmiaAsset<*>> = HashMap()

    fun setAssetsPath(path: String) {
        this.path = File(path).absolutePath
        this.available = true
    }

    fun toggleCache(cache: Boolean) {
        this.enableCache = cache
        if (!cache) {
            this.caches.clear()
        }
    }

    fun available(): Boolean = this.available

    fun hasAsset(context: KalmiaHttpContext): Boolean {
        return createFile(context.path()).isFile
    }

    fun getAsset(context: KalmiaHttpContext): KalmiaAsset<*> {
        val assetName = if (context.redirectAsset() == "") {
            context.path()
        } else {
            context.redirectAsset()
        }
        return getAsset(assetName)
    }

    fun getAsset(name: String): KalmiaAsset<*> {
        val file = createFile(name)
        if (this.enableCache) {
            val cache = this.caches[file.absolutePath]
            if (cache != null) {
                return cache
            }
        }
        if (file.isFile && file.exists()) {
            return KalmiaBinaryAsset(file).also {
                this.caches[file.absolutePath] = it
            }
        }
        HttpPathNotRegisteredException.notFound(name, "auto redirect")
    }

    fun createFile(name: String): File {
        return File("${this.path}/${name}")
    }

    fun handlePhp(context: KalmiaHttpContext, asset: KalmiaAsset<*>): String {
        try {
            val rootFile = File(this.path)
            val process = ProcessBuilder(
                "php-cgi",
                "-d", "default_charset=\"UTF-8\"",
                "-d", "mbstring.http_output=\"UTF-8\"",
                rootFile.absolutePath
            ).also { builder ->
                builder.environment().also { env ->
                    env["SCRIPT_FILENAME"] = asset.file.absolutePath
                    env["SCRIPT_NAME"] = context.path()
                    env["REQUEST_METHOD"] = context.method().name()
                    env["QUERY_STRING"] = context.arguments().toString()
                    env["REQUEST_URI"] = context.fullPath()
                    env["DOCUMENT_ROOT"] = rootFile.absolutePath
                    env["HTTP_HOST"] = context.host()
                    env["REMOTE_ADDR"] = context.host()
                    env["SERVER_SOFTWARE"] = KalmiaInformation.SOFTWARE_NAME

                    env["CONTENT_LENGTH"] = context.contentLength().toString()
                    env["SERVER_PROTOCOL"] = context.protocolVersion().toString()
                    env["CONTENT_TYPE"] = context.contentType().toString()
                    env["REDIRECT_STATUS"] = "200"
                }
            }.start()

            process.outputStream.use {
                it.write(context.content())
            }

            val response = process.inputStream.readBytes()
            process.waitFor()

            val phpResponse = String(response, StandardCharsets.UTF_8)

            val headers: MutableMap<String, Any> = HashMap()

            if (phpResponse.contains("<!DOCTYPE html>")) {
                val headerContent = phpResponse.substring(0, phpResponse.indexOf("<!DOCTYPE html>") - 1)
                val responseContent = phpResponse.substring(phpResponse.indexOf("<!DOCTYPE html>"))

                val headersContent = headerContent.split("\n")

                val status = headersContent[0]

                var index = 1
                while (index < headersContent.size) {
                    val header = headersContent[index]
                    if (header.indexOf(":") == -1) {
                        break
                    }
                    headers[header.substring(0, header.indexOf(":"))] = header.substring(header.indexOf(":") + 1)
                    index++
                }

                return responseContent
            }
            return phpResponse
        } catch (e: IOException) {
            KalmiaHttpPHPNotFoundException.notFoundPHP("Please ensure PHP installed in your server")
        }
    }

    fun createResponse(context: KalmiaHttpContext, asset: KalmiaAsset<*>?): Any {
        if (asset != null) {
            var data = asset.data
            val contentType = KalmiaHttpFileExtentions.getContentType(
                asset.file
            )
            if (contentType == HttpContentTypes.PHP) {
                data = handlePhp(context, asset)
            }
            context.withContentType(
                contentType
            )
            return data
        }
        HttpPathNotRegisteredException.notFound(context.path())
    }

    fun createResponse(context: KalmiaHttpContext): Any {
        return createResponse(context, getAsset(context))
    }
}