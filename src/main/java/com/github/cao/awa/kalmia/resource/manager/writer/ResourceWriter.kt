package com.github.cao.awa.kalmia.resource.manager.writer

import com.github.cao.awa.apricot.util.time.TimeUtil
import java.io.BufferedOutputStream

class ResourceWriter(private val output: BufferedOutputStream) {
    private val openingTimestamp = TimeUtil.millions()
    private var lastActive = openingTimestamp
    fun write(data: ByteArray) {
        lastActive = TimeUtil.millions()
        output.write(data)
        output.flush()
    }

    fun close() = this.output.close()

    fun openingTimestamp(): Long = this.openingTimestamp

    fun output(): BufferedOutputStream = this.output

    fun lastActive(): Long = this.lastActive
}