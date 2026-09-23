package com.github.kusa233.kalmia.server.network.http.exception.method

import com.github.kusa233.kalmia.server.network.http.exception.KalmiaHttpException

class NotSupportedHttpMethodException(method: String): KalmiaHttpException("HTTP method not supported: $method")