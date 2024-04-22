package com.github.cao.awa.kalmia.plugin.internal.translation;

import com.github.cao.awa.apricot.annotations.auto.Auto;
import com.github.cao.awa.apricot.annotations.auto.AutoPlugin;
import com.github.cao.awa.apricot.identifier.BytesRandomIdentifier;
import com.github.cao.awa.kalmia.bootstrap.Kalmia;
import com.github.cao.awa.kalmia.env.KalmiaTranslationEnv;
import com.github.cao.awa.kalmia.identity.LongAndExtraIdentity;
import com.github.cao.awa.kalmia.identity.PureExtraIdentity;
import com.github.cao.awa.kalmia.mathematic.Mathematics;
import com.github.cao.awa.kalmia.message.display.ClientMessage;
import com.github.cao.awa.kalmia.message.display.ClientMessageContent;
import com.github.cao.awa.kalmia.plugin.Plugin;
import com.github.cao.awa.kalmia.plugin.internal.eventbus.KalmiaEventBus;
import com.github.cao.awa.kalmia.translation.network.packet.login.feedback.TranslationLoginFailurePacket;
import com.github.cao.awa.kalmia.translation.network.packet.login.feedback.TranslationLoginSuccessPacket;
import com.github.cao.awa.kalmia.translation.network.packet.message.notice.TranslationNewMessageNoticePacket;
import com.github.cao.awa.kalmia.translation.network.packet.message.select.TranslationSelectedMessagePacket;
import com.github.cao.awa.kalmia.translation.network.packet.meta.status.TranslationProxyStatusPacket;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.atomic.AtomicInteger;

@Auto
@AutoPlugin(
        name = "kalmia_translation",
        uuid = "C942B874-2E65-CCB4-8B8C-0C743E7BE817"
)
public class KalmiaTranslation extends Plugin {
    private static final Logger LOGGER = LogManager.getLogger("KalmiaTranslation");

    @Override
    public void onLoad() {
        LOGGER.info("Loading kalmia translation");

        KalmiaEventBus.handshakePreSharedEc.trigger((router, receipt, cipherField) -> {
            KalmiaTranslationEnv.translationRouter(router)
                                .send(new TranslationProxyStatusPacket("status.kalmia.handshake.ec"));
        });

        KalmiaEventBus.loginFailure.trigger((router, receipt, uid, reason) -> {
            KalmiaTranslationEnv.translationRouter(router)
                                .send(new TranslationLoginFailurePacket(uid,
                                                                        reason
                                ));
        });

        KalmiaEventBus.loginSuccess.trigger((router, receipt, uid, token) -> {
            KalmiaTranslationEnv.translationRouter(router)
                                .send(new TranslationLoginSuccessPacket(uid,
                                                                        Mathematics.radix(token,
                                                                                          36
                                                                        )
                                ));
        });

        KalmiaEventBus.serverHello.trigger((router, receipt, testKey, testSha, iv) -> {
            KalmiaTranslationEnv.translationRouter(router)
                                .send(new TranslationProxyStatusPacket("status.kalmia.handshake.hello"));

            KalmiaTranslationEnv.translationRouter(router)
                                .send(new TranslationNewMessageNoticePacket(
                                        new LongAndExtraIdentity(
                                                114514,
                                                BytesRandomIdentifier.create(24)
                                        ),
                                        new ClientMessage(
                                                new LongAndExtraIdentity(114514,
                                                                         BytesRandomIdentifier.create(24)
                                                ),
                                                new PureExtraIdentity(
                                                        BytesRandomIdentifier.create(24)
                                                ),
                                                999,
                                                new ClientMessageContent(
                                                        new LongAndExtraIdentity(114514,
                                                                                 BytesRandomIdentifier.create(24)
                                                        ),
                                                        "source awa",
                                                        "content awa"
                                                )
                                        )
                                ));
        });

        KalmiaEventBus.selectedMessage.trigger(((router, receipt, sessionIdentity, from, to, sessionCurSeq, messages) -> {
            AtomicInteger index = new AtomicInteger();

            KalmiaTranslationEnv.translationRouter(router)
                                .send(new TranslationSelectedMessagePacket(sessionIdentity,
                                                                           messages.stream()
                                                                                   .map(message -> {
                                                                                       long seq = from + index.getAndIncrement();

                                                                                       return ClientMessage.create(sessionIdentity,
                                                                                                                   seq,
                                                                                                                   message
                                                                                       );
                                                                                   })
                                                                                   .toList()
                                ));
        }));
    }

    @Override
    public boolean canLoad() {
        return Kalmia.SERVER.serverBootstrapConfig.get()
                .translation.get()
                .enabled.get();
    }
}
