package com.github.cao.awa.kalmia.framework.network.event;

import com.github.cao.awa.apricot.annotations.auto.Auto;
import com.github.cao.awa.apricot.util.collection.ApricotCollectionFactor;
import com.github.cao.awa.catheter.Catheter;
import com.github.cao.awa.kalmia.annotations.auto.event.network.NetworkEventTarget;
import com.github.cao.awa.kalmia.env.KalmiaEnv;
import com.github.cao.awa.kalmia.event.kalmiagram.network.NetworkEvent;
import com.github.cao.awa.kalmia.framework.reflection.ReflectionFramework;
import com.github.cao.awa.kalmia.network.handler.PacketHandler;
import com.github.cao.awa.kalmia.network.packet.Packet;
import com.github.cao.awa.kalmia.network.router.kalmia.RequestRouter;
import com.github.cao.awa.sinuatum.manipulate.Manipulate;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Map;
import java.util.function.BiFunction;

public class NetworkEventFramework extends ReflectionFramework {
    private static final Logger LOGGER = LogManager.getLogger("NetworkEventFramework");
    private final Map<Class<? extends Packet<?>>, BiFunction<RequestRouter, Packet<?>, NetworkEvent<?>>> events = ApricotCollectionFactor.hashMap();

    public void work() {
        // Working stream...
        Catheter.of(reflection().getTypesAnnotatedWith(Auto.class))
                .filter(this :: match)
                .vary(this :: cast)
                .each(this :: build);
    }

    public boolean match(Class<?> clazz) {
        return clazz.isAnnotationPresent(NetworkEventTarget.class) && Packet.class.isAssignableFrom(clazz);
    }

    public Class<? extends Packet<?>> cast(Class<?> clazz) {
        return Manipulate.cast(clazz);
    }

    public void build(Class<? extends Packet<?>> clazz) {
        NetworkEventTarget target = clazz.getAnnotation(NetworkEventTarget.class);
        registerNetworkEvent(clazz,
                             (router, packet) -> Manipulate.supply(() -> target.value()
                                                                               .getConstructor(RequestRouter.class,
                                                                                               clazz
                                                                               )
                                                                               .newInstance(router,
                                                                                            packet
                                                                               )
                                                           )
                                                           .get()
        );
    }

    public void registerNetworkEvent(Class<? extends Packet<?>> packet, BiFunction<RequestRouter, Packet<?>, NetworkEvent<?>> creator) {
        this.events.put(packet,
                        creator
        );
    }

    public void fireEvent(RequestRouter router, PacketHandler<?> handler, Packet<?> packet) {
        BiFunction<RequestRouter, Packet<?>, NetworkEvent<?>> h = this.events.get(packet.getClass());
        if (h == null) {
            LOGGER.error("The packet '{}' has failed match the event handler, did annotation @NetworkEventTarget are missing in packet?",
                         packet.getClass()
                               .getName()
            );
        } else {
            KalmiaEnv.EVENT_FRAMEWORK.fireEvent(h.apply(router,
                                                        packet
            ));
        }
    }
}
