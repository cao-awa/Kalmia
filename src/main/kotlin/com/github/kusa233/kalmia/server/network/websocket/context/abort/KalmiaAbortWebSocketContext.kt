package com.github.kusa233.kalmia.server.network.websocket.context.abort

import com.github.kusa233.kalmia.server.network.context.abort.KalmiaAbortContext
import com.github.kusa233.kalmia.server.network.websocket.context.KalmiaWebSocketContext
import com.github.kusa233.kalmia.server.network.websocket.holder.KalmiaTextWebsocketFrameHolder

class KalmiaAbortWebSocketContext(context: KalmiaWebSocketContext): KalmiaWebSocketContext(context.msg, context.phase), KalmiaAbortContext<KalmiaTextWebsocketFrameHolder> {
    init {
        if (context.isPromiseClose()) {
            promiseClose()
        }
    }

    override fun promiseClose() {
        throw IllegalStateException("Cannot promise close context for aborted context")
    }
}