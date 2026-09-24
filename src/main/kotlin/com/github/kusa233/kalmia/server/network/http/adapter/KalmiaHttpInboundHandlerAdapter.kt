package com.github.kusa233.kalmia.server.network.http.adapter

import com.github.kusa233.kalmia.server.network.http.builder.KalmiaHttpGraph
import com.github.kusa233.kalmia.server.network.http.config.KalmiaHttpServerConfig
import com.github.kusa233.kalmia.server.network.http.pipeline.KalmiaHttpRequestPipeline
import com.github.kusa233.kalmia.server.network.http.context.KalmiaHttpContext
import com.github.kusa233.kalmia.server.network.http.handler.KalmiaHttpRequestAbortHandler
import com.github.kusa233.kalmia.server.network.http.holder.KalmiaFullHttpRequestHolder
import io.netty.channel.ChannelHandler
import io.netty.channel.ChannelHandlerContext
import io.netty.channel.ChannelInboundHandlerAdapter
import io.netty.handler.codec.http.FullHttpRequest

@ChannelHandler.Sharable
class KalmiaHttpInboundHandlerAdapter: ChannelInboundHandlerAdapter {
    val pipeline: KalmiaHttpRequestPipeline
    private val config: KalmiaHttpServerConfig

    constructor(builder: KalmiaHttpGraph, config: KalmiaHttpServerConfig) {
        this.pipeline = KalmiaHttpRequestPipeline(
            KalmiaHttpRequestAbortHandler(builder.abortHandlers),
            config
        )
        this.config = config
        builder.applyRoute(this)
    }

    override fun channelRead(ctx: ChannelHandlerContext, msg: Any) {
        when (msg) {
            is FullHttpRequest -> {
                // Handle full http request,
                this.pipeline.handleFull(ctx, KalmiaHttpContext(KalmiaFullHttpRequestHolder(msg)).also { context ->
                    context.withHost(this.config.serverHost())
                })
            }

            else -> {
                // TODO: handle errors that got not full http request.
            }
        }
    }

    override fun exceptionCaught(ctx: ChannelHandlerContext, cause: Throwable) {
        this.pipeline.handleExceptionCaught(ctx, cause)
    }
}