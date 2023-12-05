package com.github.cao.awa.kalmia.message.display

class ClientMessageContent(
    private val sender: Long,
    private val sourceContent: String,
    private val coverContent: String
) {
    fun sourceContent(): String = this.sourceContent

    fun coverContent(): String = this.coverContent

    fun sender(): Long = this.sender
}