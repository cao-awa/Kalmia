package com.github.cao.awa.kalmia.security.cipher.manager

import com.github.cao.awa.apricot.util.collection.ApricotCollectionFactor

abstract class KeyManager<T> {
    private val keys: MutableMap<String, T> = ApricotCollectionFactor.hashMap()

    fun add(field: String, key: T) {
        this.keys[field] = key
    }

    fun delete(field: String) {
        this.keys.remove(field)
    }

    fun get(field: String): T {
        return this.keys[field] ?: throw RuntimeException()
    }

    fun has(field: String): Boolean {
        return get(field) != null;
    }
}
