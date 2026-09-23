package com.github.kusa233.kalmia.server.network.http.body.text

import com.github.kusa233.kalmia.server.network.http.body.KalmiaHttpBody

class KalmiaHttpTextBody(val text: String): KalmiaHttpBody() {
    override fun stringData(): String {
        return this.text
    }
}