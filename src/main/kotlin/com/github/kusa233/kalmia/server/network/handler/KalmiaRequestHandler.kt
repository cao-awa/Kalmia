package com.github.kusa233.kalmia.server.network.handler

import com.github.kusa233.kalmia.server.network.context.KalmiaContext
import com.github.kusa233.kalmia.server.network.context.abort.KalmiaAbortContext
import com.github.kusa233.kalmia.server.network.holder.PathByteBufHolder

abstract class KalmiaRequestHandler<B: PathByteBufHolder, C: KalmiaContext<B, C, A>, A: KalmiaAbortContext<B>> {
    abstract fun hasRoute(path: String): Boolean

    abstract fun handle(context: C): Any

    abstract fun hasAbortHandler(exception:  Throwable): Boolean

    abstract fun handleAbort(abortScope: A, exception: Throwable, responser: (Any) -> Unit): Any
}