package com.github.kusa233.kalmia.server.network.websocket.client.adapter

import com.github.kusa233.kalmia.server.network.websocket.client.KalmiaWebSocketClient
import io.netty.channel.ChannelHandlerContext
import io.netty.channel.SimpleChannelInboundHandler
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame
import io.netty.handler.codec.http.websocketx.WebSocketFrame

class KalmiaWebSocketClientAdapter(
    private val client: KalmiaWebSocketClient
) : SimpleChannelInboundHandler<WebSocketFrame>() {
    private lateinit var currentContext: ChannelHandlerContext

    @Override
    override fun channelActive(ctx: ChannelHandlerContext) {
        this.client.setAdapter(this)
        this.currentContext = ctx
    }

    override fun channelRead0(ctx: ChannelHandlerContext, msg: WebSocketFrame) {
        this.currentContext = ctx
        this.client.fireMessage(msg)
    }

    fun sendMessage(raw: Any) {
        val msg = when (raw) {
            is String -> TextWebSocketFrame(raw)
            else -> raw
        }
        this.currentContext.writeAndFlush(msg)
    }
}