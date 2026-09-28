package com.github.kusa233.kalmia.server.network.http.error

import com.github.kusa233.kalmia.server.network.http.argument.exception.TypedHttpArgumentMissingException
import com.github.kusa233.kalmia.server.network.http.argument.type.validator.exception.TypedHttpArgumentValidateException
import com.github.kusa233.kalmia.server.network.http.context.KalmiaHttpContext
import com.github.kusa233.kalmia.server.network.http.exception.path.HttpPathNotRegisteredException
import io.netty.handler.codec.http.FullHttpResponse
import io.netty.handler.codec.http.HttpResponseStatus
import io.netty.handler.codec.http.HttpVersion
import kotlin.reflect.KClass

object KalmiaHttpErrors {
    private val ERRORS: MutableMap<KClass<out Throwable>, (HttpVersion, Throwable, String, String, KalmiaHttpContext?) -> FullHttpResponse> = HashMap()

    fun createResponse(httpVersion: HttpVersion, error: Throwable, msg: String, path: String, context: KalmiaHttpContext): FullHttpResponse {
        return KalmiaHttpError(
            context.status(),
            httpVersion,
            error,
            msg,
            path,
            context
        ).createResponse()
    }

    fun adapter(
        status: HttpResponseStatus,
        httpVersion: HttpVersion,
        exception: Throwable,
        message: String,
        requestPath: String,
        context: KalmiaHttpContext?
    ): FullHttpResponse {
        return KalmiaHttpError(
            status,
            httpVersion,
            exception,
            message,
            requestPath,
            context
        ).createResponse()
    }

    fun adapter(httpVersion: HttpVersion, error: Throwable, kalmiaContext: KalmiaHttpContext): FullHttpResponse {
        val errorProducer = ERRORS[error::class]

        if (errorProducer != null) {
            return errorProducer(httpVersion, error, error.message ?: "Unknown error", kalmiaContext.path(), kalmiaContext)
        }
        return createResponse(httpVersion, error, error.message?: "Unknown error", kalmiaContext.path(), kalmiaContext)
    }
}