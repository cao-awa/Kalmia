package com.github.cao.awa.kalmia.client.polling;

import com.github.cao.awa.apricot.identifier.BytesRandomIdentifier;
import com.github.cao.awa.kalmia.client.KalmiaClient;
import com.github.cao.awa.kalmia.event.Event;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class PollingClient {
    public static PollingClient CLIENT;
    private final KalmiaClient delegate;
    private byte[] sessionListenersIdentity = BytesRandomIdentifier.create(24);
    private final Queue<Event> stackingNotices = new ConcurrentLinkedQueue<>();

    public PollingClient(KalmiaClient delegate) {
        this.delegate = delegate;
    }

    public long curMsgSeq(long sessionId) {
        return this.delegate.messageManager()
                            .seq(sessionId);
    }

    public byte[] curSessionListenersIdentity() {
        return this.sessionListenersIdentity;
    }

    public void sessionListenersIdentity(byte[] sessionListenersIdentity) {
        this.sessionListenersIdentity = sessionListenersIdentity;
    }

    public Event curStackingNotice() {
        if (this.stackingNotices.isEmpty()) {
            return null;
        }
        return this.stackingNotices.poll();
    }

    public void stackingNotice(Event notice) {
        this.stackingNotices.add(notice);
    }
}
