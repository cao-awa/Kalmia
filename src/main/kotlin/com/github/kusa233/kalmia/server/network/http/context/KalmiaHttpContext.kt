package com.github.kusa233.kalmia.server.network.http.context

import com.github.cao.awa.cason.serialize.parser.JSONParser
import com.github.kusa233.kalmia.lazy.Lazy1
import com.github.kusa233.kalmia.server.network.context.KalmiaContext
import com.github.kusa233.kalmia.server.network.http.argument.HttpRequestArguments
import com.github.kusa233.kalmia.server.network.http.content.type.HttpContentType
import com.github.kusa233.kalmia.server.network.http.content.type.HttpContentTypes
import com.github.kusa233.kalmia.server.network.http.context.abort.KalmiaAbortHttpContext
import com.github.kusa233.kalmia.server.network.exception.abort.UnexpectedBehaviorException
import com.github.kusa233.kalmia.server.network.holder.PathByteBufHolder
import com.github.kusa233.kalmia.server.network.http.argument.type.TypedHttpArgument
import com.github.kusa233.kalmia.server.network.http.asset.producer.KalmiaAssetProducer
import com.github.kusa233.kalmia.server.network.http.body.KalmiaHttpBody
import com.github.kusa233.kalmia.server.network.http.body.empty.KalmiaHttpEmptyBody
import com.github.kusa233.kalmia.server.network.http.body.exception.UnhandleableRequestBodyException
import com.github.kusa233.kalmia.server.network.http.body.form.urlencoded.KalmiaHttpUrlencodedBody
import com.github.kusa233.kalmia.server.network.http.body.json.KalmiaHttpJsonBody
import com.github.kusa233.kalmia.server.network.http.body.text.KalmiaHttpTextBody
import com.github.kusa233.kalmia.server.network.http.form.encoded.UrlEncodedForm
import com.github.kusa233.kalmia.server.network.http.header.value.KalmiaHttpHeaderValues
import com.github.kusa233.kalmia.server.network.http.holder.KalmiaFullHttpRequestHolder
import com.github.kusa233.kalmia.server.network.http.placeholder.url.type.TypedHttpUrlPlaceholder
import com.github.kusa233.kalmia.server.network.http.url.KalmiaPlaceholderURL
import io.netty.handler.codec.http.DefaultHttpHeaders
import io.netty.handler.codec.http.HttpHeaderNames
import io.netty.handler.codec.http.HttpHeaders
import io.netty.handler.codec.http.HttpMethod
import io.netty.handler.codec.http.HttpResponseStatus
import io.netty.handler.codec.http.HttpVersion
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import org.jetbrains.annotations.Contract
import java.nio.charset.Charset
import java.nio.charset.StandardCharsets
import java.util.Random

@Suppress("unused")
open class KalmiaHttpContext : KalmiaContext<KalmiaFullHttpRequestHolder, KalmiaHttpContext, KalmiaAbortHttpContext> {
    companion object {
        private val LOGGER: Logger = LogManager.getLogger("KalmiaHttpContext")
        private val RANDOM: Random = Random()

        private fun produceArguments(msg: KalmiaFullHttpRequestHolder): HttpRequestArguments {
            return if (msg.path().contains("?")) {
                HttpRequestArguments.build(
                    UrlEncodedForm.build(
                        msg.path().substringAfter("?")
                    )
                )
            } else {
                HttpRequestArguments.EMPTY
            }
        }
    }

    private var msg: PathByteBufHolder
    private var requestId: Long = RANDOM.nextLong()
    private var arguments: HttpRequestArguments = HttpRequestArguments.EMPTY
    private var headers: HttpHeaders = DefaultHttpHeaders()
    private var responseHeaders: HttpHeaders = DefaultHttpHeaders()
    private var body: Lazy1<PathByteBufHolder, KalmiaHttpBody> = Lazy1 { msg ->
        if (msg.content().readableBytes() == 0) {
            return@Lazy1 KalmiaHttpEmptyBody
        } else {
            var contentType = this.headers[HttpHeaderNames.CONTENT_TYPE]
            var charset = StandardCharsets.UTF_8
            if (contentType.contains(";")) {
                for (data in contentType.split(";")) {
                    if (data.contains("charset=")) {
                        charset = Charset.forName(data.substringAfter("charset=").trim())
                        break
                    }
                }
                contentType = contentType.substringBefore(";")
            }
            try {
                return@Lazy1 when (contentType) {
                    KalmiaHttpHeaderValues.APPLICATION_JSON -> {
                        KalmiaHttpJsonBody(JSONParser.parseObject(msg.content().toString(charset)))
                    }

                    KalmiaHttpHeaderValues.TEXT_PLAIN -> {
                        KalmiaHttpTextBody(msg.content().toString(charset))
                    }

                    KalmiaHttpHeaderValues.X_WWW_FORM_URLENCODED -> {
                        KalmiaHttpUrlencodedBody.build(
                            UrlEncodedForm.build(msg.content().toString(charset))
                        )
                    }

                    else -> {
                        throw UnhandleableRequestBodyException("Unsupported content type: $contentType")
                    }
                }
            } catch (e: Exception) {
                LOGGER.warn(
                    "Unable to handle request body '{}', data: {}",
                    contentType,
                    msg.content().toString(charset),
                    e
                )
                LOGGER.warn("Please check the body data is correct format or report this content type to issue")
            }
        }
        return@Lazy1 KalmiaHttpEmptyBody
    }
    private var promiseClose: Boolean = false
    private var status: HttpResponseStatus = HttpResponseStatus.OK
    private var contentType: HttpContentType = HttpContentTypes.PLAIN
    private var protocolVersion: HttpVersion = HttpVersion.HTTP_1_1
    private var method: HttpMethod = HttpMethod.GET
    private var path: String? = null
    private var placeholders: MutableMap<String, Int> = mutableMapOf()
    private var placeholderURL: String = ""
    private var redirectAsset: String = ""
    private var redirectURL: String = ""
    private var dataCache: MutableMap<String, Any> = mutableMapOf()
    private var host: String = "127.0.0.1"

    constructor(msg: KalmiaFullHttpRequestHolder) : super(msg) {
        this.msg = msg
        this.arguments = produceArguments(msg)
        this.headers = msg.headers()
        this.path = super.path().let {
            var result = it
            if (result.contains("?")) {
                result = result.substringBefore("?")
            }
            if (result.endsWith("/")) {
                result.substring(0, it.length - 1)
            } else {
                result
            }
        }
        this.method = msg.method()
    }

    constructor(context: KalmiaHttpContext) : super(context) {
        this.msg = context.msg
        this.arguments = context.arguments
        this.headers = DefaultHttpHeaders()
        this.responseHeaders = DefaultHttpHeaders()
        this.promiseClose = context.promiseClose
        this.status = context.status
        this.contentType = context.contentType
        this.protocolVersion = context.protocolVersion
        this.method = context.method
        this.path = context.path
        this.placeholders = context.placeholders
        this.placeholderURL = context.placeholderURL
        this.redirectAsset = context.redirectAsset
    }

    fun cache(key: String, data: Any): KalmiaHttpContext {
        this.dataCache[key] = data
        return this
    }

    fun fetchCache(key: String): Any? {
        return this.dataCache[key]
    }

    fun withAsset(redirectAsset: String): KalmiaAssetProducer {
        this.redirectAsset = redirectAsset
        return KalmiaAssetProducer(this)
    }

    fun withPlaceholder(url: KalmiaPlaceholderURL): KalmiaHttpContext {
        this.placeholders = url.placeholders()
        this.placeholderURL = url.toString()
        return this
    }

    open fun withStatus(status: HttpResponseStatus): KalmiaHttpContext {
        this.status = status
        when (status) {
            HttpResponseStatus.MOVED_PERMANENTLY,
            HttpResponseStatus.PERMANENT_REDIRECT,
            HttpResponseStatus.TEMPORARY_REDIRECT -> {
                responseHeaders()[HttpHeaderNames.LOCATION] = this.redirectURL
            }
        }
        return this
    }

    open fun withContentType(contentType: HttpContentType): KalmiaHttpContext {
        this.contentType = contentType
        return this
    }

    open fun withProtocolVersion(protocolVersion: HttpVersion): KalmiaHttpContext {
        this.protocolVersion = protocolVersion
        return this
    }

    open fun withHost(host: String): KalmiaHttpContext {
        this.host = host
        return this
    }

    fun redirect(redirectUrl: String, permanently: Boolean = false): KalmiaHttpContext {
        return if (permanently) {
            permanentlyRedirect(redirectUrl)
        } else {
            temporaryRedirect(redirectUrl)
        }
    }

    fun temporaryRedirect(redirectUrl: String): KalmiaHttpContext {
        this.status = HttpResponseStatus.TEMPORARY_REDIRECT
        this.redirectURL = redirectUrl
        return this
    }

    fun permanentlyRedirect(redirectUrl: String): KalmiaHttpContext {
        this.status = HttpResponseStatus.PERMANENT_REDIRECT
        this.redirectURL = redirectUrl
        return this
    }

    fun movedPermanently(redirectUrl: String): KalmiaHttpContext {
        this.status = HttpResponseStatus.MOVED_PERMANENTLY
        this.redirectURL = redirectUrl
        return this
    }

    fun redirectAsset(): String {
        return this.redirectAsset
    }

    fun redirectURL(): String {
        return this.redirectURL
    }

    fun placeholders(): Map<String, Int> {
        return this.placeholders
    }

    fun placeholderURL(): String {
        return this.placeholderURL
    }

    fun arguments(): HttpRequestArguments {
        return this.arguments
    }

    fun body(): KalmiaHttpBody {
        return this.body.get(this.msg)
    }

    fun host(): String {
        return this.host
    }

    fun getHeader(name: String): String? {
        return this.headers[name]
    }

    fun headers(): HttpHeaders {
        return this.headers.copy()
    }

    open fun promiseClose() {
        this.promiseClose = true
    }

    fun isPromiseClose(): Boolean {
        return this.promiseClose
    }

    @Contract(pure = true)
    override fun path(): String {
        return this.path!!
    }

    fun createAbort(
        exception: Exception,
        errorCode: HttpResponseStatus,
        context: KalmiaHttpContext,
        postHandler: (KalmiaAbortHttpContext) -> Unit = { }
    ): KalmiaAbortHttpContext {
        when (errorCode) {
            HttpResponseStatus.OK -> error("Error response cannot use status '200 OK'")
        }
        withStatus(errorCode)
        withContentType(HttpContentTypes.JSON)
        postHandler(context.createAbort())
        return KalmiaAbortHttpContext(this).also {
            postHandler(it)
        }
    }

    fun createAbort(
        httpStatus: HttpResponseStatus,
        context: KalmiaHttpContext,
        postHandler: (KalmiaAbortHttpContext) -> Unit = { }
    ): KalmiaAbortHttpContext {
        return createAbort(
            UnexpectedBehaviorException(httpStatus.reasonPhrase()),
            httpStatus,
            context,
            postHandler
        )
    }

    fun abortWith(
        exception: Exception,
        errorCode: HttpResponseStatus,
        context: KalmiaHttpContext,
        postHandler: (KalmiaAbortHttpContext) -> Unit = { }
    ) {
        when (errorCode) {
            HttpResponseStatus.OK -> error("Error response cannot use status '200 OK'")
        }
        withStatus(errorCode)
        withContentType(HttpContentTypes.JSON)
        postHandler(context.createAbort())
        throw exception
    }

    fun abortWith(
        httpStatus: HttpResponseStatus,
        context: KalmiaHttpContext,
        postHandler: (KalmiaAbortHttpContext) -> Unit = { }
    ) {
        abortWith(
            UnexpectedBehaviorException(httpStatus.reasonPhrase()),
            httpStatus,
            context,
            postHandler
        )
    }

    fun status(): HttpResponseStatus {
        return this.status
    }

    fun contentType(): HttpContentType {
        return this.contentType
    }

    fun contentLength(): Int {
        return content().size
    }

    fun method(): HttpMethod {
        return this.method
    }

    fun protocolVersion(): HttpVersion {
        return this.protocolVersion
    }

    fun responseHeaders(): HttpHeaders {
        return this.responseHeaders
    }

    override fun createInherited(): KalmiaHttpContext {
        return KalmiaHttpContext(this)
    }

    override fun createAbort(): KalmiaAbortHttpContext {
        return KalmiaAbortHttpContext(this)
    }

    // Helper methods.

    // Typed arg.
    inline fun <reified R : Any, reified T1 : Any> build(
        arg1: TypedHttpArgument<T1>,
        builder: (T1) -> R
    ): R {
        return builder(arg1[this])
    }

    inline fun <reified R : Any, reified T1 : Any, reified T2 : Any> build(
        arg1: TypedHttpArgument<T1>,
        arg2: TypedHttpArgument<T2>,
        builder: (T1, T2) -> R
    ): R {
        return builder(arg1[this], arg2[this])
    }

    inline fun <reified R : Any, reified T1 : Any, reified T2 : Any, reified T3 : Any> build(
        arg1: TypedHttpArgument<T1>,
        arg2: TypedHttpArgument<T2>,
        arg3: TypedHttpArgument<T3>,
        builder: (T1, T2, T3) -> R
    ): R {
        return builder(arg1[this], arg2[this], arg3[this])
    }

    inline fun <reified R : Any, reified T1 : Any, reified T2 : Any, reified T3 : Any, reified T4 : Any> build(
        arg1: TypedHttpArgument<T1>,
        arg2: TypedHttpArgument<T2>,
        arg3: TypedHttpArgument<T3>,
        arg4: TypedHttpArgument<T4>,
        builder: (T1, T2, T3, T4) -> R
    ): R {
        return builder(arg1[this], arg2[this], arg3[this], arg4[this])
    }

    inline fun <
            reified R : Any,
            reified T1 : Any,
            reified T2 : Any,
            reified T3 : Any,
            reified T4 : Any,
            reified T5 : Any
            > build(
        arg1: TypedHttpArgument<T1>,
        arg2: TypedHttpArgument<T2>,
        arg3: TypedHttpArgument<T3>,
        arg4: TypedHttpArgument<T4>,
        arg5: TypedHttpArgument<T5>,
        builder: (T1, T2, T3, T4, T5) -> R
    ): R {
        return builder(arg1[this], arg2[this], arg3[this], arg4[this], arg5[this])
    }

    inline fun <
            reified R : Any,
            reified T1 : Any,
            reified T2 : Any,
            reified T3 : Any,
            reified T4 : Any,
            reified T5 : Any,
            reified T6 : Any
            > build(
        arg1: TypedHttpArgument<T1>,
        arg2: TypedHttpArgument<T2>,
        arg3: TypedHttpArgument<T3>,
        arg4: TypedHttpArgument<T4>,
        arg5: TypedHttpArgument<T5>,
        arg6: TypedHttpArgument<T6>,
        builder: (T1, T2, T3, T4, T5, T6) -> R
    ): R {
        return builder(arg1[this], arg2[this], arg3[this], arg4[this], arg5[this], arg6[this])
    }

    inline fun <
            reified R : Any,
            reified T1 : Any,
            reified T2 : Any,
            reified T3 : Any,
            reified T4 : Any,
            reified T5 : Any,
            reified T6 : Any,
            reified T7 : Any
            > build(
        arg1: TypedHttpArgument<T1>,
        arg2: TypedHttpArgument<T2>,
        arg3: TypedHttpArgument<T3>,
        arg4: TypedHttpArgument<T4>,
        arg5: TypedHttpArgument<T5>,
        arg6: TypedHttpArgument<T6>,
        arg7: TypedHttpArgument<T7>,
        builder: (T1, T2, T3, T4, T5, T6, T7) -> R
    ): R {
        return builder(arg1[this], arg2[this], arg3[this], arg4[this], arg5[this], arg6[this], arg7[this])
    }

    // Typed placeholder.
    inline fun <reified R : Any, reified T1 : Any> build(
        arg1: TypedHttpUrlPlaceholder<T1>,
        builder: (T1) -> R
    ): R {
        return builder(arg1[this])
    }

    inline fun <reified R : Any, reified T1 : Any, reified T2 : Any> build(
        arg1: TypedHttpUrlPlaceholder<T1>,
        arg2: TypedHttpUrlPlaceholder<T2>,
        builder: (T1, T2) -> R
    ): R {
        return builder(arg1[this], arg2[this])
    }

    inline fun <reified R : Any, reified T1 : Any, reified T2 : Any, reified T3 : Any> build(
        arg1: TypedHttpUrlPlaceholder<T1>,
        arg2: TypedHttpUrlPlaceholder<T2>,
        arg3: TypedHttpUrlPlaceholder<T3>,
        builder: (T1, T2, T3) -> R
    ): R {
        return builder(arg1[this], arg2[this], arg3[this])
    }

    inline fun <reified R : Any, reified T1 : Any, reified T2 : Any, reified T3 : Any, reified T4 : Any> build(
        arg1: TypedHttpUrlPlaceholder<T1>,
        arg2: TypedHttpUrlPlaceholder<T2>,
        arg3: TypedHttpUrlPlaceholder<T3>,
        arg4: TypedHttpUrlPlaceholder<T4>,
        builder: (T1, T2, T3, T4) -> R
    ): R {
        return builder(arg1[this], arg2[this], arg3[this], arg4[this])
    }

    inline fun <
            reified R : Any,
            reified T1 : Any,
            reified T2 : Any,
            reified T3 : Any,
            reified T4 : Any,
            reified T5 : Any
            > build(
        arg1: TypedHttpUrlPlaceholder<T1>,
        arg2: TypedHttpUrlPlaceholder<T2>,
        arg3: TypedHttpUrlPlaceholder<T3>,
        arg4: TypedHttpUrlPlaceholder<T4>,
        arg5: TypedHttpUrlPlaceholder<T5>,
        builder: (T1, T2, T3, T4, T5) -> R
    ): R {
        return builder(arg1[this], arg2[this], arg3[this], arg4[this], arg5[this])
    }

    inline fun <
            reified R : Any,
            reified T1 : Any,
            reified T2 : Any,
            reified T3 : Any,
            reified T4 : Any,
            reified T5 : Any,
            reified T6 : Any
            > build(
        arg1: TypedHttpUrlPlaceholder<T1>,
        arg2: TypedHttpUrlPlaceholder<T2>,
        arg3: TypedHttpUrlPlaceholder<T3>,
        arg4: TypedHttpUrlPlaceholder<T4>,
        arg5: TypedHttpUrlPlaceholder<T5>,
        arg6: TypedHttpUrlPlaceholder<T6>,
        builder: (T1, T2, T3, T4, T5, T6) -> R
    ): R {
        return builder(arg1[this], arg2[this], arg3[this], arg4[this], arg5[this], arg6[this])
    }

    inline fun <
            reified R : Any,
            reified T1 : Any,
            reified T2 : Any,
            reified T3 : Any,
            reified T4 : Any,
            reified T5 : Any,
            reified T6 : Any,
            reified T7 : Any
            > build(
        arg1: TypedHttpUrlPlaceholder<T1>,
        arg2: TypedHttpUrlPlaceholder<T2>,
        arg3: TypedHttpUrlPlaceholder<T3>,
        arg4: TypedHttpUrlPlaceholder<T4>,
        arg5: TypedHttpUrlPlaceholder<T5>,
        arg6: TypedHttpUrlPlaceholder<T6>,
        arg7: TypedHttpUrlPlaceholder<T7>,
        builder: (T1, T2, T3, T4, T5, T6, T7) -> R
    ): R {
        return builder(arg1[this], arg2[this], arg3[this], arg4[this], arg5[this], arg6[this], arg7[this])
    }

    // Direct build.
    // Typed arg.
    inline fun <reified R : Any, reified T1 : Any> build(
        arg1: T1,
        builder: (T1) -> R
    ): R {
        return builder(arg1)
    }

    inline fun <reified R : Any, reified T1 : Any, reified T2 : Any> build(
        arg1: T1,
        arg2: T2,
        builder: (T1, T2) -> R
    ): R {
        return builder(arg1, arg2)
    }

    inline fun <reified R : Any, reified T1 : Any, reified T2 : Any, reified T3 : Any> build(
        arg1: T1,
        arg2: T2,
        arg3: T3,
        builder: (T1, T2, T3) -> R
    ): R {
        return builder(arg1, arg2, arg3)
    }

    inline fun <reified R : Any, reified T1 : Any, reified T2 : Any, reified T3 : Any, reified T4 : Any> build(
        arg1: T1,
        arg2: T2,
        arg3: T3,
        arg4: T4,
        builder: (T1, T2, T3, T4) -> R
    ): R {
        return builder(arg1, arg2, arg3, arg4)
    }

    inline fun <
            reified R : Any,
            reified T1 : Any,
            reified T2 : Any,
            reified T3 : Any,
            reified T4 : Any,
            reified T5 : Any
            > build(
        arg1: T1,
        arg2: T2,
        arg3: T3,
        arg4: T4,
        arg5: T5,
        builder: (T1, T2, T3, T4, T5) -> R
    ): R {
        return builder(arg1, arg2, arg3, arg4, arg5)
    }

    inline fun <
            reified R : Any,
            reified T1 : Any,
            reified T2 : Any,
            reified T3 : Any,
            reified T4 : Any,
            reified T5 : Any,
            reified T6 : Any
            > build(
        arg1: T1,
        arg2: T2,
        arg3: T3,
        arg4: T4,
        arg5: T5,
        arg6: T6,
        builder: (T1, T2, T3, T4, T5, T6) -> R
    ): R {
        return builder(arg1, arg2, arg3, arg4, arg5, arg6)
    }

    inline fun <
            reified R : Any,
            reified T1 : Any,
            reified T2 : Any,
            reified T3 : Any,
            reified T4 : Any,
            reified T5 : Any,
            reified T6 : Any,
            reified T7 : Any
            > build(
        arg1: T1,
        arg2: T2,
        arg3: T3,
        arg4: T4,
        arg5: T5,
        arg6: T6,
        arg7: T7,
        builder: (T1, T2, T3, T4, T5, T6, T7) -> R
    ): R {
        return builder(arg1, arg2, arg3, arg4, arg5, arg6, arg7)
    }
}