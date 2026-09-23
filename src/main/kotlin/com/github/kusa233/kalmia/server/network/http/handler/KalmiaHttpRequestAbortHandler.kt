package com.github.kusa233.kalmia.server.network.http.handler

import com.github.kusa233.kalmia.server.network.http.context.abort.KalmiaAbortHttpContext
import kotlin.reflect.KClass

class KalmiaHttpRequestAbortHandler(private val exceptionHandler: MutableMap<KClass<out Throwable>, KalmiaAbortHttpContext.(Throwable) -> Any>) {
    fun hasHandler(type: KClass<out Throwable>): Boolean {
        return this.exceptionHandler[type] != null
    }

    fun handleAbort(
        abortScope: KalmiaAbortHttpContext,
        exception: Throwable
    ): Any {
        val handler = this.exceptionHandler[exception::class]

        return if (handler != null) {
            handler(abortScope, exception)
        } else {
            exception
        }
    }
}