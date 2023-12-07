package com.github.cao.awa.kalmia.network.router.kalmia.meta;

import com.github.cao.awa.apricot.identifier.BytesRandomIdentifier;
import com.github.cao.awa.apricot.util.time.TimeUtil;
import com.github.cao.awa.kalmia.mathematic.Mathematics;

public record RequestRouterMetadata(byte[] connectionId, long timestamp) {
    public static RequestRouterMetadata create() {
        return new RequestRouterMetadata(
                BytesRandomIdentifier.create(24),
                TimeUtil.millions()
        );
    }

    public String formatConnectionId() {
        return "#" + Mathematics.radix(
                this.connectionId,
                36
        );
    }
}
