package com.github.cao.awa.kalmia.framework.translation.event;

import com.github.cao.awa.apricot.annotations.auto.Auto;
import com.github.cao.awa.apricot.util.collection.ApricotCollectionFactor;
import com.github.cao.awa.catheter.Catheter;
import com.github.cao.awa.kalmia.annotations.auto.event.translation.TranslationEventTarget;
import com.github.cao.awa.kalmia.env.KalmiaEnv;
import com.github.cao.awa.kalmia.framework.reflection.ReflectionFramework;
import com.github.cao.awa.kalmia.translation.event.TranslationEvent;
import com.github.cao.awa.kalmia.translation.network.packet.TranslationPacket;
import com.github.cao.awa.kalmia.translation.network.router.TranslationRouter;
import com.github.cao.awa.sinuatum.manipulate.Manipulate;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Map;
import java.util.function.BiFunction;

public class TranslationEventFramework extends ReflectionFramework {
    private static final Logger LOGGER = LogManager.getLogger("TranslationEventFramework");
    private final Map<Class<? extends TranslationPacket>, BiFunction<TranslationRouter, TranslationPacket, TranslationEvent<?>>> events = ApricotCollectionFactor.hashMap();

    public void work() {
        // Working stream...
        Catheter.of(reflection().getTypesAnnotatedWith(Auto.class))
                .filter(this :: match)
                .vary(this :: cast)
                .each(this :: build);
    }

    public boolean match(Class<?> clazz) {
        return clazz.isAnnotationPresent(TranslationEventTarget.class) && TranslationPacket.class.isAssignableFrom(clazz);
    }

    public Class<? extends TranslationPacket> cast(Class<?> clazz) {
        return Manipulate.cast(clazz);
    }

    public void build(Class<? extends TranslationPacket> clazz) {
        TranslationEventTarget target = clazz.getAnnotation(TranslationEventTarget.class);
        try {
            registerNetworkEvent(clazz,
                                 (router, packet) -> Manipulate.supply(() -> target.value()
                                                                                   .getConstructor(TranslationRouter.class,
                                                                                                   clazz
                                                                                   )
                                                                                   .newInstance(router,
                                                                                                packet
                                                                                   )
                                                               )
                                                               .get()
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void registerNetworkEvent(Class<? extends TranslationPacket> packet, BiFunction<TranslationRouter, TranslationPacket, TranslationEvent<?>> creator) {
        this.events.put(packet,
                        creator
        );
    }

    public void fireEvent(TranslationRouter router, TranslationPacket packet) {
        BiFunction<TranslationRouter, TranslationPacket, TranslationEvent<?>> h = this.events.get(packet.getClass());
        if (h == null) {
            LOGGER.error("The packet '{}' has failed match the event handler, did annotation @TranslationEventTarget are missing in packet?",
                         packet.getClass()
                               .getName()
            );
        } else {
            KalmiaEnv.EVENT_FRAMEWORK.fireEvent(h
                                                        .apply(router,
                                                               packet
                                                        ));
        }
    }
}
