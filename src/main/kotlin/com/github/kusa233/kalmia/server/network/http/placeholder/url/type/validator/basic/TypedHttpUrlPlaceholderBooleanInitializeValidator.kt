package com.github.kusa233.kalmia.server.network.http.placeholder.url.type.validator.basic

import com.github.kusa233.kalmia.server.network.http.placeholder.url.type.validator.TypedHttpUrlPlaceholderInitializeValidator
import com.github.kusa233.kalmia.server.network.http.placeholder.url.type.validator.error

class TypedHttpUrlPlaceholderBooleanInitializeValidator : TypedHttpUrlPlaceholderInitializeValidator<Boolean> {
    override operator fun get(argumentName:String, content: String, url: String): Boolean {
        if ("true" == content) {
            return true
        } else if ("false" == content) {
            return false
        } else {
            error(argumentName, content, "Boolean", url)
        }
    }
}