package com.github.cao.awa.kalmia.session.factor

import com.github.cao.awa.apricot.io.bytes.reader.BytesReader
import com.github.cao.awa.apricot.util.collection.ApricotCollectionFactor
import com.github.cao.awa.kalmia.session.Session

import java.util.function.Function

object SessionFactor {
    private val factories: MutableMap<Int, Function<BytesReader, Session?>> = ApricotCollectionFactor.hashMap()

    fun register(id: Int, factor: Function<BytesReader, Session?>) {
        factories[id] = factor
    }

    @JvmStatic
    fun create(id: Int, reader: BytesReader): Session? {
        val factor: Function<BytesReader, Session?> = factories[id]!!
        return factor.apply(reader)
    }
}
