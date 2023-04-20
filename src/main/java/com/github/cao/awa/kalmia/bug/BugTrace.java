package com.github.cao.awa.kalmia.bug;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class BugTrace {
    private static final Logger LOGGER = LogManager.getLogger("BugTracer");

    public static void trace(Exception exception, String info) {
        LOGGER.error(info,
                     exception
        );
    }
}
