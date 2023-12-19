package com.github.cao.awa.kalmia.message.cover.processor.time

import com.github.cao.awa.kalmia.identity.LongAndExtraIdentity
import com.github.cao.awa.kalmia.message.cover.processor.MessageProcessor
import java.nio.charset.StandardCharsets
import java.text.SimpleDateFormat
import java.util.*

class TimeMessageProcessor : MessageProcessor() {
    companion object {
        val ID: UUID = UUID.fromString("080c1c64-1236-4313-b053-5828e453203c")
    }

    override fun process(bytes: ByteArray, sender: LongAndExtraIdentity): ByteArray {
        var str = String(
            bytes,
            StandardCharsets.UTF_8
        )
        val format = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        str = str.replace(
            "#{TIME}",
            format.format(Date())
        )
        return str.toByteArray(StandardCharsets.UTF_8)
    }

    override fun id(): UUID {
        return ID
    }
}