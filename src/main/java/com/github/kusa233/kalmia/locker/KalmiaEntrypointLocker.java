package com.github.kusa233.kalmia.locker;

import java.util.concurrent.LinkedBlockingQueue;

public class KalmiaEntrypointLocker {
    private final LinkedBlockingQueue<Boolean> QUEUE = new LinkedBlockingQueue<>();

    public boolean waitFor() {
        return Boolean.TRUE.equals(QUEUE.poll());
    }

    public void offer() {
        QUEUE.offer(true);
    }
}
