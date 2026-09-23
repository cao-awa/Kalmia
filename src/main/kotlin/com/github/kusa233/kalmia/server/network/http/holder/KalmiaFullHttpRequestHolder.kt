package com.github.kusa233.kalmia.server.network.http.holder

import com.github.kusa233.kalmia.server.network.holder.PathByteBufHolder
import io.netty.handler.codec.http.*
import java.net.URLDecoder
import java.nio.charset.StandardCharsets

class KalmiaFullHttpRequestHolder(val msg: FullHttpRequest): PathByteBufHolder(
    msg
) {
    private lateinit var path: String
    private var pathInitialized = false

    override fun path(): String {
        if (!this.pathInitialized) {
            this.path = URLDecoder.decode(this.msg.uri(), StandardCharsets.UTF_8)
        }
        return this.path
    }

    fun method(): HttpMethod = this.msg.method()

    fun protocolVersion(): HttpVersion = this.msg.protocolVersion()

    fun headers(): HttpHeaders = this.msg.headers()
}