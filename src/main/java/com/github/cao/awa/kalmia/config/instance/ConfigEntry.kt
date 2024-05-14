package com.github.cao.awa.kalmia.config.instance

import com.github.cao.awa.apricot.annotations.auto.Auto
import com.github.cao.awa.sinuatum.manipulate.Manipulate
import org.jetbrains.annotations.NotNull

open class ConfigEntry<T> {
    companion object {
        val ENTRY: ConfigEntry<*> = ConfigEntry<Any>()

        @JvmStatic
        fun <X> entry(): ConfigEntry<X> = Manipulate.cast(ENTRY)!!
    }

    @Auto
    private val key: String? = null

    @Auto
    private var value: T? = null

    @Auto
    constructor(@Auto specifyType: Class<T>)

    @Auto
    constructor()

    fun key(): String? = this.key

    @NotNull
    open fun get(): T? = this.value

    open fun update(value: T): ConfigEntry<T>? {
        this.value = value
        return this
    }
}
