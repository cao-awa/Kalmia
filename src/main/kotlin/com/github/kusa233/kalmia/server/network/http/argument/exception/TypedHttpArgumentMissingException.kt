package com.github.kusa233.kalmia.server.network.http.argument.exception

class TypedHttpArgumentMissingException(val msg: String): RuntimeException(msg) {
    companion object{
        fun missing(msg: String): Nothing = throw TypedHttpArgumentMissingException(msg)
    }
}