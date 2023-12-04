package com.github.cao.awa.kalmia.client.polling;

import com.github.cao.awa.apricot.util.collection.ApricotCollectionFactor;
import com.github.cao.awa.kalmia.client.KalmiaClient;
import com.github.cao.awa.kalmia.message.Message;

import java.util.List;

public class PollingClient {
    public static PollingClient CLIENT;
    private final KalmiaClient delegate;

    public PollingClient(KalmiaClient delegate) {
        this.delegate = delegate;
    }

    public long curMsgSeq(long sessionId) {
        return this.delegate.messageManager()
                            .curSeq(sessionId);
    }

    public List<Message> getMessages(long sessionId, long startSelect, long endSelect) {
        List<Message> messages = ApricotCollectionFactor.arrayList();

        this.delegate.messageManager()
                     .operation(sessionId,
                                startSelect,
                                endSelect,
                                (seq, msg) -> messages.add(msg)
                     );
        return messages;
    }

    public Message getMessages(long sessionId, long messageSeq) {
        return this.delegate.messageManager()
                            .get(
                                    sessionId,
                                    messageSeq
                            );
    }
}
