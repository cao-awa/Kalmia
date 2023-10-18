package com.github.cao.awa.kalmia.network.packet.inbound.login.success;

import com.github.cao.awa.apricot.annotation.auto.Auto;
import com.github.cao.awa.apricot.io.bytes.reader.BytesReader;
import com.github.cao.awa.kalmia.annotation.actor.Getter;
import com.github.cao.awa.kalmia.annotation.auto.event.network.NetworkEventTarget;
import com.github.cao.awa.kalmia.annotation.auto.network.unsolve.AutoData;
import com.github.cao.awa.kalmia.annotation.auto.network.unsolve.AutoSolvedPacket;
import com.github.cao.awa.kalmia.annotation.inaction.DoNotSet;
import com.github.cao.awa.kalmia.event.kalmiagram.network.inbound.login.success.LoginSuccessEvent;
import com.github.cao.awa.kalmia.network.handler.stateless.StatelessHandler;
import com.github.cao.awa.kalmia.network.packet.Packet;
import com.github.cao.awa.modmdo.annotation.platform.Client;
import com.github.cao.awa.modmdo.annotation.platform.Server;

@AutoSolvedPacket(id = 100009)
@NetworkEventTarget(LoginSuccessEvent.class)
public class LoginSuccessPacket extends Packet<StatelessHandler> {
    @AutoData
    @DoNotSet
    private long uid;
    @AutoData
    @DoNotSet
    private byte[] token;

    @Server
    public LoginSuccessPacket(long uid, byte[] token) {
        this.uid = uid;
        this.token = token;
    }

    @Auto
    @Client
    public LoginSuccessPacket(BytesReader reader) {
        super(reader);
    }

    @Getter
    public long uid() {
        return this.uid;
    }

    @Getter
    public byte[] token() {
        return this.token;
    }
}
