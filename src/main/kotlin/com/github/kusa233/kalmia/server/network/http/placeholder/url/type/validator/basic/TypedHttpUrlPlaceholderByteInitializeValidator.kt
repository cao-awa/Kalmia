package com.github.kusa233.kalmia.server.network.http.placeholder.url.type.validator.basic

import com.github.kusa233.kalmia.server.network.http.placeholder.url.type.validator.TypedHttpUrlPlaceholderInitializeValidator
import com.github.kusa233.kalmia.server.network.http.placeholder.url.type.validator.error

class TypedHttpUrlPlaceholderByteInitializeValidator : TypedHttpUrlPlaceholderInitializeValidator<Byte> {
    override operator fun get(argumentName:String, content: String, url: String): Byte {
        try {
            return java.lang.Byte.parseByte(content)
        } catch (_: NumberFormatException) {
            error(argumentName, content, "Byte", url)
        }
    }
}