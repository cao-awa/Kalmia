package com.github.cao.awa.kalmia.bug;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class BugTrace {
    private static final Logger LOGGER = LogManager.getLogger("BugTracer");

    public static void trace(Exception exception, String info) {
        trace(exception,
              info,
              false
        );
    }

    public static void trace(Exception exception, String info, boolean pleaseReport) {
        LOGGER.error((pleaseReport ? "[Please report] " : "") + info,
                     exception
        );
    }

    public static void trace(String info) {
        trace(info,
              false
        );
    }

    public static void trace(String info, boolean pleaseReport) {
        LOGGER.error((pleaseReport ? "[Please report] " : "") + info);
    }
}
