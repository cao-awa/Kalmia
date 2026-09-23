package com.github.kusa233.kalmia.server.network.http.body.json

import com.github.cao.awa.cason.obj.JSONObject
import com.github.kusa233.kalmia.server.network.http.body.KalmiaHttpBody

class KalmiaHttpJsonBody(val json: JSONObject): KalmiaHttpBody() {
    override fun stringData(): String {
        return this.json.toString()
    }
}