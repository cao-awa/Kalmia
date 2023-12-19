package com.github.cao.awa.kalmia.network.packet.inbound.message.send;

import com.github.cao.awa.apricot.annotations.auto.Auto;
import com.github.cao.awa.apricot.io.bytes.reader.BytesReader;
import com.github.cao.awa.kalmia.annotations.actor.Getter;
import com.github.cao.awa.kalmia.annotations.auto.event.network.NetworkEventTarget;
import com.github.cao.awa.kalmia.annotations.auto.network.unsolve.AutoData;
import com.github.cao.awa.kalmia.annotations.auto.network.unsolve.AutoSolvedPacket;
import com.github.cao.awa.kalmia.annotations.inaction.DoNotSet;
import com.github.cao.awa.kalmia.constant.KalmiaConstant;
import com.github.cao.awa.kalmia.event.kalmiagram.network.inbound.message.send.SendMessageEvent;
import com.github.cao.awa.kalmia.identity.PureExtraIdentity;
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
    private PureExtraIdentity sessionIdentity;
    @AutoData
    @DoNotSet
    private PureExtraIdentity keyIdentity;
    @AutoData
    @DoNotSet
    private PureExtraIdentity signIdentity;
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
     * Send message.
     *
     * @param sessionIdentity The id of session
     * @param keyIdentity     The id of crypto key
     * @param message         The message
     * @param signIdentity    The id of crypto key
     * @param sign            The sign data
     */
    @Client
    public SendMessagePacket(PureExtraIdentity sessionIdentity, PureExtraIdentity keyIdentity, byte[] message, PureExtraIdentity signIdentity, byte[] sign, boolean disableProcessor) {
        this.sessionIdentity = sessionIdentity;
        this.keyIdentity = keyIdentity;
        this.message = BytesUtil.orEmpty(message);
        this.signIdentity = signIdentity;
        this.sign = BytesUtil.orEmpty(sign);
        this.disableProcessor = disableProcessor;
    }

    @Getter
    public PureExtraIdentity sessionIdentity() {
        return this.sessionIdentity;
    }

    @Getter
    public PureExtraIdentity keyIdentity() {
        return this.keyIdentity;
    }

    @Getter
    public PureExtraIdentity signIdentity() {
        return this.signIdentity;
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
        return ! this.signIdentity.equals(KalmiaConstant.UNMARKED_PURE_IDENTITY);
    }

    public boolean crypted() {
        return ! this.keyIdentity.equals(KalmiaConstant.UNMARKED_PURE_IDENTITY);
    }
}
