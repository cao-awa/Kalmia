package com.github.cao.awa.kalmia.session.manager;

import com.github.cao.awa.kalmia.identity.LongAndExtraIdentity;
import com.github.cao.awa.kalmia.identity.PureExtraIdentity;
import com.github.cao.awa.kalmia.session.Session;
import com.github.cao.awa.kalmia.session.SessionAccessibleData;
import com.github.cao.awa.kalmia.session.database.SessionDatabase;
import com.github.cao.awa.kalmia.setting.Settings;
import org.jetbrains.annotations.Nullable;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class SessionManager {
    private final SessionDatabase database;

    public SessionManager(String path) throws Exception {
        this.database = new SessionDatabase(path);
    }

    public PureExtraIdentity add(Session session) {
        return this.database.add(session);
    }

    public PureExtraIdentity create(Session session) {
        appendCreatedMessage(session);

        return add(session);
    }

    public void createWithOutAdd(Session session) {
        appendCreatedMessage(session);

        set(session.identity(),
            session
        );
    }

    public void appendCreatedMessage(Session session) {
//        Kalmia.SERVER.messageManager()
//                     .send(
//                             session.identity(),
//                              TODO replace to AssembledMessage and TranslateMessage in future.
//                             new UserMessage(
//                                     KalmiaConstant.UNMARKED_PURE_IDENTITY,
//                                     "session.created".getBytes(StandardCharsets.UTF_8),
//                                     KalmiaConstant.UNMARKED_PURE_IDENTITY,
//                                     BytesUtil.EMPTY,
//                                     KalmiaConstant.KALMIA_SERVER_IDENTITY
//                             )
//                     );
    }

    public void set(PureExtraIdentity sessionIdentity, Session session) {
        this.database.set(sessionIdentity,
                          session
        );
    }

    public void delete(PureExtraIdentity sessionIdentity) {
        this.database.remove(sessionIdentity);
    }

    @Nullable
    public Session session(PureExtraIdentity sessionIdentity) {
        return this.database.get(sessionIdentity);
    }

    public void operation(BiConsumer<Long, Session> action) {
        this.database.operation(action);
    }

    public void deleteAll() {
        this.database.deleteAll();
    }

    public void updateAccessible(PureExtraIdentity sessionIdentity, LongAndExtraIdentity accessIdentity, Consumer<SessionAccessibleData> operation) {
        SessionAccessibleData data = this.database.accessible(
                sessionIdentity,
                accessIdentity
        );

        operation.accept(data);

        this.database.accessible(
                sessionIdentity,
                accessIdentity,
                data
        );
    }

    public SessionAccessibleData accessible(PureExtraIdentity sessionIdentity, LongAndExtraIdentity accessIdentity) {
        return this.database.accessible(
                sessionIdentity,
                accessIdentity
        );
    }

    public Settings settings(PureExtraIdentity sessionIdentity) {
        return this.database.settings(sessionIdentity);
    }

    public void settings(PureExtraIdentity sessionIdentity, Settings settings) {
        this.database.settings(sessionIdentity,
                               settings
        );
    }

    public long nextSeq() {
        return this.database.nextSeq();
    }

    public void curSeq(long curSeq) {
        this.database.curSeq(curSeq);
    }
}
