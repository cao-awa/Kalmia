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
import com.github.cao.awa.kalmia.identity.PureExtraIdentity;
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
import java.util.Set;
import java.util.function.BiConsumer;
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
                                    ResourceLoader.stream(KalmiaConstant.CLIENT_DEFAULT_CONFIG_PATH)
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

    public Set<PureExtraIdentity> sessionIds() {
        return userManager().sessionListeners(router().accessIdentity());
    }

    public long curMsgSeq(PureExtraIdentity sessionIdentity, boolean awaitGet) {
        if (awaitGet) {
            byte[] receipt = Packet.createReceipt();

            try {
                return KalmiaEnv.awaitManager.awaitGet(receipt,
                                                       () -> messageManager().seq(sessionIdentity),
                                                       () -> {
                                                           router().send(new SelectMessagePacket(sessionIdentity,
                                                                                                 0,
                                                                                                 0
                                                           ).receipt(receipt));
                                                       },
                                                       true
                );
            } catch (Exception e) {
                return messageManager().seq(sessionIdentity);
            }
        } else {
            return messageManager().seq(sessionIdentity);
        }
    }

    public List<Message> getMessages(PureExtraIdentity sessionIdentity, long startSelect, long endSelect, boolean awaitGet) {
        List<Message> messages = ApricotCollectionFactor.arrayList();

        if (awaitGet) {
            byte[] receipt = Packet.createReceipt();

            try {
                KalmiaEnv.awaitManager.awaitGet(receipt,
                                                () -> {
                                                    operationMessages((seq, msg) -> messages.add(msg),
                                                                      sessionIdentity,
                                                                      startSelect,
                                                                      endSelect
                                                    );

                                                    return null;
                                                },
                                                () -> {
                                                    router().send(new SelectMessagePacket(sessionIdentity,
                                                                                          startSelect,
                                                                                          endSelect
                                                    ).receipt(receipt));
                                                },
                                                true
                );
            } catch (Exception e) {
                messages.clear();

                operationMessages((seq, msg) -> messages.add(msg),
                                  sessionIdentity,
                                  startSelect,
                                  endSelect
                );
            }
        } else {
            operationMessages((seq, msg) -> messages.add(msg),
                              sessionIdentity,
                              startSelect,
                              endSelect
            );
        }

        return messages;
    }

    public void operationMessages(BiConsumer<Long, Message> operator, PureExtraIdentity sessionIdentity, long startSelect, long endSelect) {
        messageManager()
                .operation(sessionIdentity,
                           startSelect,
                           endSelect,
                           operator
                );
    }

    public Message getMessages(PureExtraIdentity sessionIdentity, long messageSeq, boolean awaitGet) {
        Message message = null;
        try {
            if (awaitGet) {
                byte[] receipt = Packet.createReceipt();

                message = KalmiaEnv.awaitManager.awaitGet(
                        receipt,
                        () -> messageManager().get(sessionIdentity,
                                                   messageSeq
                        ),
                        () -> {
                            router().send(new SelectMessagePacket(sessionIdentity,
                                                                  messageSeq,
                                                                  messageSeq
                            ).receipt(receipt));
                        }
                );
            } else {
                message = messageManager().get(sessionIdentity,
                                               messageSeq
                );
            }
        } catch (InterruptedException ignored) {

        }

        return message;
    }

    public ClientMessage getClientMessage(PureExtraIdentity sessionIdentity, long messageSeq, boolean awaitGet) {
        Message message = getMessages(sessionIdentity,
                                      messageSeq,
                                      awaitGet
        );
        return new ClientMessage(
                message.identity(),
                sessionIdentity,
                messageSeq,
                message.display()
        );
    }

    public PublicKey getPublicKey(PureExtraIdentity identity, boolean awaitGet) {
        PublicKey publicKey = null;

        try {
            if (awaitGet) {
                byte[] receipt = Packet.createReceipt();

                publicKey = KalmiaEnv.awaitManager.awaitGet(
                        receipt,
                        () -> keypairManager().publicKey(identity),
                        () -> router().send(new SelectKeyStorePacket(identity).receipt(receipt))
                );
            } else {
                publicKey = keypairManager().publicKey(identity);
            }
        } catch (InterruptedException ignored) {

        }

        return publicKey;
    }

    public PrivateKey getPrivateKey(PureExtraIdentity identity, boolean awaitGet) {
        PrivateKey publicKey = null;

        try {
            if (awaitGet) {
                byte[] receipt = Packet.createReceipt();

                publicKey = KalmiaEnv.awaitManager.awaitGet(
                        receipt,
                        () -> decryptPrivateKey(identity),
                        () -> router().send(new SelectKeyStorePacket(identity).receipt(receipt))
                );
            } else {
                publicKey = decryptPrivateKey(identity);
            }
        } catch (InterruptedException ignored) {

        }

        return publicKey;
    }

    public PrivateKey decryptPrivateKey(PureExtraIdentity identity) {
        try {
            KeyPairStore store = keypairManager().getStore(identity);
            return KeyStoreIdentity.createPrivateKey(
                    store.type(),
                    Crypto.aesDecrypt(store.privateKey()
                                           .key(),
                                      // TODO
                                      KalmiaEnv.testUer2AesCipher
                    )
            );
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

//    public Session getSession(PureExtraIdentity identity, boolean awaitGet) {
//
//    }
}
