package com.github.kusa233.kalmia.lazy

class Lazy<T: Any> {
    private val provider: () -> T
    private lateinit var value: T

    constructor(provider: () -> T) {
        this.provider = provider
    }

    fun get(): T {
        return this.value
    }
}