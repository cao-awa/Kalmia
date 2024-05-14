package com.github.cao.awa.kalmia.session

import com.github.cao.awa.sinuatum.manipulate.Manipulate
import java.util.*

class SessionAccessible {
    companion object {
        val DEFAULT_SETTINGS: ByteArray = Manipulate.operation(
                ByteArray(16)
        ) {
            Arrays.fill(it, 127.toByte())
        }

        fun banChat(bytes: ByteArray): ByteArray {
            val arr = ByteArray(DEFAULT_SETTINGS.size)
            moveToCurrent(bytes, arr)
            arr[0] = 0
            return arr
        }

        fun approveChat(bytes: ByteArray): ByteArray {
            val arr = ByteArray(DEFAULT_SETTINGS.size)
            moveToCurrent(bytes, arr)
            arr[0] = 1
            return arr
        }

        private fun moveToCurrent(source: ByteArray, newData: ByteArray) {
            System.arraycopy(
                source,
                0,
                newData,
                0,
                source.size
            );
        }
    }
}