package com.github.kusa233.kalmia.server.network.http.php

import kotlin.jvm.Throws

class KalmiaHttpPHPNotFoundException(msg: String): RuntimeException(msg) {
    companion object {
        @Throws(KalmiaHttpPHPNotFoundException::class)
        fun notFoundPHP(msg: String): Nothing = throw KalmiaHttpPHPNotFoundException(msg)
    }
}