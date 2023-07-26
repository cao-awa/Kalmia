package com.github.cao.awa.kalmia.framework.event;

import com.github.cao.awa.apricot.annotation.auto.Auto;
import com.github.cao.awa.apricot.util.collection.ApricotCollectionFactor;
import com.github.cao.awa.kalmia.annotation.auto.event.AutoHandler;
import com.github.cao.awa.kalmia.annotation.plugin.PluginRegister;
import com.github.cao.awa.kalmia.bug.BugTrace;
import com.github.cao.awa.kalmia.env.KalmiaEnv;
import com.github.cao.awa.kalmia.event.Event;
import com.github.cao.awa.kalmia.event.handler.EventHandler;
import com.github.cao.awa.kalmia.event.handler.network.NetworkEventHandler;
import com.github.cao.awa.kalmia.event.network.NetworkEvent;
import com.github.cao.awa.kalmia.framework.reflection.ReflectionFramework;
import com.github.cao.awa.modmdo.annotation.platform.Client;
import com.github.cao.awa.modmdo.annotation.platform.Server;
import com.github.cao.awa.trtr.util.string.StringConcat;
import com.github.zhuaidadaya.rikaishinikui.handler.universal.entrust.EntrustEnvironment;
import com.google.common.collect.BiMap;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;

public class EventFramework extends ReflectionFramework {
    private static final Logger LOGGER = LogManager.getLogger("EventFramework");
    private final BiMap<Class<? extends Event>, List<EventHandler<?>>> handlers = ApricotCollectionFactor.hashBiMap();
    private final BiMap<EventHandler<?>, String> handlerBelongs = ApricotCollectionFactor.hashBiMap();

    public void work() {
        // Working stream...
        reflection().getTypesAnnotatedWith(Auto.class)
                    .stream()
                    .filter(this :: match)
                    .map(this :: cast)
                    .forEach(this :: build);
    }

    public boolean match(Class<?> clazz) {
        return clazz.isAnnotationPresent(AutoHandler.class) && EventHandler.class.isAssignableFrom(clazz);
    }

    public Class<? extends EventHandler<?>> cast(Class<?> clazz) {
        return EntrustEnvironment.cast(clazz);
    }

    public void build(Class<? extends EventHandler<?>> clazz) {
        boolean loadWhenServer = clazz.isAnnotationPresent(Server.class);
        boolean loadWhenClient = clazz.isAnnotationPresent(Client.class);

        boolean shouldLoad;

        // Always load plugin when simultaneously annotated @Server and @Client and when don't annotated.
        if ((loadWhenServer && loadWhenClient) || (! loadWhenServer && ! loadWhenClient)) {
            shouldLoad = true;
        } else {
            // Load by environment annotation.
            shouldLoad = KalmiaEnv.isServer ? loadWhenServer : loadWhenClient;
        }

        if (shouldLoad) {
            try {
                // Get plugin register annotation used to get plugin name to auto register.
                PluginRegister pluginAnnotation = clazz.getAnnotation(PluginRegister.class);

                if (pluginAnnotation != null) {
                    AutoHandler autoAnnotation = clazz.getAnnotation(AutoHandler.class);

                    EventHandler<?> handler = clazz.getConstructor()
                                                   .newInstance();

                    this.handlers.compute(
                            autoAnnotation.value(),
                            (event, handlers) -> {
                                if (handlers == null) {
                                    handlers = ApricotCollectionFactor.arrayList();
                                }
                                handlers.add(handler);
                                return handlers;
                            }
                    );

                    handlerBelongs.put(
                            handler,
                            pluginAnnotation.name()
                    );
                }
            } catch (Exception e) {

            }
        }
    }

    public void fireEvent(@NotNull Event event) {
        this.handlers.get(event.getClass())
                     .forEach(handler -> handler.handle(EntrustEnvironment.cast(event)));
    }

    public void fireEvent(@NotNull NetworkEvent<?> event) {
        this.handlers.get(event.getClass())
                     .forEach(handler -> {
                         if (handler instanceof NetworkEventHandler<?, ?> networkHandler) {
                             EntrustEnvironment.trys(
                                     () -> networkHandler.handle(Objects.requireNonNull(EntrustEnvironment.cast(event))),
                                     ex -> {
                                         try {
                                             System.out.println("????");
                                             BugTrace.trace(ex,
                                                            StringConcat.concat(
                                                                    "Event pipeline was happened exception by plugin '",
                                                                    this.handlerBelongs.get(networkHandler),
                                                                    "'"
                                                            )
                                             );
                                         } catch (Exception e) {
                                             e.printStackTrace();
                                         }
                                     }
                             );
                         }
                     });
    }
}
