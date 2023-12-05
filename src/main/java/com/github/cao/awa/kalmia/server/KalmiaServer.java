package com.github.cao.awa.kalmia.server;

import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.JSONWriter;
import com.github.cao.awa.apricot.resource.loader.ResourceLoader;
import com.github.cao.awa.apricot.util.io.IOUtil;
import com.github.cao.awa.kalmia.config.kalmiagram.server.bootstrap.ServerBootstrapConfig;
import com.github.cao.awa.kalmia.constant.KalmiaConstant;
import com.github.cao.awa.kalmia.env.KalmiaEnv;
import com.github.cao.awa.kalmia.event.kalmiagram.launch.done.DoneLaunchEvent;
import com.github.cao.awa.kalmia.message.manage.MessageManager;
import com.github.cao.awa.kalmia.network.io.server.KalmiaServerNetworkIo;
import com.github.cao.awa.kalmia.network.packet.factor.unsolve.UnsolvedPacketFactor;
import com.github.cao.awa.kalmia.network.router.kalmia.RequestRouter;
import com.github.cao.awa.kalmia.session.listener.SessionListeners;
import com.github.cao.awa.kalmia.session.manage.SessionManager;
import com.github.cao.awa.kalmia.session.types.communal.CommunalSession;
import com.github.cao.awa.kalmia.user.manage.UserManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class KalmiaServer {
    private static final Logger LOGGER = LogManager.getLogger("KalmiaServer");
    public static ServerBootstrapConfig serverBootstrapConfig;
    private final ServerBootstrapConfig bootstrapConfig;
    private final KalmiaServerNetworkIo networkIo;
    private final MessageManager messageManager;
    private final UserManager userManager;
    private final SessionManager sessionManager;
    private final SessionListeners sessionListeners = new SessionListeners();
    private boolean started;
    private final ExecutorService executor = Executors.newCachedThreadPool();

    public boolean isStarted() {
        return this.started;
    }

    public ServerBootstrapConfig bootstrapConfig() {
        return this.bootstrapConfig;
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

    public KalmiaServer(ServerBootstrapConfig config) {
        try {
            this.bootstrapConfig = config;

            this.networkIo = new KalmiaServerNetworkIo(this);

            this.messageManager = new MessageManager("data/server/msg");
            this.userManager = new UserManager("data/server/usr");
            this.sessionManager = new SessionManager("data/server/session");

            // TODO
            // Test only
            this.userManager.set(1,
                                 KalmiaEnv.testUser1
            );
            this.userManager.set(2,
                                 KalmiaEnv.testUser2
            );

            this.sessionManager.set(0,
                                    new CommunalSession(0)
            );

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void setupBootstrapConfig() throws Exception {
        prepareConfig();

        serverBootstrapConfig = ServerBootstrapConfig.read(
                JSONObject.parse(
                        IOUtil.read(new FileReader(KalmiaConstant.SERVER_CONFIG_PATH))
                ),
                KalmiaEnv.DEFAULT_SERVER_BOOTSTRAP_CONFIG
        );

        rewriteConfig(serverBootstrapConfig);
    }

    public static void rewriteConfig(ServerBootstrapConfig bootstrapConfig) throws Exception {
        LOGGER.info("Rewriting server config");

        IOUtil.write(new FileWriter(KalmiaConstant.SERVER_CONFIG_PATH),
                     bootstrapConfig.toJSON()
                                    .toString(JSONWriter.Feature.PrettyFormat)
        );
    }

    public static void prepareConfig() throws Exception {
        LOGGER.info("Preparing server config");

        File configFile = new File(KalmiaConstant.SERVER_CONFIG_PATH);

        configFile.getParentFile()
                  .mkdirs();

        if (! configFile.isFile()) {
            IOUtil.write(
                    new FileWriter(configFile),
                    IOUtil.read(
                            new InputStreamReader(
                                    ResourceLoader.get(KalmiaConstant.SERVER_DEFAULT_CONFIG_PATH)
                            )
                    )
            );
        }
    }

    public void startup() throws Exception {
        setupNetwork();

        KalmiaEnv.eventFramework.fireEvent(new DoneLaunchEvent());
    }

    public void setupNetwork() throws Exception {
        UnsolvedPacketFactor.register();

        this.started = true;

        task(() -> {
            try {
                this.networkIo.start(
                        this.bootstrapConfig.serverNetwork()
                );
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public void task(Runnable runnable) {
        this.executor.execute(runnable);
    }

    public boolean useEpoll() {
        return true;
    }

    public List<RequestRouter> getRouter(long uid) {
        return this.networkIo.getRouter(uid);
    }

    public void login(long uid, RequestRouter router) {
        this.networkIo.login(uid,
                             router
        );
    }

    public void logout(long uid, RequestRouter router) {
        this.networkIo.logout(uid,
                              router
        );

        this.sessionListeners.unsubscribe(router);
    }

    public void logout(RequestRouter router) {
        this.networkIo.logout(router.uid(),
                              router
        );
    }
}