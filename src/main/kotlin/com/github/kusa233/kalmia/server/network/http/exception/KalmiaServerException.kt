package com.github.kusa233.kalmia.server.network.http.exception

abstract class KalmiaServerException(override val message: String? = null, override val cause: Throwable? = null): KalmiaException(message, cause) {

}