package com.github.kusa233.kalmia.server.network.http.error

import com.github.cao.awa.cason.obj.JSONObject
import com.github.kusa233.kalmia.constant.KalmiaInformation
import com.github.kusa233.kalmia.server.network.KalmiaNetworkConfig
import com.github.kusa233.kalmia.server.network.http.context.KalmiaHttpContext
import com.github.kusa233.kalmia.server.network.http.pipeline.KalmiaHttpRequestPipeline
import com.github.kusa233.kalmia.server.network.http.response.KalmiaHttpResponses
import io.netty.handler.codec.http.FullHttpResponse
import io.netty.handler.codec.http.HttpResponseStatus
import io.netty.handler.codec.http.HttpVersion

class KalmiaHttpError(
    val status: HttpResponseStatus,
    val httpVersion: HttpVersion,
    val exception: Throwable,
    val message: String,
    val requestPath: String,
    val context: KalmiaHttpContext?
) {
    companion object {
        private fun fillStacktrace(json: JSONObject, exception: Throwable) {
            json.instruct {
                arr("stacktrace") {
                    +exception.toString()
                    exception.stackTrace.forEach {
                        +" - at $it"
                    }
                }
            }
        }
    }

    fun createResponse(): FullHttpResponse {
        return KalmiaHttpResponses.createDefaultResponse(
            httpVersion,
            this.status,
            let {
                val json = JSONObject {
                    "error" set "Server protocol error (Kalmia/${KalmiaInformation.VERSION}, ${httpVersion.text()}): ${status.reasonPhrase()}"
                    "internal_error_name" set "${status.reasonPhrase()}"
                    "error_message" set message
                    if (KalmiaNetworkConfig.responseFillStacktrace) {
                        fillStacktrace(this, exception)
                    } else {
                        val detailsMessage = exception.message
                        if (detailsMessage != null) {
                            "error_details_message" set detailsMessage
                        }
                    }
                }
                let {
                    if (this.context == null) {
                        KalmiaHttpRequestPipeline.instructHttpMetadata(
                            json,
                            this.status,
                            this.httpVersion,
                            this.requestPath
                        )
                    } else {
                        KalmiaHttpRequestPipeline.instructHttpMetadata(
                            json,
                            this.context
                        )
                    }
                }.toString(true, "    ", 0)
            }
        )
    }
}