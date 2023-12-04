package com.github.cao.awa.kalmia.framework.reflection;

import com.github.cao.awa.apricot.annotations.auto.Auto;
import com.github.cao.awa.apricot.resource.loader.ResourceLoader;
import com.github.cao.awa.kalmia.framework.loader.JarSearchLoader;
import com.github.cao.awa.trtr.framework.accessor.method.MethodAccess;
import com.github.cao.awa.trtr.framework.exception.NoAutoAnnotationException;
import com.github.zhuaidadaya.rikaishinikui.handler.universal.entrust.EntrustEnvironment;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.reflections.Reflections;
import org.reflections.scanners.Scanners;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

public abstract class ReflectionFramework {
    private static final Logger LOGGER = LogManager.getLogger("ReflectionFramework");
    private static final Reflections REFLECTIONS = EntrustEnvironment.trys(() -> {
        File kalmiaJar = new File(URLDecoder.decode(
                ResourceLoader.class.getProtectionDomain()
                                    .getCodeSource()
                                    .getLocation()
                                    .getPath(),
                StandardCharsets.UTF_8
        ));
        return new Reflections(new ConfigurationBuilder().addUrls(JarSearchLoader.load(new File("mods")))
                                                         .addUrls(ClasspathHelper.forPackage(""))
                                                         .addUrls(kalmiaJar.toURI()
                                                                           .toURL())
                                                         .addScanners(Scanners.TypesAnnotated));
    });

    public Reflections reflection() {
        return REFLECTIONS;
    }

    public abstract void work();

    public boolean checkFields(String target, List<String> field) {
        if (field.size() > 0) {
            LOGGER.error("'{}' has missing required field(s): {}",
                         target,
                         field
            );
            return false;
        } else {
            LOGGER.debug("'{}' has passed checking required field(s)",
                         target
            );
            return true;
        }
    }

    public static <T> Constructor<T> ensureAccessible(Constructor<T> clazz) {
        if (clazz.canAccess(null)) {
            return clazz;
        }
        clazz.trySetAccessible();
        return clazz;
    }

    @NotNull
    public static Method ensureAccessible(Method clazz) {
        if (clazz.getAnnotation(Auto.class) != null) {
            return MethodAccess.ensureAccessible(clazz);
        }
        throw new NoAutoAnnotationException();
    }

    public static Field ensureAccessible(@NotNull Field field) {
        return ensureAccessible(field,
                                null
        );
    }

    public static Field ensureAccessible(@NotNull Field field, @Nullable Object obj) {
        if (field.canAccess(Modifier.isStatic(field.getModifiers()) ? null : obj)) {
            return field;
        }
        field.trySetAccessible();
        return field;
    }
}
