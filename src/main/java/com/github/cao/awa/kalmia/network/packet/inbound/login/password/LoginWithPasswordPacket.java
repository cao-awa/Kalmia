package com.github.cao.awa.kalmia.network.packet.inbound.login.password;

import com.github.cao.awa.apricot.annotation.auto.Auto;
import com.github.cao.awa.apricot.io.bytes.reader.BytesReader;
import com.github.cao.awa.kalmia.annotation.actor.Getter;
import com.github.cao.awa.kalmia.annotation.auto.event.network.NetworkEventTarget;
import com.github.cao.awa.kalmia.annotation.auto.network.unsolve.AutoData;
import com.github.cao.awa.kalmia.annotation.auto.network.unsolve.AutoSolvedPacket;
import com.github.cao.awa.kalmia.annotation.inaction.DoNotSet;
import com.github.cao.awa.kalmia.event.kalmiagram.network.inbound.login.password.LoginWithPasswordEvent;
import com.github.cao.awa.kalmia.network.handler.stateless.StatelessHandler;
import com.github.cao.awa.kalmia.network.packet.Packet;
import com.github.cao.awa.modmdo.annotation.platform.Client;
import com.github.cao.awa.modmdo.annotation.platform.Server;

@AutoSolvedPacket(id = 100001)
@NetworkEventTarget(LoginWithPasswordEvent.class)
public class LoginWithPasswordPacket extends Packet<StatelessHandler> {
    @AutoData
    @DoNotSet
    private long uid;
    @AutoData
    @DoNotSet
    private String password;

    @Client
    public LoginWithPasswordPacket(long uid, String password) {
        this.uid = uid;
        this.password = password;
    }

    @Auto
    @Server
    public LoginWithPasswordPacket(BytesReader reader) {
        super(reader);
    }

    @Getter
    public long uid() {
        return this.uid;
    }

    @Getter
    public String password() {
        return this.password;
    }
}
