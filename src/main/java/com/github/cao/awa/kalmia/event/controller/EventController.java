package com.github.cao.awa.kalmia.event.controller;

import com.github.cao.awa.apricot.anntation.Unsupported;
import com.github.cao.awa.kalmia.event.controller.cancel.EventCancelException;

@Unsupported("NOT_SAFELY_CANCEL")
public class EventController {
    public static void cancel() {
        throw new EventCancelException();
    }

    public static void cancel(String reason) {
        throw new EventCancelException(reason);
    }
}
