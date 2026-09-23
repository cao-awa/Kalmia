package com.github.kusa233.kalmia.server.network.websocket.holder

import com.github.kusa233.kalmia.server.network.holder.PathByteBufHolder
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame

class KalmiaTextWebsocketFrameHolder(val msg: TextWebSocketFrame, val uri: String): PathByteBufHolder(msg) {
    fun text(): String = this.msg.text()

    override fun path(): String {
        return this.uri
    }
}