package com.github.cao.awa.kalmia.message.display

class DisplayMessage(
    private val sessionId: Long,
    private val seq: Long,
    private val content: DisplayMessageContent
) {
    fun sessionId(): Long = this.sessionId

    fun seq(): Long = this.seq

    fun sourceContent(): String = this.content.sourceContent()

    fun coverContent(): String = this.content.coverContent()

    fun content(): DisplayMessageContent = this.content
}