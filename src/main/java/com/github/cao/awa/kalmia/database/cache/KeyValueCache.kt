package com.github.cao.awa.kalmia.database.cache

import java.util.function.BiConsumer
import java.util.function.Consumer
import java.util.function.Function

class KeyValueCache<K, V>(private val cache: MutableMap<K, V?>) {
    operator fun get(key: K, getter: Function<K, V?>): V? {
        var result = this.cache[key]
        if (result == null) {
            result = getter.apply(key)
            cache(
                key,
                result
            )
        }
        return result
    }

    fun cache(key: K, value: V?): V? {
        this.cache[key] = value
        return value
    }

    fun expire(key: K): V? = this.cache.remove(key)

    fun update(key: K, value: V?, updater: BiConsumer<K, V?>): V? {
        updater.accept(
            key,
            value
        )
        return cache(
            key,
            value
        )
    }

    fun delete(key: K, deleter: Consumer<K>): V? {
        deleter.accept(key)
        return expire(key)
    }

    fun clear() = this.cache.clear()
}