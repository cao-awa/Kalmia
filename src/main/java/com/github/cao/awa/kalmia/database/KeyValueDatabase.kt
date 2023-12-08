package com.github.cao.awa.kalmia.database

import com.github.cao.awa.kalmia.database.cache.KeyValueCache
import java.util.function.Supplier

abstract class KeyValueDatabase<K, V>(cacheDelegate: Supplier<MutableMap<K, V?>>) {
    private val cache: KeyValueCache<K, V>

    init {
        this.cache = KeyValueCache(cacheDelegate.get())
    }

    fun cache(): KeyValueCache<K, V> = this.cache

    abstract operator fun set(key: K, value: V)
    open operator fun set(key1: K, key2: K, value: V) {
        // Redundancy method.
    }

    open operator fun set(key1: K, key2: K, key3: K, value: V) {
        // Redundancy method.
    }

    abstract operator fun get(key: K): V
    open operator fun get(key1: K, key2: K): V? {
        // Redundancy method.
        return null
    }

    open operator fun get(key: K, key2: K, key3: K): V? {
        // Redundancy method.
        return null
    }

    abstract fun remove(key: K)
    open fun remove(key1: K, key2: K) {
        // Redundancy method.
    }

    open fun remove(key1: K, key2: K, key3: K) {
        // Redundancy method.
    }

    open fun close(): Boolean = true
}