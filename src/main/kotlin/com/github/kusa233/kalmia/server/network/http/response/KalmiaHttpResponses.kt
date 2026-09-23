package com.github.kusa233.kalmia.server.network.http.response

import com.github.kusa233.kalmia.server.network.http.content.type.HttpContentType
import com.github.kusa233.kalmia.server.network.http.content.type.HttpContentTypes
import com.github.kusa233.kalmia.server.network.http.context.KalmiaHttpContext
import io.netty.buffer.Unpooled
import io.netty.handler.codec.http.DefaultFullHttpResponse
import io.netty.handler.codec.http.FullHttpResponse
import io.netty.handler.codec.http.HttpHeaderNames
import io.netty.handler.codec.http.HttpResponseStatus
import io.netty.handler.codec.http.HttpVersion
import io.netty.util.CharsetUtil

object KalmiaHttpResponses {
    fun createDefaultResponse(
        httpVersion: HttpVersion,
        status: HttpResponseStatus
    ): FullHttpResponse {
        return DefaultFullHttpResponse(
            httpVersion,
            status
        )
    }

    fun createDefaultResponse(
        httpVersion: HttpVersion,
        status: HttpResponseStatus,
        message: String
    ): FullHttpResponse {
        return DefaultFullHttpResponse(
            httpVersion,
            status,
            Unpooled.copiedBuffer(
                message,
                CharsetUtil.UTF_8
            )
        )
    }

    fun createDefaultResponse(
        httpVersion: HttpVersion,
        status: HttpResponseStatus,
        message: ByteArray
    ): FullHttpResponse {
        return DefaultFullHttpResponse(
            httpVersion,
            status,
            Unpooled.copiedBuffer(message)
        )
    }

    fun FullHttpResponse.setPlainHeader(): FullHttpResponse {
        setContentType(HttpContentTypes.PLAIN)
        return this
    }

    fun FullHttpResponse.setJSONHeader(): FullHttpResponse {
        setContentType(HttpContentTypes.JSON)
        return this
    }

    fun FullHttpResponse.setContentType(contentType: HttpContentType): FullHttpResponse {
        headers().set(HttpHeaderNames.CONTENT_TYPE, contentType.name)
        return this
    }

    fun FullHttpResponse.setLength(): FullHttpResponse {
        headers().set(HttpHeaderNames.CONTENT_LENGTH, content().readableBytes())
        return this
    }

    fun FullHttpResponse.setLength(length: Long): FullHttpResponse {
        headers().set(HttpHeaderNames.CONTENT_LENGTH, length)
        return this
    }

    fun FullHttpResponse.headers(kalmiaHttpContext: KalmiaHttpContext): FullHttpResponse {
        for ((header, value) in kalmiaHttpContext.responseHeaders()) {
            headers().set(header, value)
        }
        return this
    }
}