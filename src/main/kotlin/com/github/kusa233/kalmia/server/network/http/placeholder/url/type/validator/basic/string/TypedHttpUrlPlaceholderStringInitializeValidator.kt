package com.github.kusa233.kalmia.server.network.http.placeholder.url.type.validator.basic.string

import com.github.kusa233.kalmia.server.network.http.placeholder.url.type.validator.TypedHttpUrlPlaceholderInitializeValidator

class TypedHttpUrlPlaceholderStringInitializeValidator : TypedHttpUrlPlaceholderInitializeValidator<String> {
    override operator fun get(argumentName:String, content: String, url: String): String = content
}