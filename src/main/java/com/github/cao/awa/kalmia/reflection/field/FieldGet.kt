package com.github.cao.awa.kalmia.reflection.field

import com.github.cao.awa.apricot.util.collection.ApricotCollectionFactor
import com.github.zhuaidadaya.rikaishinikui.handler.universal.entrust.EntrustEnvironment

import java.util.function.Function

class FieldGet<R, I> {
    private var target: Class<I>
    private val gets: MutableList<String> = ApricotCollectionFactor.arrayList()
    private val backups: MutableList<Function<Class<I>, R>> = ApricotCollectionFactor.arrayList()

    private constructor(target: I) {
        this.target =
            EntrustEnvironment.cast((target ?: throw RuntimeException())::class.java) ?: throw RuntimeException()
    }

    private constructor(target: Class<I>) {
        this.target = EntrustEnvironment.cast(target) ?: throw RuntimeException()
    }

    fun or(name: String): FieldGet<R, I> {
        this.gets.add(name)
        return this
    }

    fun or(function: Function<Class<I>, R>): FieldGet<R, I> {
        this.backups.add(function)
        return this
    }

    fun <Z> get(): Z? {
        val clazz: Class<I> = EntrustEnvironment.cast<Class<I>>(this.target) ?: throw RuntimeException()

        for (name in this.gets) {
            try {
                val o: Any? = clazz.getField(name).get(this.target)

                if (o != null) {
                    // TODO
                    return o as? Z
                }
            } catch (_: Exception) {

            }
        }

        for (supplier in backups) {
            try {
                val r: R = supplier.apply(clazz)

                if (r != null) {
                    // TODO
                    return r as Z
                }
            } catch (_: Exception) {

            }
        }

        return null
    }

    companion object {
        @JvmStatic
        fun <X, Y> create(target: Y, name: String): FieldGet<X, Y> {
            return FieldGet<X, Y>(target).or(name)
        }

        @JvmStatic
        fun <X, Y> create(target: Class<Y>, name: String): FieldGet<X, Y> {
            return FieldGet<X, Y>(target).or(name)
        }
    }
}
