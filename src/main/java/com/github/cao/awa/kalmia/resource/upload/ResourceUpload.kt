package com.github.cao.awa.kalmia.resource.upload

import com.github.cao.awa.kalmia.env.KalmiaEnv
import com.github.cao.awa.kalmia.network.packet.Packet
import com.github.cao.awa.kalmia.network.packet.inbound.resource.write.WriteResourcePacket
import com.github.cao.awa.kalmia.network.router.kalmia.RequestRouter
import com.github.zhuaidadaya.rikaishinikui.handler.universal.entrust.function.TriConsumer
import java.io.BufferedInputStream
import java.io.File
import java.io.FileInputStream

object ResourceUpload {
    @JvmStatic
    private fun upload(file: File, operator: TriConsumer<Int, ByteArray, Boolean>) {
        val reader = BufferedInputStream(FileInputStream(file))
        var startPos = 0
        var length: Int

        val buffer = ByteArray(16384)

        while (true) {
            length = reader.read(buffer)
            startPos += length
            if (length == -1) {
                break
            }
            val data = ByteArray(length)
            System.arraycopy(
                buffer, 0, data, 0, length
            )
            operator.accept(
                startPos, data, length != buffer.size
            )
        }
    }

    @JvmStatic
    fun upload(file: File, router: RequestRouter) {
        val receipt = Packet.createReceipt();

        upload(file) { _, data, isFinal ->
            KalmiaEnv.awaitManager.await(receipt, {

            }, {
                router.send(WriteResourcePacket(data, isFinal).receipt(receipt))
            })
        }
    }
}