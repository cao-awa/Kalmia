package com.github.cao.awa.kalmia.network.packet.inbound.login.success;

import com.github.cao.awa.apricot.identifier.BytesRandomIdentifier;
import com.github.cao.awa.apricot.io.bytes.reader.BytesReader;
import com.github.cao.awa.kalmia.event.network.login.success.LoginSuccessEvent;
import com.github.cao.awa.kalmia.mathematic.Mathematics;
import com.github.cao.awa.kalmia.mathematic.base.SkippedBase256;
import com.github.cao.awa.kalmia.network.handler.inbound.SolvedRequestHandler;
import com.github.cao.awa.kalmia.network.packet.ReadonlyPacket;
import com.github.cao.awa.kalmia.network.packet.request.login.success.LoginSuccessRequest;
import com.github.cao.awa.kalmia.network.packet.request.message.send.SendMessageRequest;
import com.github.cao.awa.kalmia.network.packet.unsolve.login.success.UnsolvedLoginSuccessPacket;
import com.github.cao.awa.kalmia.network.router.UnsolvedRequestRouter;
import com.github.cao.awa.modmdo.annotation.platform.Client;

/**
 * @see LoginSuccessRequest
 * @see UnsolvedLoginSuccessPacket
 */
@Client
public class LoginSuccessPacket extends ReadonlyPacket<SolvedRequestHandler> {
    private final long uid;
    private final byte[] token;

    public LoginSuccessPacket(long uid, byte[] token) {
        this.uid = uid;
        this.token = token;
    }

    public static LoginSuccessPacket create(BytesReader reader) {
        return new LoginSuccessPacket(SkippedBase256.readLong(reader),
                                      reader.all()
        );
    }


    @Override
    public void inbound(UnsolvedRequestRouter router, SolvedRequestHandler handler) {
        // Trigger the pre handlers.
        LoginSuccessEvent.trigger(this,
                                  router,
                                  handler
        );

        System.out.println("---Login success---");
        System.out.println("UID: " + this.uid);
        System.out.println("Token: " + Mathematics.radix(this.token,
                                                         36
        ));

        router.send(new SendMessageRequest(123,
                                           BytesRandomIdentifier.create(16),
                                           "www".getBytes()
        ));
    }
}
