package com.github.cao.awa.kalmia.network.router.status;

import com.google.common.collect.ImmutableSet;

import java.util.Set;

public enum RequestStatus {
    HELLO, AUTH, AUTHED, DISABLED;

    private static final Set<RequestStatus> ALL = new ImmutableSet.Builder<RequestStatus>().add(HELLO,
                                                                                                AUTH,
                                                                                                AUTHED,
                                                                                                DISABLED
                                                                                           )
                                                                                           .build();

    public static Set<RequestStatus> all() {
        return ALL;
    }
}
