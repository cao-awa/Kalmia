package com.github.kusa233.kalmia.server.network.http.argument

import com.github.kusa233.kalmia.server.network.http.form.encoded.UrlEncodedForm

class HttpRequestArguments: Iterable<Map.Entry<String, String>> {
    companion object {
        val EMPTY: HttpRequestArguments = HttpRequestArguments()

        fun build(data: UrlEncodedForm): HttpRequestArguments {
            return HttpRequestArguments().apply {
                data.forEach { (key, value) ->
                    this.data[key] = value
                }
            }
        }
    }

    private val data: MutableMap<String, String> = mutableMapOf()

    operator fun get(key: String): String? {
        return this.data[key]
    }

    fun whenNotNull(key: String, action: (String) -> Unit) {
        get(key)?.let {
            action(it)
        }
    }

    fun forEach(action: (String, String) -> Unit) {
        this.data.forEach(action)
    }

    fun forEach(action: (Map.Entry<String, String>) -> Unit) {
        this.data.forEach(action)
    }

    override fun toString(): String {
        return StringBuilder().let {
            val size = this.data.size - 1
            var count = 0
            for ((key, value) in this.data) {
                it.append(key).append("=").append(value)
                if (count++ != size) {
                    it.append("&")
                }
            }
            it.toString()
        }
    }

    override fun iterator(): Iterator<Map.Entry<String, String>> {
        return this.data.iterator()
    }
}