package com.github.kusa233.kalmia.server.network.http.placeholder.url.type.combinator

@FunctionalInterface
interface TypedHttpUrlPlaceholderCombinator<T : Any> {
    fun combinate(value: T): T
}