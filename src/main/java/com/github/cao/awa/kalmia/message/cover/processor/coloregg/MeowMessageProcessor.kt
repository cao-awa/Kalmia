package com.github.cao.awa.kalmia.message.cover.processor.coloregg

import com.github.cao.awa.kalmia.bootstrap.Kalmia
import com.github.cao.awa.kalmia.env.KalmiaEnv
import com.github.cao.awa.kalmia.identity.LongAndExtraIdentity
import com.github.cao.awa.kalmia.message.cover.processor.MessageProcessor
import com.github.cao.awa.kalmia.setting.user.UserSettings
import java.nio.charset.StandardCharsets
import java.util.*

class MeowMessageProcessor : MessageProcessor() {
    companion object {
        @JvmField
        val ID: UUID = UUID.fromString("080c1c64-1236-4313-b053-5828e453203b")
    }

    override fun process(bytes: ByteArray, sender: LongAndExtraIdentity): ByteArray {
        val user = Kalmia.SERVER.userManager()[sender]!!
        val sourceText = String(
            bytes,
            StandardCharsets.UTF_8
        )
        return (sourceText + KalmiaEnv.languageManager.translation(
            user.settings()
                .get(UserSettings.LANGUAGE)
                .languageKey(),
            "coloregg.unsigned.meow"
        )).toByteArray(StandardCharsets.UTF_8)
    }

    override fun id(): UUID {
        return ID
    }
}