package com.github.cao.awa.kalmia.attack.replay

import com.github.cao.awa.apricot.util.collection.ApricotCollectionFactor
import com.github.cao.awa.apricot.util.time.TimeUtil
import com.github.cao.awa.kalmia.mathematic.Mathematics
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger

object ReplayAttack {
    private val LOGGER: Logger = LogManager.getLogger("ReplayAttack")

    // private val invalidMarks: List<ByteArray> = ApricotCollectionFactor.timedList(4000)
    private val invalidMarks: MutableList<String> = ApricotCollectionFactor.timedList(4000)

    @JvmStatic
    fun validate(mark: ByteArray, timestamp: Long): Boolean {
        if (TimeUtil.processMillion(timestamp) > 4000) {
            return false
        }

        val radixMark: String = Mathematics.radix(
            mark, 36
        );

        return if (invalidMarks.contains(radixMark)) {
            false
        } else {
            invalidMarks.add(radixMark)
            true
        }
    }
}
