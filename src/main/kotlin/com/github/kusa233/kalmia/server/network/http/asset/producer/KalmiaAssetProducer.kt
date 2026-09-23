package com.github.kusa233.kalmia.server.network.http.asset.producer

import com.github.kusa233.kalmia.server.network.http.asset.KalmiaAsset
import com.github.kusa233.kalmia.server.network.http.context.KalmiaHttpContext
import com.github.kusa233.kalmia.server.network.http.pipeline.KalmiaHttpRequestPipeline

class KalmiaAssetProducer(val context: KalmiaHttpContext) {
    fun getAsset(pipeline: KalmiaHttpRequestPipeline): KalmiaAsset<*> {
        return pipeline.getAsset(this.context)
    }
}