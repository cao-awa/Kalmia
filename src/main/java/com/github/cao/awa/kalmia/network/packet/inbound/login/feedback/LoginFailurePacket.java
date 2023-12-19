package com.github.cao.awa.kalmia.network.packet.inbound.login.feedback;

import com.github.cao.awa.apricot.annotations.auto.Auto;
import com.github.cao.awa.apricot.io.bytes.reader.BytesReader;
import com.github.cao.awa.kalmia.annotations.actor.Getter;
import com.github.cao.awa.kalmia.annotations.auto.event.network.NetworkEventTarget;
import com.github.cao.awa.kalmia.annotations.auto.network.unsolve.AutoData;
import com.github.cao.awa.kalmia.annotations.auto.network.unsolve.AutoSolvedPacket;
import com.github.cao.awa.kalmia.annotations.inaction.DoNotSet;
import com.github.cao.awa.kalmia.event.kalmiagram.network.inbound.login.feedback.LoginFailureEvent;
import com.github.cao.awa.kalmia.identity.LongAndExtraIdentity;
import com.github.cao.awa.kalmia.network.handler.stateless.StatelessHandler;
import com.github.cao.awa.kalmia.network.packet.Packet;
import com.github.cao.awa.modmdo.annotation.platform.Client;
import com.github.cao.awa.modmdo.annotation.platform.Server;

@AutoSolvedPacket(id = 100008, crypto = true)
@NetworkEventTarget(LoginFailureEvent.class)
public class LoginFailurePacket extends Packet<StatelessHandler> {
    @AutoData
    @DoNotSet
    private LongAndExtraIdentity accessIdentity;
    @AutoData
    @DoNotSet
    private String reason;

    @Server
    public LoginFailurePacket(LongAndExtraIdentity accessIdentity, String reason) {
        this.accessIdentity = accessIdentity;
        this.reason = reason;
    }

    @Auto
    @Client
    public LoginFailurePacket(BytesReader reader) {
        super(reader);
    }

    @Getter
    public LongAndExtraIdentity accessIdentity() {
        return this.accessIdentity;
    }

    @Getter
    public String reason() {
        return this.reason;
    }
}
