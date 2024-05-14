package com.github.cao.awa.kalmia.session.listener;

import com.github.cao.awa.apricot.util.collection.ApricotCollectionFactor;
import com.github.cao.awa.kalmia.network.router.kalmia.RequestRouter;
import com.github.cao.awa.sinuatum.manipulate.Manipulate;

import java.util.List;
import java.util.Map;

public class SessionListeners {
    private static final List<?> EMPTY_LIST = ApricotCollectionFactor.arrayList();
    private final Map<Long, List<RequestRouter>> sessions = ApricotCollectionFactor.hashMap();
    private final Map<RequestRouter, List<Long>> routerSessions = ApricotCollectionFactor.hashMap();

    public void subscribe(RequestRouter router, Long session) {
        this.sessions.compute(session,
                              (key, value) -> {
                                  if (value == null) {
                                      value = ApricotCollectionFactor.arrayList();
                                  }
                                  value.add(router);
                                  return value;
                              }
        );
    }

    public void unsubscribe(RequestRouter router) {
        this.routerSessions.getOrDefault(
                    router,
                    Manipulate.cast(EMPTY_LIST)
            )
                           .forEach(
                                   session -> {
                                       this.sessions.getOrDefault(
                                                   session,
                                                   Manipulate.cast(EMPTY_LIST)
                                           )
                                                    .remove(router);
                                   }
                           );
    }

    public void unsubscribe(RequestRouter router, Long session) {
        this.routerSessions.getOrDefault(
                    router,
                    Manipulate.cast(EMPTY_LIST)
            )
                           .remove(session);

        this.sessions.getOrDefault(
                    session,
                    Manipulate.cast(EMPTY_LIST)
            )
                     .remove(router);
    }
}
