package com.github.kusa233.kalmia.server.network.http.argument.type.validator.json

import com.github.cao.awa.cason.array.JSONArray
import com.github.cao.awa.cason.serialize.parser.JSONParser
import com.github.kusa233.kalmia.server.network.http.argument.type.validator.TypedHttpArgumentInitializeValidator

interface TypedHttpArgumentJSONArrayInitializeValidator<T : Any> : TypedHttpArgumentInitializeValidator<T> {
    override operator fun get(argumentName: String, content: String): T {
        return get(argumentName, JSONParser.parseArray(content))
    }

    operator fun get(argumentName: String, content: JSONArray): T
}