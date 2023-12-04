package com.github.cao.awa.kalmia.session

class SessionAccessibleData(private val accessible: ByteArray) {
    fun accessibleChat(isWhitelist: Boolean): Boolean {
        return if (isWhitelist) this.accessible[0] == 1.toByte() else this.accessible[0] != 0.toByte();
    }

    fun accessibleChat() {
        this.accessible[0] = 1
    }

    fun banChat() {
        this.accessible[0] = 0;
    }

    fun bytes(): ByteArray = this.accessible
}