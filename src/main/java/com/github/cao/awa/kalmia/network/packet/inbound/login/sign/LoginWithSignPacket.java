package com.github.cao.awa.kalmia.network.packet.inbound.login.sign;

import com.github.cao.awa.apricot.annotations.auto.Auto;
import com.github.cao.awa.apricot.util.encryption.Crypto;
import com.github.cao.awa.kalmia.annotations.auto.event.network.NetworkEventTarget;
import com.github.cao.awa.kalmia.annotations.auto.network.unsolve.AutoAllData;
import com.github.cao.awa.kalmia.annotations.auto.network.unsolve.AutoSolvedPacket;
import com.github.cao.awa.kalmia.bug.BugTrace;
import com.github.cao.awa.kalmia.env.KalmiaEnv;
import com.github.cao.awa.kalmia.event.kalmiagram.network.inbound.login.sign.LoginWithSignEvent;
import com.github.cao.awa.kalmia.identity.LongAndExtraIdentity;
import com.github.cao.awa.kalmia.network.handler.stateless.StatelessHandler;
import com.github.cao.awa.kalmia.network.packet.Packet;
import com.github.cao.awa.modmdo.annotation.platform.Client;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.security.interfaces.ECPrivateKey;

@Getter
@AutoAllData
@NoArgsConstructor
@Accessors(fluent = true)
@AutoSolvedPacket(id = 100003, crypto = true)
@NetworkEventTarget(LoginWithSignEvent.class)
public class LoginWithSignPacket extends Packet<StatelessHandler> {
    private LongAndExtraIdentity accessIdentity;
    private byte[] challengeData;

    // Only support ec to use sign login.
    @Auto
    @Client
    public LoginWithSignPacket(LongAndExtraIdentity accessIdentity, ECPrivateKey privateKey) {
        this.accessIdentity = accessIdentity;
        try {
            this.challengeData = Crypto.ecSign(
                    KalmiaEnv.CHALLENGE_DATA,
                    privateKey
            );
        } catch (Exception e) {
            BugTrace.trace(e,
                           "Failed sign the data"
            );
        }
    }
}
