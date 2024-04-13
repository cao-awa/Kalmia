package com.github.cao.awa.kalmia.await

import com.github.cao.awa.apricot.util.collection.ApricotCollectionFactor
import com.github.cao.awa.apricot.util.time.TimeUtil
import com.github.cao.awa.kalmia.mathematic.Mathematics
import com.github.zhuaidadaya.rikaishinikui.handler.universal.entrust.EntrustEnvironment

import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.ScheduledThreadPoolExecutor
import java.util.concurrent.TimeUnit
import java.util.function.Supplier

class AwaitManager {
    private val executor: ScheduledExecutorService = ScheduledThreadPoolExecutor(1);
    private val awaiting: MutableMap<String, Long> = ApricotCollectionFactor.hashMap()

    fun awaitManager() {
        this.executor.scheduleWithFixedDelay(
            {
                val needRemoves: MutableList<String> = ApricotCollectionFactor.arrayList()

                this.awaiting.forEach { (identity, timestamp) ->
                    if (TimeUtil.processMillion(timestamp) > 15000) {
                        needRemoves.add(identity)
                    }
                }

                for (needRemove in needRemoves) {
                    this.awaiting.remove(needRemove)
                }
            }, 0, 15, TimeUnit.SECONDS
        )
    }

    fun await(awaitIdentity: ByteArray, runnable: Runnable, trigger: Runnable) {
        val radix: String = Mathematics.radix(
            awaitIdentity, 36
        )

        this.awaiting[radix] = TimeUtil.millions()

        trigger.run()

        while (this.awaiting.containsKey(radix)) {
            Thread.sleep(10)
        }

        runnable.run()
    }

    fun await(awaitIdentity: ByteArray, trigger: Runnable) {
        await(
            awaitIdentity, {}, trigger
        )
    }

    fun <T> awaitGet(awaitIdentity: ByteArray, supplier: Supplier<T>, trigger: Runnable): T {
        val getNow: T = EntrustEnvironment.trys(supplier::get)
        if (getNow == null) {
            await(
                awaitIdentity, trigger
            )
            return supplier.get()
        }
        return getNow
    }

    fun <T> awaitGet(awaitIdentity: ByteArray, supplier: Supplier<T>, trigger: Runnable, forceAwait: Boolean): T {
        return if (forceAwait) {
            await(
                awaitIdentity, trigger
            )
            supplier.get()
        } else {
            EntrustEnvironment.trys(supplier::get)
        }
    }

    fun notice(awaitIdentity: ByteArray) {
        awaiting.remove(
            Mathematics.radix(
                awaitIdentity, 36
            )
        )
    }
}
