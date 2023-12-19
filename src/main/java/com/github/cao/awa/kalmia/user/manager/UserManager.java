package com.github.cao.awa.kalmia.user.manager;

import com.github.cao.awa.kalmia.identity.LongAndExtraIdentity;
import com.github.cao.awa.kalmia.identity.PureExtraIdentity;
import com.github.cao.awa.kalmia.session.communal.CommunalSession;
import com.github.cao.awa.kalmia.user.User;
import com.github.cao.awa.kalmia.user.database.UserDatabase;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Set;
import java.util.function.BiConsumer;

public class UserManager {
    private final UserDatabase database;

    public UserManager(String path) throws Exception {
        this.database = new UserDatabase(path);
    }

    public long add(User user) {
        return this.database.add(user);
    }

    public void set(LongAndExtraIdentity accessIdentity, User user) {
        this.database.set(accessIdentity,
                          user
        );
    }

    public void delete(LongAndExtraIdentity accessIdentity) {
        this.database.markUseless(accessIdentity);
    }

    @Nullable
    public User get(LongAndExtraIdentity accessIdentity) {
        return this.database.get(accessIdentity);
    }

    public void operation(BiConsumer<Long, User> action) {
        this.database.operation(action);
    }

    public void deleteAll() {
        this.database.uselessAll();
    }

    public PureExtraIdentity duetSession(LongAndExtraIdentity selfIdentity, LongAndExtraIdentity targetIdentity) {
        return this.database.session(selfIdentity,
                                     targetIdentity
        );
    }

    public void duetSession(LongAndExtraIdentity selfIdentity, LongAndExtraIdentity targetIdentity, PureExtraIdentity sessionIdentity) {
        this.database.session(selfIdentity,
                              targetIdentity,
                              sessionIdentity
        );
    }

    public List<PureExtraIdentity> sessionListeners(LongAndExtraIdentity accessIdentity) {
        List<PureExtraIdentity> list = this.database.sessionListeners(accessIdentity);
        // TODO test only
        list.add(CommunalSession.TEST_COMMUNAL_IDENTITY);
        return list;
    }

    public void sessionListeners(LongAndExtraIdentity accessIdentity, List<PureExtraIdentity> listeners) {
        this.database.sessionListeners(accessIdentity,
                                       listeners
        );
    }

    public Set<PureExtraIdentity> keyStores(LongAndExtraIdentity accessIdentity) {
        return this.database.keyStores(accessIdentity);
    }

    public void keyStores(LongAndExtraIdentity accessIdentity, Set<PureExtraIdentity> stores) {
        this.database.keyStores(accessIdentity,
                                stores
        );
    }
}
