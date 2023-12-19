package com.github.cao.awa.kalmia.message.cover.processor;

import com.github.cao.awa.kalmia.identity.LongAndExtraIdentity;

import java.util.UUID;

public abstract class MessageProcessor {
    public abstract byte[] process(byte[] bytes, LongAndExtraIdentity sender);

    public abstract UUID id();
}
