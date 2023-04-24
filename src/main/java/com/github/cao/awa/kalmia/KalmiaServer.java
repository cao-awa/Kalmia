package com.github.cao.awa.kalmia;

import com.github.cao.awa.kalmia.chat.session.manager.SessionManager;
import com.github.cao.awa.kalmia.message.manage.MessageManager;
import com.github.cao.awa.kalmia.network.io.server.KalmiaServerNetworkIo;
import com.github.cao.awa.kalmia.network.packet.factor.unsolve.UnsolvedPacketFactor;
import com.github.cao.awa.kalmia.user.manage.UserManager;

public class KalmiaServer {
    private final KalmiaServerNetworkIo networkIo;
    private final MessageManager messageManager;
    private final UserManager userManager;
    private final SessionManager sessionManager;

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
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void startup() throws Exception {
        setupNetwork();
    }

    public void setupNetwork() throws Exception {
        UnsolvedPacketFactor.register();

        this.networkIo.start(12345);
    }

    public boolean useEpoll() {
        return true;
    }
}