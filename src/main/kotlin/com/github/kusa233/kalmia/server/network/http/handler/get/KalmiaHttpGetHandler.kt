package com.github.kusa233.kalmia.server.network.http.handler.get

import com.github.kusa233.kalmia.server.network.http.handler.KalmiaHttpRequestHandler
import io.netty.handler.codec.http.HttpMethod

class KalmiaHttpGetHandler : KalmiaHttpRequestHandler(HttpMethod.GET)