package com.github.cao.awa.kalmia.session

import com.github.zhuaidadaya.rikaishinikui.handler.universal.entrust.EntrustEnvironment
import java.util.*

class SessionAccessible {
    companion object {
        val DEFAULT_SETTINGS: ByteArray = EntrustEnvironment.operation(
                ByteArray(6)
        ) {
            Arrays.fill(it, 127.toByte())
        }

        fun banChat(bytes: ByteArray): ByteArray {
            val arr = ByteArray(DEFAULT_SETTINGS.size);
            System.arraycopy(
                    bytes,
                    0,
                    arr,
                    0,
                    bytes.size
            );
            arr[0] = 0;
            return arr;
        }

        fun approveChat(bytes: ByteArray): ByteArray {
            val arr = ByteArray(DEFAULT_SETTINGS.size);
            System.arraycopy(
                    bytes,
                    0,
                    arr,
                    0,
                    bytes.size
            );
            arr[0] = 1;
            return arr;
        }
    }
}