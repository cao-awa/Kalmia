package com.github.cao.awa.kalmia.resource.manager

import com.github.cao.awa.apricot.util.collection.ApricotCollectionFactor
import com.github.cao.awa.apricot.util.digger.MessageDigger
import com.github.cao.awa.apricot.util.digger.MessageDigger.Sha3
import com.github.cao.awa.apricot.util.io.IOUtil
import com.github.cao.awa.apricot.util.time.TimeUtil
import com.github.cao.awa.kalmia.mathematic.Mathematics
import com.github.cao.awa.kalmia.resource.manager.writer.ResourceWriter
import com.github.cao.awa.sinuatum.function.consumer.TriConsumer
import java.io.*
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.ScheduledThreadPoolExecutor
import java.util.concurrent.TimeUnit


class ResourcesManager(private val path: String) {
    private val executor: ScheduledExecutorService = ScheduledThreadPoolExecutor(1)
    private val writing = mutableMapOf<String, ResourceWriter>()

    init {
        File(this.path).mkdirs()

        this.executor.scheduleWithFixedDelay(
            {
                val needRemoves: MutableList<String> = ApricotCollectionFactor.arrayList()
                this.writing.forEach { (receipt, writer) ->
                    if (TimeUtil.processMillion(writer.lastActive()) > (120 * 1000)) {
                        needRemoves.add(receipt)
                        writer.close()
                    }
                }

                needRemoves.forEach(this.writing::remove)
            },
            0,
            30,
            TimeUnit.SECONDS
        )
    }

    fun getResource(identity: ByteArray): ByteArray? {
        return getResource(getResourceKey(identity))
    }

    fun getResource(resourceKey: String): ByteArray? {
        return try {
            IOUtil.readBytes(FileInputStream("${this.path}/$resourceKey"))
        } catch (e: Exception) {
            null
        }
    }

    fun getShardedResource(resourceKey: String, shardSize: Int, operator: TriConsumer<Int, ByteArray, Boolean>) {
        val reader = BufferedInputStream(FileInputStream("${this.path}/$resourceKey"))
        var startPos = 0
        var length: Int

        val buffer = ByteArray(shardSize)

        while (true) {
            length = reader.read(buffer)
            startPos += length
            if (length == -1) {
                break
            }
            val data = ByteArray(length)
            System.arraycopy(
                buffer,
                0,
                data,
                0,
                length
            )
            operator.accept(
                startPos,
                data,
                length != buffer.size
            )
        }
    }

    fun setResource(data: ByteArray): String {
        try {
            val resourceKey = MessageDigger.digest(data, Sha3.SHA_512)

            IOUtil.write(FileOutputStream("${this.path}/$resourceKey"), data)

            return resourceKey
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return ""
    }

    fun requestWrite(receipt: String, data: ByteArray, isFinal: Boolean, doneCallback: Runnable) {
        val file = File("${this.path}/${receipt}.temp");

        if (this.writing[receipt] == null) {
            this.writing[receipt] = ResourceWriter(BufferedOutputStream(FileOutputStream(file)))
        }
        val writer = this.writing[receipt]

        writer!!.write(data)

        if (isFinal) {
            writer.close()
            doneWriting(file)
            this.writing.remove(receipt)
        }

        doneCallback.run()
    }

    private fun doneWriting(temp: File) {
        val resourceKey = MessageDigger.digestFile(temp, Sha3.SHA_512)
        val resourceFile = File("${this.path}/${resourceKey}")
        IOUtil.copy(temp, resourceFile);
        temp.delete()
    }

    fun getResourceKey(identity: ByteArray): String {
        return Mathematics.radix(
            identity,
            36
        )
    }
}