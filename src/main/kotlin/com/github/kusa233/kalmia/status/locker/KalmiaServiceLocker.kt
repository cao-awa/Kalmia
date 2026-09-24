package com.github.kusa233.kalmia.status.locker

import java.util.concurrent.BlockingQueue
import java.util.concurrent.LinkedBlockingQueue

class KalmiaHttpServerLocker {
    private val queue: BlockingQueue<Boolean> = LinkedBlockingQueue()

    fun onStop() {
        this.queue.offer(true)
    }

    @Throws(InterruptedException::class)
    fun await() {
         this.queue.take()
    }
}