package com.github.kusa233.kalmia.server.network.http.placeholder.url.type.validator

import com.github.kusa233.kalmia.server.network.http.argument.type.validator.exception.TypedHttpArgumentValidateException

interface TypedHttpUrlPlaceholderInitializeValidator<T : Any> {
    operator fun get(argumentName: String, content: String, url: String): T
}

@Throws(TypedHttpArgumentValidateException::class)
fun error(argumentName: String, content: String, type: String, url: String): Nothing =
    throw TypedHttpArgumentValidateException("The value '$content' for the $type placeholder  '$argumentName'  is not valid in URL '${handleUrlArg(argumentName, content, url)}'")

fun handleUrlArg(argumentName: String, content: String, url: String): String {
    return url.replace(content, "{${argumentName}}")
}