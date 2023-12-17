package com.github.cao.awa.kalmia.session;

import com.github.cao.awa.kalmia.bootstrap.Kalmia;
import com.github.cao.awa.kalmia.session.inaccessible.InaccessibleSession;

import java.util.List;

public class Sessions {
    public static final Session INACCESSIBLE = new InaccessibleSession();

    public static List<Long> subscribe(long sessionId, long uid) {
        List<Long> listeners = Kalmia.SERVER.userManager()
                                            .sessionListeners(uid);

        listeners.add(sessionId);

        Kalmia.SERVER.userManager()
                     .sessionListeners(uid,
                                       listeners
                     );

        return listeners;
    }
}
