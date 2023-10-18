package com.github.cao.awa.kalmia.bootstrap;

import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.JSONWriter;
import com.github.cao.awa.apricot.resource.loader.ResourceLoader;
import com.github.cao.awa.apricot.util.io.IOUtil;
import com.github.cao.awa.kalmia.config.kalmiagram.bootstrap.ServerBootstrapConfig;
import com.github.cao.awa.kalmia.constant.KalmiaConstant;
import com.github.cao.awa.kalmia.env.KalmiaEnv;
import com.github.cao.awa.kalmia.message.DeletedMessage;
import com.github.cao.awa.kalmia.message.factor.MessageFactor;
import com.github.cao.awa.kalmia.message.plains.PlainsMessage;
import com.github.cao.awa.kalmia.server.KalmiaServer;
import com.github.cao.awa.kalmia.user.DefaultUser;
import com.github.cao.awa.kalmia.user.DeletedUser;
import com.github.cao.awa.kalmia.user.factor.UserFactor;
import com.github.cao.awa.kalmia.user.key.ServerKeyPairStoreFactor;
import com.github.cao.awa.kalmia.user.key.ec.EcServerKeyPair;
import com.github.cao.awa.kalmia.user.key.rsa.RsaServerKeyPair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStreamReader;

public class Kalmia {
    private static final Logger LOGGER = LogManager.getLogger("Kalmia");
    public static ServerBootstrapConfig bootstrapConfig;
    public static KalmiaServer SERVER;

    public static void main(String[] args) {
        setupEnvironment();

        try {
            startServer();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void startServer() throws Exception {
        LOGGER.info("Starting kalmia server");

        setupBootstrapConfig();

        SERVER = new KalmiaServer(bootstrapConfig);

        LOGGER.info("Setup kalmia server");

        KalmiaEnv.setupServer();

        SERVER.startup();
    }

    public static void setupBootstrapConfig() throws Exception {
        prepareConfig();

        bootstrapConfig = ServerBootstrapConfig.read(
                JSONObject.parse(
                        IOUtil.read(new FileReader(KalmiaConstant.SERVER_CONFIG_PATH))
                ),
                KalmiaEnv.DEFAULT_BOOTSTRAP_CONFIG
        );

        rewriteConfig(bootstrapConfig);
    }

    public static void rewriteConfig(ServerBootstrapConfig bootstrapConfig) throws Exception {
        LOGGER.info("Rewriting server config");

        IOUtil.write(new FileWriter(KalmiaConstant.SERVER_CONFIG_PATH),
                     bootstrapConfig.toJSON()
                                    .toString(JSONWriter.Feature.PrettyFormat)
        );

        System.out.println(bootstrapConfig.toJSON()
                                          .toString(JSONWriter.Feature.PrettyFormat));
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

    public static void setupEnvironment() {
        UserFactor.register(- 1,
                            DeletedUser :: create
        );
        UserFactor.register(0,
                            DefaultUser :: create
        );

        MessageFactor.register(0,
                               PlainsMessage :: create
        );
        MessageFactor.register(- 1,
                               DeletedMessage :: create
        );

        ServerKeyPairStoreFactor.register(0,
                                          RsaServerKeyPair :: new
        );
        ServerKeyPairStoreFactor.register(1,
                                          EcServerKeyPair :: new
        );
    }
}
