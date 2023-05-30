package com.github.cao.awa.kalmia.server;

import com.github.cao.awa.apricot.util.time.TimeUtil;
import com.github.cao.awa.kalmia.message.manage.MessageManager;
import com.github.cao.awa.kalmia.network.io.server.KalmiaServerNetworkIo;
import com.github.cao.awa.kalmia.network.packet.factor.unsolve.UnsolvedPacketFactor;
import com.github.cao.awa.kalmia.session.manage.SessionManager;
import com.github.cao.awa.kalmia.user.DefaultUser;
import com.github.cao.awa.kalmia.user.manage.UserManager;

import java.nio.charset.StandardCharsets;

public class KalmiaServer {
    private final KalmiaServerNetworkIo networkIo;
    private final MessageManager messageManager;
    private final UserManager userManager;
    private final SessionManager sessionManager;
    private boolean started;

    public boolean isStarted() {
        return this.started;
    }

    public UserManager userManager() {
        return this.userManager;
    }

    public MessageManager messageManager() {
        return this.messageManager;
    }

    public SessionManager sessionManager() {
        return this.sessionManager;
    }

    public KalmiaServer() {
        try {
            this.networkIo = new KalmiaServerNetworkIo(this);

            this.messageManager = new MessageManager("data/msg");
            this.userManager = new UserManager("data/usr");
            this.sessionManager = new SessionManager("data/session");

            // TODO
            // Test only
            this.userManager.set(1,
                                 new DefaultUser(TimeUtil.millions(),
                                                 "123456".getBytes(StandardCharsets.UTF_8)
                                 )
            );
            this.userManager.set(2,
                                 new DefaultUser(TimeUtil.millions(),
                                                 "123456".getBytes(StandardCharsets.UTF_8)
                                 )
            );
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void startup() throws Exception {
        setupNetwork();
    }

    public void setupNetwork() throws Exception {
        UnsolvedPacketFactor.register();

        this.started = true;

        this.networkIo.start(12345);
    }

    public boolean useEpoll() {
        return true;
    }
}