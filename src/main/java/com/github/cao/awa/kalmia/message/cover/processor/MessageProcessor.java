package com.github.cao.awa.kalmia.message.cover.processor;

import java.util.UUID;

public abstract class MessageProcessor {
    public abstract byte[] process(byte[] bytes, long sender);

    public abstract UUID id();
}
