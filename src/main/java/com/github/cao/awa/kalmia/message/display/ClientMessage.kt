package com.github.cao.awa.kalmia.message.display

import com.github.cao.awa.kalmia.identity.MillsAndExtraIdentity

class ClientMessage(
    private val identity: MillsAndExtraIdentity,
    private val sessionId: Long,
    private val seq: Long,
    private val content: ClientMessageContent
) {
    fun sessionId(): Long = this.sessionId

    fun seq(): Long = this.seq

    fun sourceContent(): String = this.content.sourceContent()

    fun coverContent(): String = this.content.coverContent()

    fun sender(): Long = this.content.sender()

    fun content(): ClientMessageContent = this.content

    fun identity(): MillsAndExtraIdentity = this.identity

    fun timestamp(): Long = this.identity.mills()
}