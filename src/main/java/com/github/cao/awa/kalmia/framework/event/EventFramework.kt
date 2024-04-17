package com.github.cao.awa.kalmia.framework.event

import com.github.cao.awa.apricot.annotations.auto.Auto
import com.github.cao.awa.apricot.thread.pool.ExecutorFactor
import com.github.cao.awa.apricot.util.collection.ApricotCollectionFactor
import com.github.cao.awa.kalmia.annotations.auto.event.AutoHandler
import com.github.cao.awa.kalmia.annotations.plugin.PluginRegister
import com.github.cao.awa.kalmia.env.KalmiaEnv
import com.github.cao.awa.kalmia.event.Event
import com.github.cao.awa.kalmia.event.kalmiagram.handler.EventHandler
import com.github.cao.awa.kalmia.event.kalmiagram.handler.network.NetworkEventHandler
import com.github.cao.awa.kalmia.event.kalmiagram.network.NetworkEvent
import com.github.cao.awa.kalmia.framework.AnnotationUtil
import com.github.cao.awa.kalmia.framework.reflection.ReflectionFramework
import com.github.cao.awa.kalmia.plugin.Plugin
import com.github.cao.awa.kalmia.threading.ThreadingUtil
import com.github.cao.awa.modmdo.annotation.platform.Client
import com.github.cao.awa.modmdo.annotation.platform.Server
import com.github.zhuaidadaya.rikaishinikui.handler.universal.entrust.EntrustEnvironment
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger

import java.lang.reflect.Modifier
import java.util.concurrent.ExecutorService
import java.util.function.BiFunction
import java.util.function.Consumer

class EventFramework : ReflectionFramework() {
    companion object {
        private val LOGGER: Logger = LogManager.getLogger("EventFramework")
    }

    private val executor: ExecutorService = ExecutorFactor.intensiveCpu()
    private val handlers: MutableMap<Class<out Event>, List<EventHandler<*>>> = ApricotCollectionFactor.hashMap()
    private val handlerBelongs: MutableMap<EventHandler<*>, String> = ApricotCollectionFactor.hashMap()
    private val targetedEventHandlers: MutableMap<Class<out EventHandler<*>>, Class<out Event>> =
        ApricotCollectionFactor.hashMap()
    private val registeredHandlers: MutableList<Class<out EventHandler<*>>> = ApricotCollectionFactor.arrayList()

    override fun work() {
        // Working stream...
        reflection().getTypesAnnotatedWith(AutoHandler::class.java).stream().filter(this::match).map(this::cast)
            .forEach(this::autoHandler)

        reflection().getTypesAnnotatedWith(Auto::class.java).stream().filter(this::match).map(this::cast)
            .forEach(this::build)
    }

    fun match(clazz: Class<*>): Boolean {
        return EventHandler::class.java.isAssignableFrom(clazz)
    }

    fun cast(clazz: Class<*>): Class<out EventHandler<*>> {
        return EntrustEnvironment.cast(clazz)!!
    }

    fun build(clazz: Class<out EventHandler<*>>) {
        if (Modifier.isInterface(clazz.modifiers)) {
            return
        }

        // Get plugin register annotation used to get plugin name to auto register.
        val pluginAnnotation: PluginRegister = clazz.getAnnotation(PluginRegister::class.java) ?: return

        val shouldLoad: Boolean = if (KalmiaEnv.pluginFramework.plugin(pluginAnnotation.name).forceRegister()) {
            true
        } else {
            val loadWhenServer: Boolean = clazz.isAnnotationPresent(Server::class.java)
            val loadWhenClient: Boolean = clazz.isAnnotationPresent(Client::class.java)

            // Always load plugin when simultaneously annotated @Server and @Client and when don't annotate.
            if ((loadWhenServer && loadWhenClient) || (!loadWhenServer && !loadWhenClient)) {
                true
            } else {
                // Load by environment annotation.
                if (KalmiaEnv.serverSideLoading) loadWhenServer else loadWhenClient
            }
        }

        if (shouldLoad) {
            try {
                if (!KalmiaEnv.pluginFramework.plugin(pluginAnnotation.name).enabled()) {
                    return
                }

                val handler: EventHandler<*> = clazz.getConstructor().newInstance()

                // Declare way to register.
                val autoAnnotation: AutoHandler? = clazz.getAnnotation(AutoHandler::class.java)

                val adder: Consumer<Class<out Event>> =
                    Consumer { event -> handlers.compute(event, computeHandler(handler)) }

                // Do potential coding problem tests.
                val annotations: MutableSet<AutoHandler> =
                    AnnotationUtil.getAnnotations(handler.javaClass, AutoHandler::class.java)!!
                if (autoAnnotation == null) {
                    // Do potential coding problem tests.
                    if (annotations.size > 1) {
                        // Available declared target over than 1 means one handler matched to multi event.
                        // This behavior is not expected in current kalmia event framework.
                        LOGGER.error(
                            "Class chains found the target over than 1 available declared, that wrongly, unable to register the '{}'",
                            handler.javaClass.name
                        )

                        return
                    }

                    for (interfaceOf in (clazz.interfaces)) {
                        adder.accept(target(EntrustEnvironment.cast<Class<out EventHandler<*>>>(interfaceOf)!!)!!)
                    }

                    // Auto register in undeclared.
                    LOGGER.info(
                        "Registered auto event handler '{}' via plugin '{}'",
                        handler.javaClass.name,
                        pluginAnnotation.name
                    )
                } else {
                    // Do potential coding problem tests.
                    if (annotations.size == 2) {
                        annotations.remove(autoAnnotation)
                        LOGGER.warn(
                            "Targeted event handler '{}' declared a target '{}', but its superclass expected another target '{}', this may be a wrong, please check it",
                            handler.javaClass.name,
                            autoAnnotation.value.java.name,
                            annotations.toTypedArray<AutoHandler?>()[0]?.value
                        )
                    } else if (annotations.size > 2) {
                        // Available declared target over than 1 means one handler matched to multi event.
                        // This behavior is not expected in current kalmia event framework.
                        LOGGER.error(
                            "Class chains found the target over than 2 available declared, that wrongly, unable to register the '{}'",
                            handler.javaClass.name
                        )

                        return
                    }

                    adder.accept(autoAnnotation.value.java)

                    // Targeted  register in declared.
                    LOGGER.info(
                        "Registered targeted event handler '{}' via plugin '{}'",
                        handler.javaClass.name,
                        pluginAnnotation.name
                    )
                }

                this.handlerBelongs[handler] = pluginAnnotation.name
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun registerHandler(handler: EventHandler<*>, plugin: Plugin) {
        val autoAnnotation: AutoHandler = AnnotationUtil.getAnnotation(handler.javaClass, AutoHandler::class.java)

        handlers.compute(autoAnnotation.value.java, computeHandler(handler))

        handlerBelongs[handler] = KalmiaEnv.pluginFramework.name(plugin)
        LOGGER.info(
            "Registered manual event handler '${handler.javaClass.name}' via plugin '${
                KalmiaEnv.pluginFramework.name(
                    plugin
                )
            }'",
        )
    }

    fun optionalRegisterHandler(handler: EventHandler<*>, plugin: Plugin) {
        if (!registeredHandlers.contains(handler.javaClass)) {
            registerHandler(handler, plugin)
        }
    }

    fun computeHandler(handler: EventHandler<*>): BiFunction<in Class<out Event>, in List<EventHandler<*>>?, out List<EventHandler<*>>> {
        return BiFunction { _, handlers: List<EventHandler<*>>? ->
            val handlers: MutableList<EventHandler<*>> =
                handlers?.toMutableList() ?: ApricotCollectionFactor.arrayList()

            val handlerType: Class<out EventHandler<*>> = cast(handler.javaClass)

            if (registeredHandlers.contains(handlerType)) {
                LOGGER.warn(
                    "Handler '{}' already registered, this action is repeated, may cause event be handled twice or more times",
                    handlerType
                )
            }

            handlers.add(handler)
            registeredHandlers.add(handlerType)
            handlers
        }
    }

    fun autoHandler(handler: Class<out EventHandler<*>>?): Class<out EventHandler<*>> {
        if (handler == null) {
            throw RuntimeException()
        }
        if (Modifier.isInterface(handler.modifiers)) {
            val autoHandler: AutoHandler = handler.getAnnotation(AutoHandler::class.java)

            this.targetedEventHandlers[handler] = autoHandler.value.java
        }

        return handler
    }

    fun target(handlerType: Class<out EventHandler<*>>): Class<out Event>? {
        return this.targetedEventHandlers[handlerType]
    }

    fun fireEvent(event: Event) {
        val handlers: List<EventHandler<*>> = this.handlers[event.javaClass]!!

        if (missingHandler(handlers, event)) {
            return
        }

        handlers.forEach { handler ->
            if (
            // If plugin are disabled, then do not let it handle events.
                plugin(handler).enabled()) {
                handleEvent(handler, event)
            }
        }
    }

    fun fireEvent(event: NetworkEvent<*>) {
        val handlers: List<EventHandler<*>> = handlers[event.javaClass]!!

        if (missingHandler(handlers, event)) {
            return
        }

        for (handler in handlers) {
            if (
            // Network event can only handle by network event handler.
                handler is NetworkEventHandler<*, *> &&
                // If plugin are disabled, then do not let it handle events.
                plugin(handler).enabled()
            ) {
                handleEvent(handler, event)
            }
        }
    }

    fun handleEvent(handler: EventHandler<*>, event: Event) {
        val handleAction: () -> Unit = {
            handler.handle(EntrustEnvironment.cast(event))
        }

        if (ThreadingUtil.forceMainThread(handler.javaClass)) {
            handleAction.run {}
        } else {
            this.executor.execute(handleAction)
        }
    }

    fun missingHandler(handlers: List<EventHandler<*>>?, event: Event): Boolean {
        if (handlers == null) {
            LOGGER.warn("No handler(s) can process the happening event '{}'", event.javaClass.name)
            return true
        }
        return false
    }

    fun plugin(handler: EventHandler<*>): Plugin {
        return KalmiaEnv.pluginFramework.plugin(
            handlerBelongs[handler] ?: handler.javaClass.getAnnotation(PluginRegister::class.java).name
        )
    }
}
