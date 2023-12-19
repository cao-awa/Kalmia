package com.github.cao.awa.kalmia.network.packet.inbound.login.token;

import com.github.cao.awa.apricot.annotations.auto.Auto;
import com.github.cao.awa.apricot.io.bytes.reader.BytesReader;
import com.github.cao.awa.kalmia.annotations.actor.Getter;
import com.github.cao.awa.kalmia.annotations.auto.event.network.NetworkEventTarget;
import com.github.cao.awa.kalmia.annotations.auto.network.unsolve.AutoData;
import com.github.cao.awa.kalmia.annotations.auto.network.unsolve.AutoSolvedPacket;
import com.github.cao.awa.kalmia.annotations.inaction.DoNotSet;
import com.github.cao.awa.kalmia.event.kalmiagram.network.inbound.login.token.LoginWithTokenEvent;
import com.github.cao.awa.kalmia.identity.LongAndExtraIdentity;
import com.github.cao.awa.kalmia.network.handler.stateless.StatelessHandler;
import com.github.cao.awa.kalmia.network.packet.Packet;
import com.github.cao.awa.modmdo.annotation.platform.Client;
import com.github.cao.awa.modmdo.annotation.platform.Server;

@AutoSolvedPacket(id = 100002, crypto = true)
@NetworkEventTarget(LoginWithTokenEvent.class)
public class LoginWithTokenPacket extends Packet<StatelessHandler> {
    @AutoData
    @DoNotSet
    private LongAndExtraIdentity identity;
    @AutoData
    @DoNotSet
    private byte[] token;

    @Client
    public LoginWithTokenPacket(LongAndExtraIdentity identity, byte[] token) {
        this.identity = identity;
        this.token = token;
    }

    @Auto
    @Server
    public LoginWithTokenPacket(BytesReader reader) {
        super(reader);
    }

    @Getter
    public LongAndExtraIdentity identity() {
        return this.identity;
    }

    @Getter
    public byte[] token() {
        return this.token;
    }
}
