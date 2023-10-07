package com.github.cao.awa.kalmia.network.router.meta;

import com.github.cao.awa.apricot.identifier.BytesRandomIdentifier;
import com.github.cao.awa.apricot.util.time.TimeUtil;
import com.github.cao.awa.kalmia.mathematic.Mathematics;

public record RouterMetadata(byte[] connectionId, long timestamp) {
    public static RouterMetadata create() {
        return new RouterMetadata(
                BytesRandomIdentifier.create(24),
                TimeUtil.nano()
        );
    }

    public String formatConnectionId() {
        return Mathematics.radix(
                this.connectionId,
                36
        );
    }
}
