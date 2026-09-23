package com.github.kusa233.kalmia.server.network.http.handler.post

import com.github.kusa233.kalmia.server.network.http.handler.KalmiaHttpRequestHandler
import io.netty.handler.codec.http.HttpMethod

class KalmiaHttpPostHandler: KalmiaHttpRequestHandler(HttpMethod.POST)