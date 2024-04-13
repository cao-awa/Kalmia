package com.github.cao.awa.kalmia.bug

import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger

object BugTrace {
    private val LOGGER: Logger = LogManager.getLogger("BugTracer")

    @JvmStatic
    fun trace(exception: Exception, info: String) {
        trace(
            exception, info, false
        )
    }

    @JvmStatic
    fun trace(exception: Exception, info: String, pleaseReport: Boolean) {
        LOGGER.error((if (pleaseReport) "[Please report] " else "") + info, exception)
    }

    @JvmStatic
    fun trace(info: String, pleaseReport: Boolean) {
        LOGGER.error((if (pleaseReport) "[Please report] " else "") + info)
    }
}
