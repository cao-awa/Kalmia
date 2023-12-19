package com.github.cao.awa.kalmia.message.display

import com.github.cao.awa.kalmia.identity.LongAndExtraIdentity
import com.github.cao.awa.kalmia.identity.PureExtraIdentity

class ClientMessage(
    private val identity: LongAndExtraIdentity,
    private val sessionIdentity: PureExtraIdentity,
    private val seq: Long,
    private val content: ClientMessageContent
) {
    fun sessionIdentity(): PureExtraIdentity = this.sessionIdentity

    fun seq(): Long = this.seq

    fun sourceContent(): String = this.content.sourceContent()

    fun coverContent(): String = this.content.coverContent()

    fun sender(): LongAndExtraIdentity = this.content.sender()

    fun content(): ClientMessageContent = this.content

    fun identity(): LongAndExtraIdentity = this.identity

    fun timestamp(): Long = this.identity.longValue()
}