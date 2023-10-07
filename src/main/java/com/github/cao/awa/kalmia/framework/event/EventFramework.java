package com.github.cao.awa.kalmia.framework.event;

import com.github.cao.awa.apricot.annotation.auto.Auto;
import com.github.cao.awa.apricot.util.collection.ApricotCollectionFactor;
import com.github.cao.awa.kalmia.annotation.auto.event.AutoHandler;
import com.github.cao.awa.kalmia.annotation.plugin.PluginRegister;
import com.github.cao.awa.kalmia.env.KalmiaEnv;
import com.github.cao.awa.kalmia.event.Event;
import com.github.cao.awa.kalmia.event.handler.EventHandler;
import com.github.cao.awa.kalmia.event.handler.network.NetworkEventHandler;
import com.github.cao.awa.kalmia.event.network.NetworkEvent;
import com.github.cao.awa.kalmia.framework.AnnotationUtil;
import com.github.cao.awa.kalmia.framework.reflection.ReflectionFramework;
import com.github.cao.awa.kalmia.plugin.Plugin;
import com.github.cao.awa.modmdo.annotation.platform.Client;
import com.github.cao.awa.modmdo.annotation.platform.Server;
import com.github.zhuaidadaya.rikaishinikui.handler.universal.entrust.EntrustEnvironment;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Modifier;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Consumer;

public class EventFramework extends ReflectionFramework {
    private static final Logger LOGGER = LogManager.getLogger("EventFramework");
    private final Map<Class<? extends Event>, List<EventHandler<?>>> handlers = ApricotCollectionFactor.hashMap();
    private final Map<EventHandler<?>, String> handlerBelongs = ApricotCollectionFactor.hashMap();
    private final Map<Class<? extends EventHandler<?>>, Class<? extends Event>> targetedEventHandler = ApricotCollectionFactor.hashMap();

    public void work() {
        // Working stream...
        reflection().getTypesAnnotatedWith(AutoHandler.class)
                    .stream()
                    .filter(this :: match)
                    .map(this :: cast)
                    .forEach(this :: autoHandler);

        reflection().getTypesAnnotatedWith(Auto.class)
                    .stream()
                    .filter(this :: match)
                    .map(this :: cast)
                    .forEach(this :: build);
    }

    public boolean match(Class<?> clazz) {
        return EventHandler.class.isAssignableFrom(clazz);
    }

    public Class<? extends EventHandler<?>> cast(Class<?> clazz) {
        return EntrustEnvironment.cast(clazz);
    }

    public void build(Class<? extends EventHandler<?>> clazz) {
        if (Modifier.isInterface(clazz.getModifiers())) {
            return;
        }

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
                    EventHandler<?> handler = clazz.getConstructor()
                                                   .newInstance();

                    // Declare way to register.
                    AutoHandler autoAnnotation = clazz.getAnnotation(AutoHandler.class);

                    Consumer<Class<? extends Event>> adder = h -> this.handlers.compute(
                            h,
                            (event, handlers) -> {
                                if (handlers == null) {
                                    handlers = ApricotCollectionFactor.arrayList();
                                }
                                handlers.add(handler);
                                return handlers;
                            }
                    );

                    // Do potential coding problem tests.
                    Set<AutoHandler> annotations = AnnotationUtil.getAnnotations(handler.getClass(),
                                                                                 AutoHandler.class
                    );

                    if (autoAnnotation == null) {
                        // Do potential coding problem tests.
                        if (annotations.size() > 1) {
                            // Available declared target over than 1 means one handler matched to multi event.
                            // This behavior is not expected in current kalmia event framework.
                            LOGGER.error(
                                    "Class chains found the target over than 1 available declared, that wrongly, unable to register the '{}'",
                                    handler.getClass()
                                           .getName()
                            );

                            return;
                        }

                        // Auto register in undeclared.
                        LOGGER.info(
                                "Register auto event handler '{}' via plugin '{}'",
                                handler.getClass()
                                       .getName(),
                                pluginAnnotation.name()
                        );

                        for (Class<?> interfaceOf : (clazz.getInterfaces())) {
                            adder.accept(target(EntrustEnvironment.cast(interfaceOf)));
                        }
                    } else {
                        // Do potential coding problem tests.
                        if (annotations.size() == 2) {
                            annotations.remove(autoAnnotation);
                            LOGGER.warn(
                                    "Targeted event handler '{}' declared a target '{}', but its superclass expected another target '{}', this may be a wrong, please check it",
                                    handler.getClass()
                                           .getName(),
                                    autoAnnotation.value()
                                                  .getName(),
                                    annotations.toArray(new AutoHandler[0])[0].value()
                            );
                        } else if (annotations.size() > 2) {
                            // Available declared target over than 1 means one handler matched to multi event.
                            // This behavior is not expected in current kalmia event framework.
                            LOGGER.error(
                                    "Class chains found the target over than 2 available declared, that wrongly, unable to register the '{}'",
                                    handler.getClass()
                                           .getName()
                            );

                            return;
                        }

                        // Targeted  register in declared.
                        LOGGER.info(
                                "Register targeted event handler '{}' via plugin '{}'",
                                handler.getClass()
                                       .getName(),
                                pluginAnnotation.name()
                        );

                        adder.accept(autoAnnotation.value());
                    }

                    this.handlerBelongs.put(
                            handler,
                            pluginAnnotation.name()
                    );
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void registerHandler(EventHandler<?> handler) {
        AutoHandler autoAnnotation = AnnotationUtil.getAnnotation(handler.getClass(),
                                                                  AutoHandler.class
        );

        if (autoAnnotation == null) {
            throw new IllegalStateException("");
        }

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
    }

    public Class<? extends EventHandler<?>> autoHandler(Class<? extends EventHandler<?>> handler) {
        if (Modifier.isInterface(handler.getModifiers())) {
            AutoHandler autoHandler = handler.getAnnotation(AutoHandler.class);

            if (autoHandler == null) {
                return handler;
            }

            this.targetedEventHandler.put(handler,
                                          autoHandler.value()
            );
        }

        return handler;
    }

    public Class<? extends Event> target(Class<? extends EventHandler<?>> handlerType) {
        return this.targetedEventHandler.get(handlerType);
    }

    public void fireEvent(@NotNull Event event) {
        List<EventHandler<?>> handlers = this.handlers.get(event.getClass());

        if (missingHandler(
                handlers,
                event
        )) {
            return;
        }

        handlers.forEach(handler -> {
            if (
                // If plugin are disabled, then do not let it handle events.
                    plugin(handler).enabled()
            ) {
                handler.handle(EntrustEnvironment.cast(event));
            }
        });
    }

    public void fireEvent(@NotNull NetworkEvent<?> event) {
        List<EventHandler<?>> handlers = this.handlers.get(event.getClass());

        if (missingHandler(
                handlers,
                event
        )) {
            return;
        }

        for (EventHandler<?> handler : handlers) {
            if (
                // Network event can only handle by network event handler.
                    handler instanceof NetworkEventHandler<?, ?> networkHandler &&
                            // If plugin are disabled, then do not let it handle events.
                            plugin(networkHandler).enabled()
            ) {
//                EntrustEnvironment.trys(
//                        () ->
                networkHandler.handle(Objects.requireNonNull(EntrustEnvironment.cast(event)));
//                                ,
//                        ex -> BugTrace.trace(ex,
//                                             StringConcat.concat(
//                                                     "Event pipeline was happened exception by plugin '",
//                                                     this.handlerBelongs.get(networkHandler),
//                                                     "'"
//                                             )
//                        )
//                );
            }
        }
    }

    public boolean missingHandler(List<EventHandler<?>> handlers, Event event) {
        if (handlers == null) {
            LOGGER.warn(
                    "No handler(s) can process the happening event {}",
                    event.getClass()
                         .getName()
            );
            return true;
        }
        return false;
    }

    public Plugin plugin(EventHandler<?> handler) {
        return KalmiaEnv.pluginFramework.plugin(handler.getClass()
                                                       .getAnnotation(PluginRegister.class)
                                                       .name());
    }
}
