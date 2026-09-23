package com.github.kusa233.kalmia.server.network.websocket.context

import com.github.kusa233.kalmia.server.network.context.KalmiaContext
import com.github.kusa233.kalmia.server.network.exception.abort.UnexpectedBehaviorException
import com.github.kusa233.kalmia.server.network.websocket.context.abort.KalmiaAbortWebSocketContext
import com.github.kusa233.kalmia.server.network.websocket.holder.KalmiaTextWebsocketFrameHolder
import com.github.kusa233.kalmia.server.network.websocket.phase.KalmiaWebSocketPhase

@Suppress("unused")
open class KalmiaWebSocketContext(val msg: KalmiaTextWebsocketFrameHolder, val phase: KalmiaWebSocketPhase): KalmiaContext<KalmiaTextWebsocketFrameHolder, KalmiaWebSocketContext, KalmiaAbortWebSocketContext>(msg) {
    companion object {

    }
    private var promiseClose: Boolean = false
    private var path: String = path().let {
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

    open fun promiseClose() {
        this.promiseClose = true
    }

    fun isPromiseClose(): Boolean {
        return this.promiseClose
    }

    fun abortWith(exception: Exception, postHandler: () -> Unit = { }) {
        postHandler()
        throw exception
    }

    fun abortWith(postHandler: () -> Unit = { }) {
        abortWith(
            UnexpectedBehaviorException(),
            postHandler
        )
    }

    fun abortIf(condition: Boolean, postHandler: () -> Unit = { }) {
        if (condition) {
            abortWith(postHandler)
        }
    }

    override fun createInherited(): KalmiaWebSocketContext {
        return KalmiaWebSocketContext(this.msg, this.phase).also {
            it.promiseClose = this.promiseClose
        }
    }

    override fun createAbort(): KalmiaAbortWebSocketContext {
        return KalmiaAbortWebSocketContext(this)
    }
}