package com.github.kusa233.kalmia.server.network.http.argument.type.validator.basic

import com.github.kusa233.kalmia.server.network.http.argument.type.validator.TypedHttpArgumentInitializeValidator
import com.github.kusa233.kalmia.server.network.http.argument.type.validator.error

class TypedHttpArgumentCharInitializeValidator : TypedHttpArgumentInitializeValidator<Char> {
    override operator fun get(argumentName:String, content: String): Char {
        return if(content.length == 1) {
            content[0]
        } else {
            error(argumentName, content, "Char")
        }
    }
}