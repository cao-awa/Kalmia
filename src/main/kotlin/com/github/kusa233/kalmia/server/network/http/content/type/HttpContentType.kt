package com.github.kusa233.kalmia.server.network.http.content.type

@Suppress("unused")
data class HttpContentType(val name: String, val charset: String = "UTF-8") {
    override fun toString(): String {
        return "${this.name};charset=${this.charset};"
    }

    fun charset(charset: String): HttpContentType {
        return HttpContentType(this.name, charset)
    }
}