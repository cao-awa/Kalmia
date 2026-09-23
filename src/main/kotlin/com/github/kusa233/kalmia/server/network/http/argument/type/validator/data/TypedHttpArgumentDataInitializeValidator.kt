package com.github.kusa233.kalmia.server.network.http.argument.type.validator.data

import com.github.kusa233.kalmia.server.network.http.argument.type.validator.TypedHttpArgumentInitializeValidator

class TypedHttpArgumentDataInitializeValidator<T : Any>(
    private val validator: (String, String) -> T
) : TypedHttpArgumentInitializeValidator<T> {
    override operator fun get(argumentName: String, content: String): T = this.validator(argumentName, content)
}