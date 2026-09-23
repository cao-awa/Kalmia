package com.github.kusa233.kalmia.server.network.http.url

import org.jetbrains.annotations.Contract
import java.util.LinkedList

class KalmiaPlaceholderURL {
    companion object {
        fun create(input: String): KalmiaPlaceholderURL {
            val route = KalmiaPlaceholderURL()
            val paths = input.split("/")
            for (path in paths) {
                route.url.add(path)
                if (path.contains("{") && path.contains("}")) {
                    route.hasPlaceholder = true
                }
            }
            return route
        }
    }

    private val url: LinkedList<String> = LinkedList()
    private var hasPlaceholder: Boolean = false

    @Contract(pure = true)
    fun hasPlaceholder(): Boolean = this.hasPlaceholder

    fun matchPlaceholder(url: KalmiaPlaceholderURL): Boolean {
        if (this.url.size != url.url.size) {
            return false
        }

        var seq = 0
        val end = this.url.size
        var hasPlaceholder = false
        while (seq < end) {
            val path = this.url[seq]
            val targetPath = url.url[seq]
            if (targetPath.startsWith("{") || path.endsWith("}")) {
                hasPlaceholder = true
            } else if (path != targetPath) {
                return false
            }
            seq++
        }

        return hasPlaceholder
    }

    fun placeholders(): MutableMap<String, Int> {
        val placeholders: MutableMap<String, Int> = HashMap()
        var seq = 0
        for (path in this.url) {
            if (path.startsWith("{") && path.endsWith("}")) {
                val clearPath = path.replace("{", "").replace("}", "")
                placeholders[clearPath] = seq
            }
            seq++
        }
        return placeholders
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) {
            return true
        }

        if (other == null || javaClass != other.javaClass) {
            return false
        }

        if (other is KalmiaPlaceholderURL) {
            if (this.url.size != other.url.size) {
                return false
            }

            var seq = 0
            val end = this.url.size
            while (seq < end) {
                if (this.url[seq] != other.url[seq]) {
                    return false
                }
                seq++
            }
        }

        return true
    }

    override fun hashCode(): Int {
        return this.url.hashCode()
    }

    override fun toString(): String {
        val builder = StringBuilder()
        for (path in this.url) {
            builder.append(path)
            builder.append("/")
        }
        builder.delete(builder.length - 1, builder.length)
        return builder.toString()
    }
}

fun String.urlParameterRoute(): KalmiaPlaceholderURL {
    return KalmiaPlaceholderURL.create(this)
}