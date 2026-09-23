package com.github.kusa233.kalmia.server.network.http.placeholder.url.type.validator.basic

import com.github.kusa233.kalmia.server.network.http.placeholder.url.type.validator.TypedHttpUrlPlaceholderInitializeValidator
import com.github.kusa233.kalmia.server.network.http.placeholder.url.type.validator.error

class TypedHttpUrlPlaceholderDoubleInitializeValidator : TypedHttpUrlPlaceholderInitializeValidator<Double> {
    override operator fun get(argumentName:String, content: String, url: String): Double {
        try {
            return java.lang.Double.parseDouble(content)
        } catch (_: NumberFormatException) {
            error(argumentName, content, "Double", url)
        }
    }
}