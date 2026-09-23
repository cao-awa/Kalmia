package com.github.kusa233.kalmia.server.network.http.placeholder.url.type

import com.github.kusa233.kalmia.server.network.http.argument.type.validator.exception.TypedHttpArgumentValidateException
import com.github.kusa233.kalmia.server.network.http.placeholder.url.type.validator.basic.TypedHttpUrlPlaceholderBooleanInitializeValidator
import com.github.kusa233.kalmia.server.network.http.placeholder.url.type.validator.basic.TypedHttpUrlPlaceholderByteInitializeValidator
import com.github.kusa233.kalmia.server.network.http.placeholder.url.type.validator.basic.TypedHttpUrlPlaceholderCharInitializeValidator
import com.github.kusa233.kalmia.server.network.http.placeholder.url.type.validator.basic.TypedHttpUrlPlaceholderDoubleInitializeValidator
import com.github.kusa233.kalmia.server.network.http.placeholder.url.type.validator.basic.TypedHttpUrlPlaceholderFloatInitializeValidator
import com.github.kusa233.kalmia.server.network.http.placeholder.url.type.validator.basic.TypedHttpUrlPlaceholderIntInitializeValidator
import com.github.kusa233.kalmia.server.network.http.placeholder.url.type.validator.basic.TypedHttpUrlPlaceholderLongInitializeValidator
import com.github.kusa233.kalmia.server.network.http.placeholder.url.type.validator.basic.TypedHttpUrlPlaceholderShortInitializeValidator
import com.github.kusa233.kalmia.server.network.http.placeholder.url.type.validator.basic.string.TypedHttpUrlPlaceholderStringInitializeValidator
import com.github.kusa233.kalmia.server.network.http.placeholder.url.type.validator.TypedHttpUrlPlaceholderInitializeValidator
import com.github.kusa233.kalmia.server.network.http.context.KalmiaHttpContext
import com.github.kusa233.kalmia.server.network.http.placeholder.url.exception.TypedHttpUrlMissingException
import com.github.kusa233.kalmia.server.network.http.placeholder.url.type.combinator.TypedHttpUrlPlaceholderCombinator
import com.github.kusa233.kalmia.server.network.http.placeholder.url.type.validator.uuid.TypedHttpUrlPlaceholderUuidInitializeValidator
import java.util.UUID
import kotlin.reflect.KClass
import kotlin.reflect.KProperty

class TypedHttpUrlPlaceholder<T : Any> {
    companion object {
        val THREAD_LOCAL: ThreadLocal<KalmiaHttpContext> = ThreadLocal()
        private val validators: MutableMap<KClass<*>, TypedHttpUrlPlaceholderInitializeValidator<*>> = mutableMapOf()

        init {
            addValidator(Short::class, TypedHttpUrlPlaceholderShortInitializeValidator())
            addValidator(Int::class, TypedHttpUrlPlaceholderIntInitializeValidator())
            addValidator(Long::class, TypedHttpUrlPlaceholderLongInitializeValidator())
            addValidator(Float::class, TypedHttpUrlPlaceholderFloatInitializeValidator())
            addValidator(Double::class, TypedHttpUrlPlaceholderDoubleInitializeValidator())

            addValidator(Boolean::class, TypedHttpUrlPlaceholderBooleanInitializeValidator())

            addValidator(Byte::class, TypedHttpUrlPlaceholderByteInitializeValidator())
            addValidator(Char::class, TypedHttpUrlPlaceholderCharInitializeValidator())

            addValidator(String::class, TypedHttpUrlPlaceholderStringInitializeValidator())

            addValidator(UUID::class, TypedHttpUrlPlaceholderUuidInitializeValidator())
        }

        private fun <T : Any> addValidator(type: KClass<T>, validator: TypedHttpUrlPlaceholderInitializeValidator<T>) {
            this.validators[type] = validator
        }

        @Suppress("unchecked_cast")
        fun <T : Any> getValidator(type: KClass<T>): TypedHttpUrlPlaceholderInitializeValidator<T>? {
            return this.validators[type] as TypedHttpUrlPlaceholderInitializeValidator<T>?
        }

        fun <T: Any> getActualValidator(type: KClass<T>): TypedHttpUrlPlaceholderInitializeValidator<T> {
            return getValidator(type)
                ?: TypedHttpArgumentValidateException.failed("Unregistered placeholder validator of type '${type}'")
        }

        inline fun <reified T: Any> create(name: String, type: KClass<T>): TypedHttpUrlPlaceholder<T> {
            return TypedHttpUrlPlaceholder(name, type, getActualValidator(type))
        }
    }

    val name: String
    private val type: KClass<T>
    private val initializeValidator: TypedHttpUrlPlaceholderInitializeValidator<T>
    private var combinators: MutableList<TypedHttpUrlPlaceholderCombinator<T>> = mutableListOf()

    constructor(name: String, type: KClass<T>, initializeValidator: TypedHttpUrlPlaceholderInitializeValidator<T>) {
        this.name = name
        this.type = type
        this.initializeValidator = initializeValidator
    }

    @Suppress("unchecked_cast")
    operator fun get(context: KalmiaHttpContext): T {
        val cachedValue = context.fetchCache(toString())
        if (cachedValue != null) {
            return cachedValue as T
        }
        val contentList = context.path().split("/")
        val seq: Int = context.placeholders()[this.name] ?: missing(context)

        if (contentList.size <= seq) {
            missing(context)
        }

        val content = contentList[seq]
        var value: T =  this.initializeValidator[this.name, content, context.path()]
        for (validator in combinators) {
            value = validator.combinate(value)
        }
        context.cache("{${this.name}}", value)
        return value
    }

    @Suppress("unchecked_cast")
    operator fun getValue(nothing: Nothing?, property: KProperty<*>): T {
        val context = THREAD_LOCAL.get()
        val cachedValue = context.fetchCache(toString())
        if (cachedValue != null) {
            return cachedValue as T
        }
        if (context != null) {
            return get(context)
        }
        throw IllegalStateException("Typed placeholder delegate cannot used without request scope")
    }

    private fun missing(context: KalmiaHttpContext): Nothing = TypedHttpUrlMissingException.missing(
        "Required placeholder '${this.name}' is missing, type is ${this.type.simpleName}, at '${context.placeholderURL()}'"
    )

    operator fun invoke(context: KalmiaHttpContext): T {
        return get(context)
    }

    fun combinator(validator: TypedHttpUrlPlaceholderCombinator<T>): TypedHttpUrlPlaceholder<T> {
        this.combinators.add(validator)
        return this
    }

    fun combinator(combinator: (T) -> T): TypedHttpUrlPlaceholder<T> {
        combinator(
            object : TypedHttpUrlPlaceholderCombinator<T> {
                override fun combinate(value: T): T {
                    return combinator(value)
                }
            }
        )
        return this
    }
}

inline fun <reified T : Any> placeholder(name: String): TypedHttpUrlPlaceholder<T> {
    return TypedHttpUrlPlaceholder.create(name, T::class)
}