package com.github.kusa233.kalmia.server.network.http.pipeline

import com.github.cao.awa.cason.codec.encoder.JSONEncoder
import com.github.cao.awa.cason.obj.JSONObject
import com.github.kusa233.kalmia.server.network.KalmiaNetworkConfig
import com.github.kusa233.kalmia.server.network.http.KalmiaHttpServer
import com.github.kusa233.kalmia.server.network.http.argument.type.TypedHttpArgument
import com.github.kusa233.kalmia.server.network.http.asset.KalmiaAsset
import com.github.kusa233.kalmia.server.network.http.asset.config.KalmiaAssetManagerConfig
import com.github.kusa233.kalmia.server.network.http.asset.producer.KalmiaAssetProducer
import com.github.kusa233.kalmia.server.network.http.asset.manager.KalmiaHttpAssetsManager
import com.github.kusa233.kalmia.server.network.http.config.KalmiaHttpServerConfig
import com.github.kusa233.kalmia.server.network.http.content.type.HttpContentTypes
import com.github.kusa233.kalmia.server.network.http.context.KalmiaHttpContext
import com.github.kusa233.kalmia.server.network.http.context.abort.KalmiaAbortHttpContext
import com.github.kusa233.kalmia.server.network.http.error.KalmiaHttpErrors
import com.github.kusa233.kalmia.server.network.http.exception.KalmiaServerException
import com.github.kusa233.kalmia.server.network.http.exception.method.NotSupportedHttpMethodException
import com.github.kusa233.kalmia.server.network.http.handler.KalmiaHttpRequestHandler
import com.github.kusa233.kalmia.server.network.http.handler.KalmiaHttpRequestAbortHandler
import com.github.kusa233.kalmia.server.network.http.handler.get.KalmiaHttpGetHandler
import com.github.kusa233.kalmia.server.network.http.handler.post.KalmiaHttpPostHandler
import com.github.kusa233.kalmia.server.network.http.holder.KalmiaFullHttpRequestHolder
import com.github.kusa233.kalmia.server.network.http.metadata.HttpResponseMetadata
import com.github.kusa233.kalmia.server.network.http.exception.path.HttpPathNotRegisteredException
import com.github.kusa233.kalmia.server.network.http.placeholder.url.type.TypedHttpUrlPlaceholder
import com.github.kusa233.kalmia.server.network.http.response.KalmiaHttpResponses
import com.github.kusa233.kalmia.server.network.http.response.KalmiaHttpResponses.headers
import com.github.kusa233.kalmia.server.network.http.response.KalmiaHttpResponses.setContentType
import com.github.kusa233.kalmia.server.network.http.response.KalmiaHttpResponses.setLength
import com.github.kusa233.kalmia.server.network.http.response.content.NoContentResponse
import com.github.kusa233.kalmia.server.network.http.response.redirect.MovedPermanentlyResponse
import com.github.kusa233.kalmia.server.network.http.response.redirect.PermanentlyRedirectResponse
import com.github.kusa233.kalmia.server.network.http.response.redirect.TemporaryRedirectResponse
import com.github.kusa233.kalmia.server.network.pipeline.KalmiaRequestPipeline
import io.netty.channel.ChannelFutureListener
import io.netty.channel.ChannelHandlerContext
import io.netty.handler.codec.http.FullHttpResponse
import io.netty.handler.codec.http.HttpMethod
import io.netty.handler.codec.http.HttpResponseStatus
import io.netty.handler.codec.http.HttpVersion
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.asContextElement
import kotlinx.coroutines.launch
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import org.github.cao.awa.com.github.cao.awa.capertml.html.HTMLElement

class KalmiaHttpRequestPipeline(
    private val serverAbortHandlers: KalmiaHttpRequestAbortHandler,
    private val config: KalmiaHttpServerConfig
) :
    KalmiaRequestPipeline<KalmiaFullHttpRequestHolder, KalmiaHttpContext, KalmiaAbortHttpContext, KalmiaHttpRequestHandler>() {
    companion object {
        private val LOGGER: Logger = LogManager.getLogger("KalmiaHttpRequestPipeline")

        fun instructHttpMetadata(json: JSONObject, context: KalmiaHttpContext): JSONObject {
            json.instruct {
                if (KalmiaHttpServer.instructRequestType) {
                    "request_type" set context.method().name()
                }
                if (KalmiaHttpServer.instructRequestPath) {
                    "request_path" set context.path()
                }
                instructHttpMetadata(
                    this,
                    context.status(),
                    context.protocolVersion(),
                    context.path()
                )
            }

            return json
        }

        fun instructHttpMetadata(
            json: JSONObject,
            status: HttpResponseStatus,
            protocolVersion: HttpVersion,
            requestPath: String
        ): JSONObject {
            json.instruct {
                if (KalmiaNetworkConfig.instructTimestamp) {
                    "timestamp" set System.currentTimeMillis()
                }
                if (KalmiaHttpServer.instructRequestType) {
                    "request_path" set requestPath
                }
                if (KalmiaHttpServer.instructHttpMetadata) {
                    nested("http_meta") {
                        HttpResponseMetadata(
                            if (KalmiaHttpServer.instructHttpStatusCode) {
                                status.code()
                            } else null,
                            if (KalmiaHttpServer.instructHttpVersionCode) {
                                protocolVersion.text()
                            } else null
                        )
                    }
                }
            }

            return json
        }
    }

    private val assetManagerConfig: KalmiaAssetManagerConfig = this.config.assetManagerConfig()
    private val handlers: Map<HttpMethod, KalmiaHttpRequestHandler> =
        HashMap<HttpMethod, KalmiaHttpRequestHandler>().apply {
            put(HttpMethod.GET, KalmiaHttpGetHandler())
            put(HttpMethod.POST, KalmiaHttpPostHandler())
        }
    private val assetsManager: KalmiaHttpAssetsManager = KalmiaHttpAssetsManager()
    private val executionScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    init {
        if (this.assetManagerConfig.enable()) {
            toggleAssetsCache(this.assetManagerConfig.cache())
            setAssetsPath(this.assetManagerConfig.assetPath())
        }
    }

    fun toggleAssetsCache(cache: Boolean) {
        this.assetsManager.toggleCache(cache)
    }

    fun setAssetsPath(path: String) {
        this.assetsManager.setAssetsPath(path)
    }

    fun getAsset(context: KalmiaHttpContext): KalmiaAsset<*> {
        return this.assetsManager.getAsset(context)
    }

    fun getHandler(method: HttpMethod): KalmiaHttpRequestHandler? = this.handlers[method]

    fun handleFull(handlerContext: ChannelHandlerContext, kalmiaContext: KalmiaHttpContext) {
        // Launch on coroutine scope.
        this.executionScope.launch(
            TypedHttpArgument.THREAD_LOCAL.asContextElement(kalmiaContext) + TypedHttpUrlPlaceholder.THREAD_LOCAL.asContextElement(kalmiaContext)
        ) {
            val handler: KalmiaHttpRequestHandler? = handlers[kalmiaContext.method()]
            if (handler != null) {
                // Handle program logics.
                abortable(handlerContext, kalmiaContext, handler) {
                    try {
                        // Try to create a result and response the result.
                        response(
                            handlerContext,
                            kalmiaContext,
                            handler.handle(kalmiaContext)
                        )
                    } catch (e: Throwable) {
                        // When error, default status is 500 INTERNAL_SERVER_ERROR.
                        var httpStatus = HttpResponseStatus.INTERNAL_SERVER_ERROR

                        // When path not registered, use asset manager to delegate the response.
                        if (e is HttpPathNotRegisteredException) {
                            if (assetManagerConfig.enable() && assetsManager.available()) {
                                val asset: KalmiaAsset<*>? = if (assetsManager.hasAsset(kalmiaContext)) {
                                    assetsManager.getAsset(kalmiaContext)
                                } else {
                                    if (assetsManager.createFile(kalmiaContext.path()).isDirectory) {
                                        assetsManager.getAsset(kalmiaContext.path() + "/index.html")
                                    } else {
                                        null
                                    }
                                }

                                // If asset not null. response the asset.
                                if (asset != null) {
                                    response(
                                        handlerContext,
                                        kalmiaContext,
                                        asset
                                    )
                                    return@abortable
                                }
                            } else {
                                // When error is page path not registered and asset manager are not available, it should be 404 NOT_FOUND.
                                httpStatus = HttpResponseStatus.NOT_FOUND
                            }
                        }

                        // Let user handle error if user registered error handler.
                        val abortContext = kalmiaContext.createAbort(httpStatus, kalmiaContext)

                        if (e is KalmiaServerException) {
                            // Handle server level exception.
                            response(
                                handlerContext,
                                kalmiaContext,
                                serverAbortHandlers.handleAbort(
                                    abortContext,
                                    e
                                )
                            )
                        } else {
                            // Handle abort control logic.
                            handler.handleAbort(
                                abortContext,
                                e
                            ) {
                                // Response formatted JSON error or user result.
                                responseExceptionOrData(handlerContext, kalmiaContext, it, e)
                            }
                        }
                    }
                }

                release(kalmiaContext)
            } else {
                release(kalmiaContext)

                // Notice user doesn't register this method handler (like POST, GET or ETC.) and let Kalmia framework handle this error.
                throw NotSupportedHttpMethodException("${kalmiaContext.method().name()} handler not registered")
            }
        }
    }

    fun release(kalmiaContext: KalmiaHttpContext) {
        TypedHttpArgument.THREAD_LOCAL.set(null)
        TypedHttpUrlPlaceholder.THREAD_LOCAL.set(null)
        // Release the msg let GC could be clears,
        kalmiaContext.release()
    }

    fun responseExceptionOrData(
        handlerContext: ChannelHandlerContext,
        kalmiaContext: KalmiaHttpContext,
        response: Any,
        exception: Throwable
    ) {
        if (response is Unit) {
            // Response formatted JSON error response when user doesn't make a result.
            response(handlerContext, kalmiaContext, exception)
        } else {
            // Response user result.
            response(handlerContext, kalmiaContext, response)
        }
    }

    fun handleExceptionCaught(handlerContext: ChannelHandlerContext, cause: Throwable) {
        // Response an error message.
        handlerContext.writeAndFlush(
            KalmiaHttpErrors.INTERNAL_SERVER_ERROR(
                HttpVersion.HTTP_1_1,
                cause,
                cause.message ?: "Unhandled internal server error",
                "{UNKNOWN}",
                null
            )
        ).addListener(ChannelFutureListener.CLOSE)
    }

    override fun handleException(
        exception: Throwable,
        handlerContext: ChannelHandlerContext,
        kalmiaContext: KalmiaHttpContext
    ) {
        // If not handleable, response a formatted error message by KalmiaHttpErrors.adapter formatter.
        if (kalmiaContext.status() == HttpResponseStatus.OK) {
            kalmiaContext.withStatus(HttpResponseStatus.INTERNAL_SERVER_ERROR)
        }
        kalmiaContext.withContentType(HttpContentTypes.JSON)
        handlerContext.writeAndFlush(
            KalmiaHttpErrors.adapter(
                HttpVersion.HTTP_1_0,
                exception,
                kalmiaContext
            ).setContentType(HttpContentTypes.JSON).setLength()
        ).addListener(ChannelFutureListener.CLOSE)
    }

    override fun response(handlerContext: ChannelHandlerContext, kalmiaContext: KalmiaHttpContext, response: Any) {
        val response = when (kalmiaContext.status()) {
            HttpResponseStatus.MOVED_PERMANENTLY -> MovedPermanentlyResponse
            HttpResponseStatus.TEMPORARY_REDIRECT -> TemporaryRedirectResponse
            HttpResponseStatus.PERMANENT_REDIRECT -> PermanentlyRedirectResponse
            else -> response
        }

        when (response) {
            is JSONObject -> {
                responseJSON(handlerContext, kalmiaContext) {
                    response
                }
            }

            is String -> {
                response(handlerContext, kalmiaContext) {
                    kalmiaContext.withContentType(HttpContentTypes.HTML)
                    response
                }
            }

            is NoContentResponse -> {
                responseNoContent(handlerContext, kalmiaContext)
            }

            is MovedPermanentlyResponse -> {
                responseMovedPermanently(handlerContext, kalmiaContext)
            }

            is TemporaryRedirectResponse -> {
                responseMovedPermanently(handlerContext, kalmiaContext)
            }

            is HTMLElement -> {
                response(handlerContext, kalmiaContext) {
                    // Setting content type to HTML to render HTML page.
                    kalmiaContext.withContentType(HttpContentTypes.HTML)
                    response.toString()
                }
            }

            is KalmiaAsset<*>, is KalmiaAssetProducer -> {
                val data: KalmiaAsset<*> = if (response is KalmiaAssetProducer) {
                    response.getAsset(this@KalmiaHttpRequestPipeline)
                } else {
                    response as KalmiaAsset<*>
                }
                response(
                    handlerContext,
                    kalmiaContext,
                    this.assetsManager.createResponse(kalmiaContext, data)
                )
            }

            is Throwable -> {
                responseFull(handlerContext, kalmiaContext) {
                    kalmiaContext.withContentType(HttpContentTypes.JSON)
                    KalmiaHttpErrors.adapter(
                        kalmiaContext.protocolVersion(),
                        kalmiaContext.path(),
                        response
                    )
                }
            }

            is KalmiaHttpContext -> {
                response(
                    handlerContext,
                    kalmiaContext,
                    assetsManager.createResponse(kalmiaContext)
                )
            }

            is Unit -> {
                responseJSON(handlerContext, kalmiaContext) {
                    JSONObject()
                }
            }

            is ByteArray -> {
                responseRaw(handlerContext, kalmiaContext) {
                    response
                }
            }

            else -> {
                responseJSON(handlerContext, kalmiaContext) {
                    JSONEncoder.encodeData(response)
                }
            }
        }
    }

    fun responseMovedPermanently(handlerContext: ChannelHandlerContext, kalmiaContext: KalmiaHttpContext) {
        response(handlerContext, kalmiaContext) {
            // Force be no content status when response is no body response.
            kalmiaContext.withStatus(HttpResponseStatus.MOVED_PERMANENTLY)
            ""
        }
    }

    fun responseTemporaryRedirect(handlerContext: ChannelHandlerContext, kalmiaContext: KalmiaHttpContext) {
        response(handlerContext, kalmiaContext) {
            // Force be no content status when response is no body response.
            kalmiaContext.withStatus(HttpResponseStatus.TEMPORARY_REDIRECT)
            ""
        }
    }

    fun responseNoContent(handlerContext: ChannelHandlerContext, kalmiaContext: KalmiaHttpContext) {
        response(handlerContext, kalmiaContext) {
            // Force be no content status when response is no body response.
            kalmiaContext.withStatus(HttpResponseStatus.NO_CONTENT)
            ""
        }
    }

    private fun response(
        handlerContext: ChannelHandlerContext,
        kalmiaContext: KalmiaHttpContext,
        response: KalmiaHttpContext.() -> String
    ) {
        val msg: String = response(kalmiaContext)

        handlerContext.writeAndFlush(
            KalmiaHttpResponses.createDefaultResponse(
                kalmiaContext.protocolVersion(), kalmiaContext.status(), msg
            ).setContentType(kalmiaContext.contentType())
                .setLength()
                .headers(kalmiaContext)
        ).also {
            if (kalmiaContext.isPromiseClose()) {
                it.addListener(ChannelFutureListener.CLOSE)
            }
        }
    }

    private fun responseRaw(
        handlerContext: ChannelHandlerContext,
        kalmiaContext: KalmiaHttpContext,
        response: KalmiaHttpContext.() -> ByteArray
    ) {
        val msg = response(kalmiaContext)

        handlerContext.writeAndFlush(
            KalmiaHttpResponses.createDefaultResponse(
                kalmiaContext.protocolVersion(), kalmiaContext.status(), msg
            ).setContentType(kalmiaContext.contentType())
                .setLength()
                .headers(kalmiaContext)
        ).also {
            if (kalmiaContext.isPromiseClose()) {
                it.addListener(ChannelFutureListener.CLOSE)
            }
        }
    }

    private fun responseFull(
        handlerContext: ChannelHandlerContext,
        kalmiaContext: KalmiaHttpContext,
        response: KalmiaHttpContext.() -> FullHttpResponse
    ) {
        val msg: FullHttpResponse = response(kalmiaContext)

        handlerContext.writeAndFlush(
            msg.setContentType(kalmiaContext.contentType())
                .setLength()
                .headers(kalmiaContext)
        ).also {
            if (kalmiaContext.isPromiseClose()) {
                it.addListener(ChannelFutureListener.CLOSE)
            }
        }
    }

    private fun responseJSON(
        handlerContext: ChannelHandlerContext,
        kalmiaContext: KalmiaHttpContext,
        responser: KalmiaHttpContext.() -> JSONObject
    ) {
        val sendingContext = kalmiaContext.createInherited()

        val msg: JSONObject = instructHttpMetadata(JSONObject(), sendingContext)
        val data = responser(sendingContext)
        if (data.size() > 0) {
            msg.instruct {
                "data" set data
            }
        }

        sendingContext.withContentType(HttpContentTypes.JSON)

        response(handlerContext, sendingContext) {
            JSONEncoder.renderJSON(msg)
        }
    }
}