package com.github.cao.awa.kalmia.client;

import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.JSONWriter;
import com.github.cao.awa.apricot.resource.loader.ResourceLoader;
import com.github.cao.awa.apricot.util.collection.ApricotCollectionFactor;
import com.github.cao.awa.apricot.util.encryption.Crypto;
import com.github.cao.awa.apricot.util.io.IOUtil;
import com.github.cao.awa.kalmia.config.kalmiagram.client.bootstrap.ClientBootstrapConfig;
import com.github.cao.awa.kalmia.constant.KalmiaConstant;
import com.github.cao.awa.kalmia.env.KalmiaEnv;
import com.github.cao.awa.kalmia.keypair.KeyStoreIdentity;
import com.github.cao.awa.kalmia.keypair.manager.KeypairManager;
import com.github.cao.awa.kalmia.keypair.store.KeyPairStore;
import com.github.cao.awa.kalmia.message.Message;
import com.github.cao.awa.kalmia.message.display.ClientMessage;
import com.github.cao.awa.kalmia.message.manager.MessageManager;
import com.github.cao.awa.kalmia.network.io.client.KalmiaClientNetworkIo;
import com.github.cao.awa.kalmia.network.packet.Packet;
import com.github.cao.awa.kalmia.network.packet.inbound.key.select.SelectKeyStorePacket;
import com.github.cao.awa.kalmia.network.packet.inbound.message.select.SelectMessagePacket;
import com.github.cao.awa.kalmia.network.router.kalmia.RequestRouter;
import com.github.cao.awa.kalmia.session.manager.SessionManager;
import com.github.cao.awa.kalmia.user.manager.UserManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.List;
import java.util.function.Consumer;

public class KalmiaClient {
    private static final Logger LOGGER = LogManager.getLogger("KalmiaClient");
    public static ClientBootstrapConfig clientBootstrapConfig;
    private final ClientBootstrapConfig bootstrapConfig;
    private final MessageManager messageManager;
    private final UserManager userManager;
    private final KeypairManager keypairManager;
    private final SessionManager sessionManager;
    private Consumer<RequestRouter> activeCallback;
    private RequestRouter router;

    public KalmiaClient(ClientBootstrapConfig config) {
        try {
            this.bootstrapConfig = config;

            this.messageManager = new MessageManager("data/client/msg");
            this.userManager = new UserManager("data/client/usr");
            this.keypairManager = new KeypairManager("data/client/keypair");
            this.sessionManager = new SessionManager("data/client/session");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public ClientBootstrapConfig bootstrapConfig() {
        return this.bootstrapConfig;
    }

    public UserManager userManager() {
        return this.userManager;
    }

    public KeypairManager keypairManager() {
        return this.keypairManager;
    }

    public MessageManager messageManager() {
        return this.messageManager;
    }

    public SessionManager sessionManager() {
        return this.sessionManager;
    }

    public static void setupBootstrapConfig() throws Exception {
        prepareConfig();

        clientBootstrapConfig = ClientBootstrapConfig.read(
                JSONObject.parse(
                        IOUtil.read(new FileReader(KalmiaConstant.CLIENT_CONFIG_PATH))
                ),
                KalmiaEnv.DEFAULT_CLIENT_BOOTSTRAP_CONFIG
        );

        rewriteConfig(clientBootstrapConfig);
    }

    public static void rewriteConfig(ClientBootstrapConfig bootstrapConfig) throws Exception {
        LOGGER.info("Rewriting client config");

        IOUtil.write(new FileWriter(KalmiaConstant.CLIENT_CONFIG_PATH),
                     bootstrapConfig.toJSON()
                                    .toString(JSONWriter.Feature.PrettyFormat)
        );
    }

    public static void prepareConfig() throws Exception {
        LOGGER.info("Preparing client config");

        File configFile = new File(KalmiaConstant.CLIENT_CONFIG_PATH);

        configFile.getParentFile()
                  .mkdirs();

        if (! configFile.isFile()) {
            IOUtil.write(
                    new FileWriter(configFile),
                    IOUtil.read(
                            new InputStreamReader(
                                    ResourceLoader.get(KalmiaConstant.CLIENT_DEFAULT_CONFIG_PATH)
                            )
                    )
            );
        }
    }

    public RequestRouter router() {
        return this.router;
    }

    public void router(RequestRouter router) {
        this.router = router;
    }

    public KalmiaClient activeCallback(Consumer<RequestRouter> activeCallback) {
        this.activeCallback = activeCallback;
        return this;
    }

    public Consumer<RequestRouter> activeCallback() {
        return this.activeCallback;
    }

    public void disconnect() {
        this.router.disconnect();
    }

    public boolean useEpoll() {
        return this.bootstrapConfig.clientNetwork()
                                   .useEpoll();
    }

    public void connect() throws Exception {
        new KalmiaClientNetworkIo(this).connect(this.bootstrapConfig.clientNetwork());
    }

    public List<Long> sessionIds() {
        return userManager().sessionListeners(router().uid());
    }

    public long curMsgSeq(long sessionId, boolean awaitGet) {
        if (awaitGet) {
            byte[] receipt = Packet.createReceipt();

            try {
                return KalmiaEnv.awaitManager.awaitGet(receipt,
                                                       () -> messageManager().curSeq(sessionId),
                                                       () -> {
                                                           router().send(new SelectMessagePacket(sessionId,
                                                                                                 0,
                                                                                                 1
                                                           ).receipt(receipt));
                                                       },
                                                       true
                );
            } catch (Exception e) {
                return messageManager().curSeq(sessionId);
            }
        } else {
            return messageManager().curSeq(sessionId);
        }
    }

    public List<ClientMessage> getMessages(long sessionId, long startSelect, long endSelect, boolean awaitGet) {
        List<ClientMessage> messages = ApricotCollectionFactor.arrayList();

        if (awaitGet) {
            byte[] receipt = Packet.createReceipt();

            try {
                KalmiaEnv.awaitManager.awaitGet(receipt,
                                                () -> {
                                                    getMessages(messages,
                                                                sessionId,
                                                                startSelect,
                                                                endSelect
                                                    );

                                                    return null;
                                                },
                                                () -> {
                                                    router().send(new SelectMessagePacket(sessionId,
                                                                                          startSelect,
                                                                                          endSelect
                                                    ).receipt(receipt));
                                                },
                                                true
                );
            } catch (Exception e) {
                messages.clear();

                getMessages(messages,
                            sessionId,
                            startSelect,
                            endSelect
                );
            }
        } else {
            getMessages(messages,
                        sessionId,
                        startSelect,
                        endSelect
            );
        }

        return messages;
    }

    public void getMessages(List<ClientMessage> messages, long sessionId, long startSelect, long endSelect) {
        try {
            messageManager()
                    .operation(sessionId,
                               startSelect,
                               endSelect,
                               (seq, msg) -> {
                                   messages.add(
                                           new ClientMessage(
                                                   msg.identity(),
                                                   sessionId,
                                                   seq,
                                                   msg.display()
                                           )
                                   );
                               }
                    );
        } catch (Exception e) {
            e.printStackTrace();
            messages.clear();
        }
    }

    public ClientMessage getMessages(long sessionId, long messageSeq, boolean awaitGet) {
        Message message = null;
        try {
            if (awaitGet) {
                byte[] receipt = Packet.createReceipt();

                message = KalmiaEnv.awaitManager.awaitGet(
                        receipt,
                        () -> messageManager().get(sessionId,
                                                   messageSeq
                        ),
                        () -> {
                            router().send(new SelectMessagePacket(sessionId,
                                                                  messageSeq,
                                                                  messageSeq
                            ).receipt(receipt));
                        }
                );
            } else {
                message = messageManager().get(sessionId,
                                               messageSeq
                );
            }
        } catch (InterruptedException ignored) {

        }

        if (message == null) {
            return null;
        }

        return new ClientMessage(
                message.identity(),
                sessionId,
                messageSeq,
                message.display()
        );
    }

    public PublicKey getPublicKey(long id, boolean awaitGet) {
        PublicKey publicKey = null;

        try {
            if (awaitGet) {
                byte[] receipt = Packet.createReceipt();

                publicKey = KalmiaEnv.awaitManager.awaitGet(
                        receipt,
                        () -> keypairManager().publicKey(id),
                        () -> router().send(new SelectKeyStorePacket(id).receipt(receipt))
                );
            } else {
                publicKey = keypairManager().publicKey(id);
            }
        } catch (InterruptedException ignored) {

        }

        return publicKey;
    }

    public PrivateKey getPrivateKey(long id, boolean awaitGet) {
        PrivateKey publicKey = null;

        try {
            if (awaitGet) {
                byte[] receipt = Packet.createReceipt();

                publicKey = KalmiaEnv.awaitManager.awaitGet(
                        receipt,
                        () -> decryptPrivateKey(id),
                        () -> router().send(new SelectKeyStorePacket(id).receipt(receipt))
                );
            } else {
                publicKey = decryptPrivateKey(id);
            }
        } catch (InterruptedException ignored) {

        }

        return publicKey;
    }

    public PrivateKey decryptPrivateKey(long id) {
        try {
            KeyPairStore store = keypairManager().getStore(id);
            return KeyStoreIdentity.createPrivateKey(
                    store.type(),
                    Crypto.aesDecrypt(store.privateKey()
                                           .key(),
                                      // TODO
                                      new byte[]{}
                    )
            );
        } catch (Exception e) {
            return null;
        }
    }
}
