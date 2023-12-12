package com.github.cao.awa.kalmia.network.packet.inbound.message.send;

import com.github.cao.awa.apricot.annotations.auto.Auto;
import com.github.cao.awa.apricot.io.bytes.reader.BytesReader;
import com.github.cao.awa.kalmia.annotations.actor.Getter;
import com.github.cao.awa.kalmia.annotations.auto.event.network.NetworkEventTarget;
import com.github.cao.awa.kalmia.annotations.auto.network.unsolve.AutoData;
import com.github.cao.awa.kalmia.annotations.auto.network.unsolve.AutoSolvedPacket;
import com.github.cao.awa.kalmia.annotations.inaction.DoNotSet;
import com.github.cao.awa.kalmia.event.kalmiagram.network.inbound.message.send.SendMessageEvent;
import com.github.cao.awa.kalmia.network.handler.inbound.AuthedRequestHandler;
import com.github.cao.awa.kalmia.network.packet.Packet;
import com.github.cao.awa.modmdo.annotation.platform.Client;
import com.github.cao.awa.modmdo.annotation.platform.Server;
import com.github.cao.awa.viburnum.util.bytes.BytesUtil;

@AutoSolvedPacket(id = 100, crypto = true)
@NetworkEventTarget(SendMessageEvent.class)
public class SendMessagePacket extends Packet<AuthedRequestHandler> {
    @AutoData
    @DoNotSet
    private long sessionId;
    @AutoData
    @DoNotSet
    private long keyId;
    @AutoData
    @DoNotSet
    private long signId;
    @AutoData
    @DoNotSet
    private byte[] message;
    @AutoData
    @DoNotSet
    private byte[] sign;
    @AutoData
    @DoNotSet
    private boolean disableProcessor;

    @Auto
    @Server
    public SendMessagePacket(BytesReader reader) {
        super(reader);
    }

    /**
     * Send message as signed and crypted.
     *
     * @param sessionId The id of session
     * @param keyId     The id of crypto key
     * @param message   The message
     * @param signId    The id of crypto key
     * @param sign      The sign data
     */
    @Client
    public SendMessagePacket(long sessionId, long keyId, byte[] message, long signId, byte[] sign, boolean disableProcessor) {
        this.sessionId = sessionId;
        this.keyId = keyId;
        this.message = BytesUtil.orEmpty(message);
        this.signId = signId;
        this.sign = BytesUtil.orEmpty(sign);
        this.disableProcessor = disableProcessor;
    }

    @Getter
    public long sessionId() {
        return this.sessionId;
    }

    @Getter
    public long keyId() {
        return this.keyId;
    }

    @Getter
    public long signId() {
        return this.signId;
    }

    @Getter
    public byte[] message() {
        return this.message;
    }

    @Getter
    public byte[] sign() {
        return this.sign;
    }

    @Getter
    public boolean disableProcessor() {
        return this.disableProcessor;
    }

    public boolean signed() {
        return this.signId != - 1;
    }

    public boolean crypted() {
        return this.keyId != - 1;
    }
}
