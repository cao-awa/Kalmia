package com.github.cao.awa.kalmia.network.packet.inbound.login.failed;

import com.github.cao.awa.apricot.io.bytes.reader.BytesReader;
import com.github.cao.awa.kalmia.event.network.login.failed.LoginFailedEvent;
import com.github.cao.awa.kalmia.mathematic.base.SkippedBase256;
import com.github.cao.awa.kalmia.network.handler.inbound.SolvedRequestHandler;
import com.github.cao.awa.kalmia.network.packet.ReadonlyPacket;
import com.github.cao.awa.kalmia.network.packet.request.login.failed.LoginFailedRequest;
import com.github.cao.awa.kalmia.network.packet.unsolve.login.failed.UnsolvedLoginFailedPacket;
import com.github.cao.awa.kalmia.network.router.UnsolvedRequestRouter;
import com.github.cao.awa.modmdo.annotation.platform.Client;

/**
 * @see LoginFailedRequest
 * @see UnsolvedLoginFailedPacket
 */
@Client
public class LoginFailedPacket extends ReadonlyPacket<SolvedRequestHandler> {
    private final long uid;

    public LoginFailedPacket(long uid) {
        this.uid = uid;
    }

    public static LoginFailedPacket create(BytesReader reader) {
        return new LoginFailedPacket(SkippedBase256.readLong(reader)
        );
    }


    @Override
    public void inbound(UnsolvedRequestRouter router, SolvedRequestHandler handler) {
        // Trigger the pre handlers.
        LoginFailedEvent.trigger(this,
                                 router,
                                 handler
        );

        System.out.println("---Login failed---");
        System.out.println("UID: " + this.uid);
    }
}
