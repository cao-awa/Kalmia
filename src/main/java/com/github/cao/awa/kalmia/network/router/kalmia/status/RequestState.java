package com.github.cao.awa.kalmia.network.router.kalmia.status;

import com.google.common.collect.ImmutableSet;

import java.util.Set;

public enum RequestState {
    STATELESS, HELLO, AUTH, AUTHED;

    private static final Set<RequestState> ALL = new ImmutableSet.Builder<RequestState>().add(
                                                                                                 STATELESS,
                                                                                                 HELLO,
                                                                                                 AUTH,
                                                                                                 AUTHED
                                                                                         )
                                                                                         .build();

    public static Set<RequestState> all() {
        return ALL;
    }
}
