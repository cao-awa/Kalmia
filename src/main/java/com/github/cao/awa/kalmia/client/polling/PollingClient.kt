package com.github.cao.awa.kalmia.client.polling

import com.github.cao.awa.apricot.identifier.BytesRandomIdentifier
import com.github.cao.awa.kalmia.client.KalmiaClient
import com.github.cao.awa.kalmia.event.Event
import com.github.cao.awa.kalmia.identity.PureExtraIdentity

import java.util.Queue
import java.util.concurrent.ConcurrentLinkedQueue

class PollingClient(private val delegate: KalmiaClient) {
    companion object {
        lateinit var CLIENT: PollingClient
    }

    private var sessionListenersIdentity: ByteArray = BytesRandomIdentifier.create(24)
    private val stackingNotices: Queue<Event> = ConcurrentLinkedQueue()

    fun curMsgSeq(sessionIdentity: PureExtraIdentity): Long {
        return delegate.messageManager.seq(sessionIdentity)
    }

    fun curSessionListenersIdentity(): ByteArray {
        return sessionListenersIdentity
    }

    fun sessionListenersIdentity(sessionListenersIdentity: ByteArray) {
        this.sessionListenersIdentity = sessionListenersIdentity
    }

    fun curStackingNotice(): Event? {
        if (this.stackingNotices.isEmpty()) {
            return null
        }
        return this.stackingNotices.poll()
    }

    fun stackingNotice(notice: Event) {
        stackingNotices.add(notice)
    }
}
