package com.github.kusa233.kalmia.kt.extent

fun <E> Collection<E>.onlyContains(element: E): Boolean {
    return this.size == 1 && this.contains(element)
}