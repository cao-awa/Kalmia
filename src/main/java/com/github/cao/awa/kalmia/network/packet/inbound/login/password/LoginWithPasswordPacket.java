package com.github.cao.awa.kalmia.network.packet.inbound.login.password;

import com.github.cao.awa.apricot.annotations.auto.Auto;
import com.github.cao.awa.apricot.io.bytes.reader.BytesReader;
import com.github.cao.awa.kalmia.annotations.actor.Getter;
import com.github.cao.awa.kalmia.annotations.auto.event.network.NetworkEventTarget;
import com.github.cao.awa.kalmia.annotations.auto.network.unsolve.AutoData;
import com.github.cao.awa.kalmia.annotations.auto.network.unsolve.AutoSolvedPacket;
import com.github.cao.awa.kalmia.annotations.inaction.DoNotSet;
import com.github.cao.awa.kalmia.event.kalmiagram.network.inbound.login.password.LoginWithPasswordEvent;
import com.github.cao.awa.kalmia.identity.LongAndExtraIdentity;
import com.github.cao.awa.kalmia.network.handler.stateless.StatelessHandler;
import com.github.cao.awa.kalmia.network.packet.Packet;
import com.github.cao.awa.modmdo.annotation.platform.Client;
import com.github.cao.awa.modmdo.annotation.platform.Server;

@AutoSolvedPacket(id = 100001, crypto = true)
@NetworkEventTarget(LoginWithPasswordEvent.class)
public class LoginWithPasswordPacket extends Packet<StatelessHandler> {
    @AutoData
    @DoNotSet
    private LongAndExtraIdentity accessIdentity;
    @AutoData
    @DoNotSet
    private String password;

    @Client
    public LoginWithPasswordPacket(LongAndExtraIdentity accessIdentity, String password) {
        this.accessIdentity = accessIdentity;
        this.password = password;
    }

    @Auto
    @Server
    public LoginWithPasswordPacket(BytesReader reader) {
        super(reader);
    }

    @Getter
    public LongAndExtraIdentity accessIdentity() {
        return this.accessIdentity;
    }

    @Getter
    public String password() {
        return this.password;
    }
}
