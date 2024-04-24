package com.github.cao.awa.kalmia.debug.dependency.circular

import com.github.cao.awa.apricot.util.collection.ApricotCollectionFactor
import java.util.function.Consumer;

class RequiredDependency {
    private val required: MutableList<Any> = ApricotCollectionFactor.arrayList()

    fun add(dependency: Any): RequiredDependency {
        this.required.add(dependency)
        return this
    }

    fun get(): List<Any> = this.required

    fun forEach(consumer: Consumer<Any>?) = this.required.forEach(consumer)
}
