package com.github.cao.awa.kalmia.framework.plugin;

import com.github.cao.awa.apricot.annotation.auto.Auto;
import com.github.cao.awa.apricot.annotation.auto.AutoPlugin;
import com.github.cao.awa.apricot.util.collection.ApricotCollectionFactor;
import com.github.cao.awa.kalmia.env.KalmiaEnv;
import com.github.cao.awa.kalmia.framework.reflection.ReflectionFramework;
import com.github.cao.awa.kalmia.plugin.Plugin;
import com.github.cao.awa.kalmia.reflection.field.FieldGet;
import com.github.cao.awa.modmdo.annotation.platform.Client;
import com.github.cao.awa.modmdo.annotation.platform.Server;
import com.github.zhuaidadaya.rikaishinikui.handler.universal.entrust.EntrustEnvironment;
import com.google.common.collect.BiMap;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.UUID;

public class PluginFramework extends ReflectionFramework {
    private static final Logger LOGGER = LogManager.getLogger("PluginFramework");
    private final BiMap<Plugin, UUID> plugins = ApricotCollectionFactor.hashBiMap();
    private final BiMap<String, Plugin> pluginsName = ApricotCollectionFactor.hashBiMap();

    public void work() {
        // Working stream...
        reflection().getTypesAnnotatedWith(Auto.class)
                    .stream()
                    .filter(this :: match)
                    .map(this :: cast)
                    .forEach(this :: build);
    }

    public boolean match(Class<?> clazz) {
        return clazz.isAnnotationPresent(AutoPlugin.class) && Plugin.class.isAssignableFrom(clazz);
    }

    public Class<? extends Plugin> cast(Class<?> clazz) {
        return EntrustEnvironment.cast(clazz);
    }

    public void build(Class<? extends Plugin> clazz) {
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
            // Get plugin annotation used to get name and uuid.
            AutoPlugin autoAnnotation = clazz.getAnnotation(AutoPlugin.class);

            UUID uuid = FieldGet.create(
                                        // Can use 'UUID' or 'ID' field name for automatic loading.
                                        clazz,
                                        "UUID"
                                )
                                .or("ID")
                                // If don't use field loading, will get uuid by annotation.
                                .or(p -> UUID.fromString(autoAnnotation.uuid()))
                                .get();

            try {
                // Create and register.
                Plugin plugin = clazz.getConstructor()
                                     .newInstance();

                LOGGER.info("Register plugin '{}' ({})",
                            autoAnnotation.name(),
                            uuid
                );

                this.plugins.put(plugin,
                                 uuid
                );
                this.pluginsName.put(autoAnnotation.name(),
                                     plugin
                );
            } catch (Exception e) {
                LOGGER.warn("Failed load plugin: {} ({})",
                            autoAnnotation.name(),
                            uuid
                );
            }
        }
    }

    public UUID uuid(Plugin plugin) {
        return this.plugins.get(plugin);
    }

    public String name(Plugin plugin) {
        return this.pluginsName.inverse()
                               .get(plugin);
    }

    public Plugin plugin(String name) {
        return this.pluginsName.get(name);
    }

    public Plugin plugin(UUID uuid) {
        return this.plugins.inverse()
                           .get(uuid);
    }
}