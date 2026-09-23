package com.github.kusa233.kalmia.server.network.http.argument.type.validator.basic

import com.github.kusa233.kalmia.server.network.http.argument.type.validator.TypedHttpArgumentInitializeValidator
import com.github.kusa233.kalmia.server.network.http.argument.type.validator.error

class TypedHttpArgumentIntInitializeValidator : TypedHttpArgumentInitializeValidator<Int> {
    override operator fun get(argumentName:String, content: String): Int {
        try {
            return Integer.parseInt(content)
        } catch (_: NumberFormatException) {
            error(argumentName, content, "Int")
        }
    }
}