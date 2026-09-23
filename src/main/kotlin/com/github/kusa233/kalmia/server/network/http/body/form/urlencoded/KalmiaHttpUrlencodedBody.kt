package com.github.kusa233.kalmia.server.network.http.body.form.urlencoded

import com.github.kusa233.kalmia.server.network.http.body.KalmiaHttpBody
import com.github.kusa233.kalmia.server.network.http.form.encoded.UrlEncodedForm

class KalmiaHttpUrlencodedBody: KalmiaHttpBody() {
    companion object {
        val EMPTY: KalmiaHttpUrlencodedBody = KalmiaHttpUrlencodedBody()

        fun build(data: UrlEncodedForm): KalmiaHttpUrlencodedBody {
            return KalmiaHttpUrlencodedBody().apply {
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

    override fun stringData(): String {
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
}