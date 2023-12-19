package com.github.cao.awa.kalmia.session;

import com.github.cao.awa.kalmia.bootstrap.Kalmia;
import com.github.cao.awa.kalmia.identity.LongAndExtraIdentity;
import com.github.cao.awa.kalmia.identity.PureExtraIdentity;
import com.github.cao.awa.kalmia.session.inaccessible.InaccessibleSession;

import java.util.List;

public class Sessions {
    public static final Session INACCESSIBLE = new InaccessibleSession();

    public static List<PureExtraIdentity> subscribe(PureExtraIdentity sessionIdentity, LongAndExtraIdentity accessIdentity) {
        List<PureExtraIdentity> listeners = Kalmia.SERVER.userManager()
                                                         .sessionListeners(accessIdentity);

        listeners.add(sessionIdentity);

        Kalmia.SERVER.userManager()
                     .sessionListeners(accessIdentity,
                                       listeners
                     );

        return listeners;
    }
}
