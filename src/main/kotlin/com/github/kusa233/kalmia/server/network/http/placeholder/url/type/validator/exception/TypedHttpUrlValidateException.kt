package com.github.kusa233.kalmia.server.network.http.placeholder.url.type.validator.exception

class TypedHttpUrlValidateException(val msg: String): RuntimeException(msg) {
    companion object {
        fun failed(msg: String): Nothing = throw TypedHttpUrlValidateException(msg)
    }
}