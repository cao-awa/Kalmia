package com.github.kusa233.kalmia.lazy

class Lazy1<I, T: Any> {
    private val provider: (I) -> T
    private lateinit var value: T

    constructor(provider: (I) -> T) {
        this.provider = provider
    }

    fun get(input: I): T {
        return this.value
    }
}