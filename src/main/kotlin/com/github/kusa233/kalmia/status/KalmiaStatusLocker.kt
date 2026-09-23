package com.github.kusa233.kalmia.status

import java.util.concurrent.BlockingQueue
import java.util.concurrent.LinkedBlockingQueue

class KalmiaStatusLocker {
    private val lifecycleStatus: MutableMap<String, Any> = mutableMapOf()
    private val lifecycleStatusLookup: MutableMap<Any, String> = mutableMapOf()
    private val queue: BlockingQueue<Boolean> = LinkedBlockingQueue()

    fun registerLifecycle(name: String, lifecycleHolder: Any) {
        this.lifecycleStatus[name] = lifecycleHolder
        this.lifecycleStatusLookup[lifecycleHolder] = name
    }

    fun completedLifecycle(name: String) {
        val source = this.lifecycleStatus.remove(name)
        this.lifecycleStatusLookup.remove(source)
        if (this.lifecycleStatus.isEmpty()){
            this.queue.offer(true)
        }
    }

    fun completedLifecycle(reloadableHolder: Any) {
        val name = this.lifecycleStatusLookup[reloadableHolder]
        if (name != null) {
            completedLifecycle(name)
        }
    }

    fun registeredLifecycle(): Map<String, Any> {
        return this.lifecycleStatus
    }

    @Throws(InterruptedException::class)
    fun await() {
         this.queue.take()
    }

    fun clear() {
        this.lifecycleStatus.clear()
        this.lifecycleStatusLookup.clear()
    }
}