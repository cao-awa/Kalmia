package com.github.kusa233.kalmia.server.network.http.exception.version

import com.github.kusa233.kalmia.server.network.http.exception.KalmiaHttpException

class NotSupportedHttpVersionException(method: String): KalmiaHttpException("HTTP version not supported: $method")