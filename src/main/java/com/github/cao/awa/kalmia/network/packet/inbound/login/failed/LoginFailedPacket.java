package com.github.cao.awa.kalmia.network.packet.inbound.login.failed;

import com.github.cao.awa.apricot.annotation.auto.Auto;
import com.github.cao.awa.apricot.io.bytes.reader.BytesReader;
import com.github.cao.awa.kalmia.annotation.auto.event.NetworkEventTarget;
import com.github.cao.awa.kalmia.annotation.auto.network.unsolve.AutoData;
import com.github.cao.awa.kalmia.annotation.auto.network.unsolve.AutoSolvedPacket;
import com.github.cao.awa.kalmia.event.network.inbound.login.failed.LoginFailedEvent;
import com.github.cao.awa.kalmia.network.handler.ping.StatelessHandler;
import com.github.cao.awa.kalmia.network.packet.Packet;
import com.github.cao.awa.modmdo.annotation.platform.Client;
import com.github.cao.awa.modmdo.annotation.platform.Server;

@AutoSolvedPacket(100008)
@NetworkEventTarget(LoginFailedEvent.class)
public class LoginFailedPacket extends Packet<StatelessHandler> {
    @AutoData
    private long uid;

    @Server
    public LoginFailedPacket(long uid) {
        this.uid = uid;
    }

    @Auto
    @Client
    public LoginFailedPacket(BytesReader reader) {
        super(reader);
    }

    public long uid() {
        return this.uid;
    }
}
