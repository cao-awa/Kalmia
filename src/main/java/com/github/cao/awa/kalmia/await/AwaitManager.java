package com.github.cao.awa.kalmia.await;

import com.github.cao.awa.apricot.util.collection.ApricotCollectionFactor;
import com.github.cao.awa.apricot.util.time.TimeUtil;
import com.github.cao.awa.kalmia.mathematic.Mathematics;
import com.github.zhuaidadaya.rikaishinikui.handler.universal.entrust.EntrustEnvironment;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

public class AwaitManager {
    private final ScheduledExecutorService executor = new ScheduledThreadPoolExecutor(1);
    private final Map<String, Long> awaiting = ApricotCollectionFactor.hashMap();

    public AwaitManager() {
        this.executor.scheduleWithFixedDelay(
                () -> {
                    List<String> needRemoves = ApricotCollectionFactor.arrayList();

                    this.awaiting.forEach((identity, timestamp) -> {
                        if (TimeUtil.processMillion(timestamp) > 15000) {
                            needRemoves.add(identity);
                        }
                    });

                    for (String needRemove : needRemoves) {
                        this.awaiting.remove(needRemove);
                    }
                },
                0,
                15,
                TimeUnit.SECONDS
        );
    }

    public void await(byte[] awaitIdentity, Runnable runnable, Runnable trigger) throws InterruptedException {
        String radix = Mathematics.radix(awaitIdentity,
                                         36
        );

        this.awaiting.put(radix,
                          TimeUtil.millions()
        );

        trigger.run();

        while (this.awaiting.containsKey(radix)) {
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
        T getNow = EntrustEnvironment.trys(supplier :: get);
        if (getNow == null) {
            await(awaitIdentity,
                  trigger
            );
            return supplier.get();
        }
        return getNow;
    }

    public void notice(byte[] awaitIdentity) {
        this.awaiting.remove(Mathematics.radix(awaitIdentity,
                                               36
        ));
    }
}
