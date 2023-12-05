package com.github.cao.awa.kalmia.await;

import com.github.cao.awa.apricot.util.collection.ApricotCollectionFactor;

import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public class AwaitManager {
    private final List<byte[]> awaiting = ApricotCollectionFactor.timedList(30000);
    private final Map<byte[], Runnable> awaitingAction = ApricotCollectionFactor.hashMap();

    public void await(byte[] awaitIdentity, Runnable runnable, Runnable trigger) throws InterruptedException {
        this.awaitingAction.put(awaitIdentity,
                                runnable
        );

        this.awaiting.add(awaitIdentity);

        trigger.run();

        while (this.awaiting.contains(awaitIdentity)) {
            Thread.sleep(10);
        }

        runnable.run();
    }

    public void await(byte[] awaitIdentity, Runnable trigger) throws InterruptedException {
        await(awaitIdentity,
              () -> {
              },
              trigger
        );
    }

    public <T> T awaitGet(byte[] awaitIdentity, Supplier<T> supplier, Runnable trigger) throws InterruptedException {
        T getNow = supplier.get();
        if (getNow == null) {
            await(awaitIdentity,
                  trigger
            );
            return supplier.get();
        }
        return getNow;
    }

    public void notice(byte[] awaitIdentity) {
        this.awaiting.remove(awaitIdentity);
    }
}
