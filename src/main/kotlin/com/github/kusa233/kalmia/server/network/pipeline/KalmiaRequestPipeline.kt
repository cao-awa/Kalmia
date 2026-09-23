package com.github.kusa233.kalmia.server.network.pipeline

import com.github.kusa233.kalmia.server.network.context.KalmiaContext
import com.github.kusa233.kalmia.server.network.context.abort.KalmiaAbortContext
import com.github.kusa233.kalmia.server.network.handler.KalmiaRequestHandler
import com.github.kusa233.kalmia.server.network.holder.PathByteBufHolder
import io.netty.channel.ChannelHandlerContext

abstract class KalmiaRequestPipeline<B: PathByteBufHolder, C: KalmiaContext<B, C, A>, A: KalmiaAbortContext<B>, H: KalmiaRequestHandler<B, C, A>> {
    fun abortable(
        handlerContext: ChannelHandlerContext,
        kalmiaContext: C,
        handler: H?,
        action: () -> Unit
    ) {
        try {
            // Do program logic.
            action()
        } catch (exception: Throwable) {
            // When exception, reveal all the details by Kalmia framework.
            try {
                // Inherite current request context, responser need this context.
                val responseScope = kalmiaContext.createInherited()
                // Create abort context, used to response custom logic.
                val abortScope = kalmiaContext.createAbort()
                // Handle exception by abort handler with abort scope (inherited by current context).
                if (handler != null && handler.hasAbortHandler(exception)) {
                    handler.handleAbort(abortScope, exception){
                        response(handlerContext, responseScope, it)
                    }
                } else {
                    // If no handler found, let Kalmia framework to handle this exception.
                    throw exception
                }
            } catch (unhandledException: Throwable) {
                handleException(
                    unhandledException,
                    handlerContext,
                    kalmiaContext
                )
            }
        }
    }

    abstract fun handleException(exception: Throwable, handlerContext: ChannelHandlerContext, kalmiaContext: C)

    abstract fun response(
        handlerContext: ChannelHandlerContext,
        kalmiaContext: C,
        response: Any
    )
}