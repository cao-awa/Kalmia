package com.github.kusa233.kalmia.server.network.http.file.header

import com.github.kusa233.kalmia.server.network.http.content.type.HttpContentType
import com.github.kusa233.kalmia.server.network.http.content.type.HttpContentTypes
import java.io.File

object KalmiaHttpFileExtentions {
    private val headers: MutableMap<ByteArray, HttpContentType> = HashMap()

    fun getContentType(file: File): HttpContentType {
        return when (file.extension) {
            "html" -> HttpContentTypes.HTML
            "htm" -> HttpContentTypes.HTML
            "xml" -> HttpContentTypes.XML
            "svg" -> HttpContentTypes.SVG_XML
            "json" -> HttpContentTypes.JSON
            "jar" -> HttpContentTypes.JAVA_ARCHIVE
            "ico" -> HttpContentTypes.X_ICON
            "php" -> HttpContentTypes.PHP
            "css" -> HttpContentTypes.CSS
            "js" -> HttpContentTypes.JAVASCRIPT
            else -> {
                HttpContentTypes.JSON
            }
        }
    }
}