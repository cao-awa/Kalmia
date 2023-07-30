package com.github.cao.awa.kalmia.network.packet.inbound.login.password;

import com.github.cao.awa.apricot.annotation.auto.Auto;
import com.github.cao.awa.apricot.io.bytes.reader.BytesReader;
import com.github.cao.awa.kalmia.annotation.auto.event.NetworkEventTarget;
import com.github.cao.awa.kalmia.annotation.auto.network.unsolve.AutoData;
import com.github.cao.awa.kalmia.annotation.auto.network.unsolve.AutoSolvedPacket;
import com.github.cao.awa.kalmia.event.network.inbound.login.password.LoginWithPasswordEvent;
import com.github.cao.awa.kalmia.network.handler.ping.StatelessHandler;
import com.github.cao.awa.kalmia.network.packet.Packet;
import com.github.cao.awa.modmdo.annotation.platform.Client;
import com.github.cao.awa.modmdo.annotation.platform.Server;

@AutoSolvedPacket(100001)
@NetworkEventTarget(LoginWithPasswordEvent.class)
public class LoginWithPasswordPacket extends Packet<StatelessHandler> {
    @AutoData
    private long uid;
    @AutoData
    private byte[] password;

    @Client
    public LoginWithPasswordPacket(long uid, byte[] password) {
        this.uid = uid;
        this.password = password;
    }

    @Auto
    @Server
    public LoginWithPasswordPacket(BytesReader reader) {
        super(reader);
    }

    public long getUid() {
        return this.uid;
    }

    public byte[] getPassword() {
        return this.password;
    }

    public long uid() {
        return this.uid;
    }

    public byte[] password() {
        return this.password;
    }
}
