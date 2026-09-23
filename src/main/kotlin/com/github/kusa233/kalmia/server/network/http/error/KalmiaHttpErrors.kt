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
    private val ERRORS: MutableMap<KClass<out Throwable>,  (HttpVersion, Throwable, String, String, KalmiaHttpContext?) -> FullHttpResponse> = HashMap()

    val FAILURE_NOT_FULL: (HttpVersion, Throwable, String, String,  KalmiaHttpContext?) -> FullHttpResponse = { httpVersion, exception, _, requestPath, context ->
        KalmiaHttpError(
            HttpResponseStatus.BAD_REQUEST,
            httpVersion,
            exception,
            "Request is not full",
            requestPath,
            context
        ).createResponse()
    }

    val NOT_FOUND: (HttpVersion, Throwable, String, String,   KalmiaHttpContext?) -> FullHttpResponse = { httpVersion, exception, _, requestPath,  context ->
        KalmiaHttpError(
            HttpResponseStatus.NOT_FOUND,
            httpVersion,
            exception,
            "Page not found",
            requestPath,
            context
        ).createResponse()
    }


    val BAD_REQUEST: (HttpVersion, Throwable, String, String,  KalmiaHttpContext?) -> FullHttpResponse = { httpVersion, exception, message, requestPath,  context ->
        KalmiaHttpError(
            HttpResponseStatus.BAD_REQUEST,
            httpVersion,
            exception,
            message,
            requestPath,
            context
        ).createResponse()
    }

    val INTERNAL_SERVER_ERROR: (HttpVersion, Throwable, String, String,   KalmiaHttpContext?) -> FullHttpResponse = { httpVersion, exception, message, requestPath,  context ->
        KalmiaHttpError(
            HttpResponseStatus.INTERNAL_SERVER_ERROR,
            httpVersion,
            exception,
            message,
            requestPath,
            context
        ).createResponse()
    }

    fun adapter(httpVersion: HttpVersion, requestPath: String, error: Throwable): FullHttpResponse {
        val errorProducer = ERRORS[error::class]

        if (errorProducer != null) {
            return errorProducer(httpVersion, error, error.message ?: "Unknown error", requestPath, null)
        }
        return INTERNAL_SERVER_ERROR(httpVersion, error, error.message?: "Unknown error", requestPath, null)
    }

    fun adapter(httpVersion: HttpVersion, error: Throwable, kalmiaContext: KalmiaHttpContext): FullHttpResponse {
        val errorProducer = ERRORS[error::class]

        if (errorProducer != null) {
            return errorProducer(httpVersion, error, error.message ?: "Unknown error", kalmiaContext.path(), kalmiaContext)
        }
        return INTERNAL_SERVER_ERROR(httpVersion, error, error.message?: "Unknown error", kalmiaContext.path(), kalmiaContext)
    }

    init {
        ERRORS[TypedHttpArgumentMissingException::class] = BAD_REQUEST
        ERRORS[TypedHttpArgumentValidateException::class] = BAD_REQUEST
        ERRORS[HttpPathNotRegisteredException::class] = NOT_FOUND
    }
}