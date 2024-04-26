package com.github.cao.awa.kalmia.framework.reflection;

import com.github.cao.awa.apricot.resource.loader.ResourceLoader;
import com.github.cao.awa.apricot.util.collection.ApricotCollectionFactor;
import com.github.cao.awa.kalmia.framework.loader.JarSearchLoader;
import com.github.cao.awa.trtr.framework.accessor.method.MethodAccess;
import com.github.zhuaidadaya.rikaishinikui.handler.universal.entrust.EntrustEnvironment;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.reflections.Reflections;
import org.reflections.scanners.Scanners;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;

import java.io.File;
import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;

public abstract class ReflectionFramework {
    private static final Logger LOGGER = LogManager.getLogger("ReflectionFramework");
    private static final Field MODIFIER_FILED = EntrustEnvironment.trys(() -> {
        Method getDeclaredFields0 = Class.class.getDeclaredMethod("getDeclaredFields0",
                                                                  boolean.class
        );
        getDeclaredFields0.setAccessible(true);
        Field[] fields = (Field[]) getDeclaredFields0.invoke(Field.class,
                                                             false
        );
        Field modifierField = null;
        for (Field f : fields) {
            if ("modifiers".equals(f.getName())) {
                modifierField = f;
                break;
            }
        }
        if (modifierField == null) {
            return null;
        }
        modifierField.setAccessible(true);
        return modifierField;
    });
    private static final Map<Class<?>, Map<Class<?>, Function<Object, Object>>> SPECIALLY_CAST = EntrustEnvironment.operation(ApricotCollectionFactor.hashMap(),
                                                                                                                              map -> {
                                                                                                                                  Map<Class<?>, Function<Integer, Object>> integerCasts = ApricotCollectionFactor.hashMap();
                                                                                                                                  integerCasts.put(Long.class,
                                                                                                                                                   i -> i
                                                                                                                                  );
                                                                                                                                  map.put(Integer.class,
                                                                                                                                          EntrustEnvironment.cast(integerCasts)
                                                                                                                                  );

                                                                                                                                  Map<Class<?>, Function<Long, Object>> longCasts = ApricotCollectionFactor.hashMap();
                                                                                                                                  longCasts.put(Integer.class,
                                                                                                                                                Long :: intValue
                                                                                                                                  );
                                                                                                                                  map.put(Long.class,
                                                                                                                                          EntrustEnvironment.cast(longCasts)
                                                                                                                                  );
                                                                                                                              }
    );
    private static final Reflections REFLECTIONS = EntrustEnvironment.trys(() -> {
        File kalmiaJar = new File(URLDecoder.decode(
                ResourceLoader.class.getProtectionDomain()
                                    .getCodeSource()
                                    .getLocation()
                                    .getPath(),
                StandardCharsets.UTF_8
        ));
        return new Reflections(new ConfigurationBuilder().addUrls(JarSearchLoader.load(new File("plugins")))
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
        if (! field.isEmpty()) {
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
    public static Method ensureAccessible(Object object, Method clazz) {
        return MethodAccess.ensureAccessible(object,
                                             clazz
        );
    }

    public static Field ensureAccessible(@NotNull Field field) {
        field.trySetAccessible();
        if (Modifier.isFinal(field.getModifiers())) {
            try {
                assert MODIFIER_FILED != null;
                MODIFIER_FILED.setInt(field,
                                      field.getModifiers() & ~ Modifier.FINAL
                );
            } catch (Throwable ignored) {

            }
        }
        return field;
    }

    public static Field fetchField(Class<?> clazz, @NotNull String key) {
        if (clazz == null) {
            return null;
        }
        return EntrustEnvironment.trys(() -> ensureAccessible(clazz.getDeclaredField(key)),
                                       e -> {
                                           return fetchField(clazz.getSuperclass(),
                                                             key
                                           );
                                       }
        );
    }

    public static Field fetchField(@NotNull Object object, @NotNull String key) {
        return EntrustEnvironment.trys(() -> ensureAccessible(object.getClass()
                                                                    .getDeclaredField(key)),
                                       e -> {
                                           return fetchField(object.getClass()
                                                                   .getSuperclass(),
                                                             key
                                           );
                                       }
        );
    }

    public static Field fetchField(@NotNull Field field) {
        return EntrustEnvironment.trys(() -> ensureAccessible(field));
    }

    public static Method fetchMethod(Object object, Class<?> clazz, @NotNull String name, Class<?>... args) {
        if (clazz == null) {
            return null;
        }
        return EntrustEnvironment.trys(() -> ensureAccessible(object,
                                                              clazz.getDeclaredMethod(name,
                                                                                      args
                                                              )
                                       ),
                                       e -> {
                                           return fetchMethod(object,
                                                              clazz.getSuperclass(),
                                                              name,
                                                              args
                                           );
                                       }
        );
    }

    public static Method fetchMethod(@NotNull Object object, @NotNull String name, Class<?>... args) {
        return fetchMethod(object,
                           object.getClass(),
                           name,
                           args
        );
    }

    public static Type[] getGenericType(@NotNull ParameterizedType parameterized) {
        return Arrays.stream(
                             parameterized.getActualTypeArguments()
                     )
                     .toArray(Type[] :: new);
    }

    public static Class<?> toClass(Type type) {
        return (Class<?>) type;
    }

    public static void checkType(Class<?> target, Object object, Consumer<Object> consumer) {
        if (object == null) {
            return;
        }
        if (target != object.getClass()) {
            Object casted = EntrustEnvironment.trys(() -> SPECIALLY_CAST.get(object.getClass())
                                                                        .get(target)
                                                                        .apply(object));
            if (casted == null) {
                throw new ClassCastException("The parameter has specified '" + target + "' but got '" + object.getClass() + "'");
            }
            consumer.accept(casted);
        }
        consumer.accept(object);
    }

    public static <T> T checkOrDiscard(Class<?> target, T object) {
        if (object == null || target != object.getClass()) {
            return null;
        }
        return object;
    }

    public static <T> T checkOrDiscard(Class<?> target, T object, Class<?> except) {
        if ((object == null || (target != object.getClass() && object.getClass() != except))) {
            return null;
        }
        return object;
    }

    public static Type getArgType(Field field) {
        return getArgType(field.getGenericType());
    }

    public static Type getArgType(Type type) {
        if (type instanceof ParameterizedType parameterized) {
            return getGenericType(parameterized)[0];
        }
        return null;
    }

    public static <A extends Annotation> A getAnnotation(Object object, Class<A> annotation) {
        return object.getClass()
                     .getAnnotation(annotation);
    }

    public static Field[] getFields(Object object) {
        return object.getClass()
                     .getDeclaredFields();
    }

    public static Field[] getFields(Class<?> clazz) {
        return clazz.getDeclaredFields();
    }

    public static boolean hasField(Object object, String name) {
        return EntrustEnvironment.trys(() -> object.getClass()
                                                   .getDeclaredField(name)) != null;
    }
}
