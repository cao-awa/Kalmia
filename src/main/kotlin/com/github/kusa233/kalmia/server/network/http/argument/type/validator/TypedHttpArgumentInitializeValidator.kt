package com.github.kusa233.kalmia.server.network.http.argument.type.validator

import com.github.kusa233.kalmia.server.network.http.argument.type.validator.exception.TypedHttpArgumentValidateException

interface TypedHttpArgumentInitializeValidator<T : Any> {
    operator fun get(argumentName: String, content: String): T
}

@Throws(TypedHttpArgumentValidateException::class)
fun error(argumentName: String, content: String, type: String): Nothing =
    throw TypedHttpArgumentValidateException("The value '$content' for the $type argument  '$argumentName'  is not valid")