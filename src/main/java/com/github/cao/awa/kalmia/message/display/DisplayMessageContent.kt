package com.github.cao.awa.kalmia.message.display

class DisplayMessageContent(
    private val sourceContent: String,
    private val coverContent: String
) {
    fun sourceContent(): String = this.sourceContent

    fun coverContent(): String = this.coverContent
}