package com.github.cao.awa.kalmia.message.display

import com.github.cao.awa.kalmia.identity.LongAndExtraIdentity

class ClientMessageContent(
    private val sender: LongAndExtraIdentity,
    private val sourceContent: String,
    private val coverContent: String
) {
    fun sourceContent(): String = this.sourceContent

    fun coverContent(): String = this.coverContent

    fun sender(): LongAndExtraIdentity = this.sender
}