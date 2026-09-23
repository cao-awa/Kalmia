package com.github.kusa233.kalmia.server.network.websocket.config

import com.github.kusa233.kalmia.server.network.websocket.config.decoder.KalmiaWebSocketDecoderConfig
import io.netty.handler.codec.http.websocketx.WebSocketCloseStatus

data class KalmiaWebSocketServerProtocolConfig(
    val subprotocols: String?,
    val checkStartsWith: Boolean,
    val handshakeTimeoutMillis: Long,
    val forceCloseTimeoutMillis: Long,
    val handleCloseFrames: Boolean,
    val sendCloseFrame: WebSocketCloseStatus,
    val dropPongFrames: Boolean,
    val decoderConfig: KalmiaWebSocketDecoderConfig
)