package com.github.cao.awa.kalmia.debug.dependency.circular

import com.github.cao.awa.apricot.util.collection.ApricotCollectionFactor
import com.github.cao.awa.kalmia.exception.auto.CircularDependencyException
import java.util.*

class CircularDependency {
    private val dependencies: MutableMap<Any, RequiredDependency> = ApricotCollectionFactor.hashMap()

    fun pushRequirement(target: Any, dependency: RequiredDependency) {
        if (this.dependencies.containsKey(target)) {
            val oldDep = this.dependencies[target]
            dependency.forEach { oldDep?.add(it) }
        }
        this.dependencies[target] = dependency

        checkCircular(target, ApricotCollectionFactor.stack(), false)
    }

    private fun checkCircular(target: Any, stack: Stack<Any>, inner: Boolean) {
        if (this.dependencies.containsKey(target)) {
            this.dependencies[target]?.forEach {
                stack.push(it)
                val map: MutableMap<Any, Int> = ApricotCollectionFactor.hashMap()
                for (o in stack) {
                    val currentLooping = map.getOrDefault(o, 0) + 1

                    if (currentLooping == 2) {
                        stack.pop()
                        throw CircularDependencyException(
                            stack.stream().map(Any::toString).toList(),
                            o.toString()
                        )
                    }
                    map[o] = currentLooping
                }
                checkCircular(it, stack, true)
                if (inner) {
                    stack.pop()
                }
            }
        }
    }
}
