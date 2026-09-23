package com.github.kusa233.kalmia.server.network.http.body.empty

import com.github.kusa233.kalmia.server.network.http.body.KalmiaHttpBody

object KalmiaHttpEmptyBody: KalmiaHttpBody() {
    override fun stringData(): String {
        return ""
    }
}