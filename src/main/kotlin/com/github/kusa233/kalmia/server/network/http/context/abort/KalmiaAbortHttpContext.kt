package com.github.kusa233.kalmia.server.network.http.context.abort

import com.github.kusa233.kalmia.server.network.context.abort.KalmiaAbortContext
import com.github.kusa233.kalmia.server.network.http.context.KalmiaHttpContext
import com.github.kusa233.kalmia.server.network.http.holder.KalmiaFullHttpRequestHolder

class KalmiaAbortHttpContext(context: KalmiaHttpContext): KalmiaHttpContext(context), KalmiaAbortContext<KalmiaFullHttpRequestHolder> {
}