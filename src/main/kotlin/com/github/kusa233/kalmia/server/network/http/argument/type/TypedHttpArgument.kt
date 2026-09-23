package com.github.kusa233.kalmia.server.network.http.argument.type

import com.github.kusa233.kalmia.server.network.http.argument.exception.TypedHttpArgumentMissingException
import com.github.kusa233.kalmia.server.network.http.argument.type.validator.basic.TypedHttpArgumentBooleanInitializeValidator
import com.github.kusa233.kalmia.server.network.http.argument.type.validator.basic.TypedHttpArgumentByteInitializeValidator
import com.github.kusa233.kalmia.server.network.http.argument.type.validator.basic.TypedHttpArgumentCharInitializeValidator
import com.github.kusa233.kalmia.server.network.http.argument.type.validator.data.TypedHttpArgumentDataInitializeValidator
import com.github.kusa233.kalmia.server.network.http.argument.type.validator.basic.TypedHttpArgumentDoubleInitializeValidator
import com.github.kusa233.kalmia.server.network.http.argument.type.validator.basic.TypedHttpArgumentFloatInitializeValidator
import com.github.kusa233.kalmia.server.network.http.argument.type.validator.basic.TypedHttpArgumentIntInitializeValidator
import com.github.kusa233.kalmia.server.network.http.argument.type.validator.basic.TypedHttpArgumentLongInitializeValidator
import com.github.kusa233.kalmia.server.network.http.argument.type.validator.basic.TypedHttpArgumentShortInitializeValidator
import com.github.kusa233.kalmia.server.network.http.argument.type.validator.basic.string.TypedHttpArgumentStringInitializeValidator
import com.github.kusa233.kalmia.server.network.http.argument.type.validator.TypedHttpArgumentInitializeValidator
import com.github.kusa233.kalmia.server.network.http.argument.type.combinator.TypedHttpArgumentCombinator
import com.github.kusa233.kalmia.server.network.http.argument.type.validator.exception.TypedHttpArgumentValidateException
import com.github.kusa233.kalmia.server.network.http.argument.type.validator.uuid.TypedHttpArgumentUuidInitializeValidator
import com.github.kusa233.kalmia.server.network.http.argument.type.value.TypedHttpArgumentDefaultValues
import com.github.kusa233.kalmia.server.network.http.context.KalmiaHttpContext
import java.util.UUID
import kotlin.reflect.KClass
import kotlin.reflect.KProperty

class TypedHttpArgument<T : Any> {
    companion object {
        val THREAD_LOCAL: ThreadLocal<KalmiaHttpContext> = ThreadLocal()
        private val validators: MutableMap<KClass<*>, TypedHttpArgumentInitializeValidator<*>> = mutableMapOf()

        init {
            addValidator(Short::class, TypedHttpArgumentShortInitializeValidator())
            addValidator(Int::class, TypedHttpArgumentIntInitializeValidator())
            addValidator(Long::class, TypedHttpArgumentLongInitializeValidator())
            addValidator(Float::class, TypedHttpArgumentFloatInitializeValidator())
            addValidator(Double::class, TypedHttpArgumentDoubleInitializeValidator())

            addValidator(Boolean::class, TypedHttpArgumentBooleanInitializeValidator())

            addValidator(Byte::class, TypedHttpArgumentByteInitializeValidator())
            addValidator(Char::class, TypedHttpArgumentCharInitializeValidator())

            addValidator(String::class, TypedHttpArgumentStringInitializeValidator())

            addValidator(UUID::class, TypedHttpArgumentUuidInitializeValidator())
        }

        fun <T : Any> addValidator(type: KClass<T>, validator: TypedHttpArgumentInitializeValidator<T>) {
            this.validators[type] = validator
        }

        fun <T : Any> addValidator(type: KClass<T>, validator: (String, String) -> T) {
            this.validators[type] = TypedHttpArgumentDataInitializeValidator(validator)
        }

        @Suppress("unchecked_cast")
        fun <T : Any> getValidator(type: KClass<T>): TypedHttpArgumentInitializeValidator<T>? {
            return this.validators[type] as TypedHttpArgumentInitializeValidator<T>?
        }

        inline fun <reified T : Any> getActualValidator(type: KClass<T>): TypedHttpArgumentInitializeValidator<T> {
            return getValidator(type)
                ?: TypedHttpArgumentValidateException.failed("Unregistered argument validator of type '${type}'")
        }

        inline fun <reified T : Any> create(name: String, type: KClass<T>, missable: Boolean): TypedHttpArgument<T> {
            return TypedHttpArgument(name, type, missable, getActualValidator(T::class))
        }
    }

    val name: String
    val missable: Boolean
    private val type: KClass<T>
    private var defaultValue: T? = null
    private val initializeValidator: TypedHttpArgumentInitializeValidator<T>
    private var combinators: MutableList<TypedHttpArgumentCombinator<T>> = mutableListOf()
    private lateinit var context: KalmiaHttpContext

    constructor(
        name: String,
        type: KClass<T>,
        missable: Boolean,
        initializeValidator: TypedHttpArgumentInitializeValidator<T>
    ) {
        this.name = name
        this.type = type
        this.missable = missable
        this.initializeValidator = initializeValidator
    }

    @Suppress("unchecked_cast")
    operator fun get(context: KalmiaHttpContext): T {
        return try {
            val cachedValue = context.fetchCache(toString())
            if (cachedValue != null) {
                return cachedValue as T
            }
            val content: String = context.arguments()[this.name]
                ?: TypedHttpArgumentMissingException.missing("Required argument '${this.name}' is missing, type is ${this.type.simpleName}")
            var value: T = this.initializeValidator[this.name, content]
            this.context = context
            for (validator in combinators) {
                value = validator.combinate(value)
            }
            context.cache(toString(), value)
            return value
        } catch (e: Exception) {
            if (this.missable) {
                if (this.defaultValue == null) {
                    TypedHttpArgumentDefaultValues.getDefault(this.type)
                } else {
                    this.defaultValue!!
                }
            } else {
                throw e
            }
        }
    }

    operator fun invoke(context: KalmiaHttpContext): T {
        return get(context)
    }

    @Suppress("unchecked_cast")
    operator fun getValue(nothing: Nothing?, property: KProperty<*>): T {
        val context = THREAD_LOCAL.get()
        val cachedValue = context.fetchCache(this.name)
        if (cachedValue != null) {
            return cachedValue as T
        }
        if (context != null) {
            return get(context)
        }
        throw IllegalStateException("Typed argument delegate cannot used without request scope")
    }

    fun defaultValue(value: T): TypedHttpArgument<T> {
        this.defaultValue = value
        return this
    }

    private fun combinator(combinator: TypedHttpArgumentCombinator<T>): TypedHttpArgument<T> {
        this.combinators.add(combinator)
        return this
    }

    fun combinator(combinator: KalmiaHttpContext.(T) -> T): TypedHttpArgument<T> {
        combinator(
            object : TypedHttpArgumentCombinator<T> {
                override fun combinate(value: T): T {
                    return combinator(this@TypedHttpArgument.context, value)
                }
            }
        )
        return this
    }
}

inline fun <reified T : Any> arg(name: String, missable: Boolean = false): TypedHttpArgument<T> {
    return TypedHttpArgument.create(name, T::class, missable)
}
