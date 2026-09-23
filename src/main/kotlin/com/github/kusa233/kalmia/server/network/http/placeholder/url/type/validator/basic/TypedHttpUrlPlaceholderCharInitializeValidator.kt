package com.github.kusa233.kalmia.server.network.http.placeholder.url.type.validator.basic

import com.github.kusa233.kalmia.server.network.http.placeholder.url.type.validator.TypedHttpUrlPlaceholderInitializeValidator
import com.github.kusa233.kalmia.server.network.http.placeholder.url.type.validator.error

class TypedHttpUrlPlaceholderCharInitializeValidator : TypedHttpUrlPlaceholderInitializeValidator<Char> {
    override operator fun get(argumentName:String, content: String, url: String): Char {
        return if(content.length == 1) {
            content[0]
        } else {
            error(argumentName, content, "Char", url)
        }
    }
}