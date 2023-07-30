package com.github.cao.awa.kalmia.framework.network.event;

import com.github.cao.awa.apricot.annotation.auto.Auto;
import com.github.cao.awa.apricot.util.collection.ApricotCollectionFactor;
import com.github.cao.awa.kalmia.annotation.auto.event.NetworkEventTarget;
import com.github.cao.awa.kalmia.env.KalmiaEnv;
import com.github.cao.awa.kalmia.event.network.NetworkEvent;
import com.github.cao.awa.kalmia.framework.reflection.ReflectionFramework;
import com.github.cao.awa.kalmia.network.handler.PacketHandler;
import com.github.cao.awa.kalmia.network.packet.Packet;
import com.github.cao.awa.kalmia.network.router.RequestRouter;
import com.github.zhuaidadaya.rikaishinikui.handler.universal.entrust.EntrustEnvironment;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Map;
import java.util.function.BiFunction;

public class NetworkEventFramework extends ReflectionFramework {
    private static final Logger LOGGER = LogManager.getLogger("NetworkEventFramework");
    private final Map<Class<? extends Packet<?>>, BiFunction<RequestRouter, Packet<?>, NetworkEvent<?>>> networkEvents = ApricotCollectionFactor.hashMap();

    public void work() {
        // Working stream...
        reflection().getTypesAnnotatedWith(Auto.class)
                    .stream()
                    .filter(this :: match)
                    .map(this :: cast)
                    .forEach(this :: build);
    }

    public boolean match(Class<?> clazz) {
        return clazz.isAnnotationPresent(NetworkEventTarget.class) && Packet.class.isAssignableFrom(clazz);
    }

    public Class<? extends Packet<?>> cast(Class<?> clazz) {
        return EntrustEnvironment.cast(clazz);
    }

    public void build(Class<? extends Packet<?>> clazz) {
        NetworkEventTarget target = clazz.getAnnotation(NetworkEventTarget.class);
        registerNetworkEvent(clazz,
                             (router, packet) -> EntrustEnvironment.trys(() -> target.value()
                                                                                     .getConstructor(RequestRouter.class,
                                                                                                     clazz
                                                                                     )
                                                                                     .newInstance(router,
                                                                                                  packet
                                                                                     ))
        );
    }

    public void registerNetworkEvent(Class<? extends Packet<?>> packet, BiFunction<RequestRouter, Packet<?>, NetworkEvent<?>> creator) {
        this.networkEvents.put(packet,
                               creator
        );
    }

    public void fireEvent(RequestRouter router, PacketHandler<?> handler, Packet<?> packet) {
        KalmiaEnv.eventFramework.fireEvent(this.networkEvents.get(packet.getClass())
                                                             .apply(router,
                                                                    packet
                                                             ));
    }
}
