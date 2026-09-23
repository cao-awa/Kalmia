package com.github.kusa233.kalmia.server.network.http.argument.type.combinator

@FunctionalInterface
interface TypedHttpArgumentCombinator<T : Any> {
    fun combinate(value: T): T
}