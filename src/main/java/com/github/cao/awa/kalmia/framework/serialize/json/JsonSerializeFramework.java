package com.github.cao.awa.kalmia.framework.serialize.json;

import com.alibaba.fastjson2.JSONObject;
import com.github.cao.awa.apricot.annotation.auto.Auto;
import com.github.cao.awa.apricot.util.collection.ApricotCollectionFactor;
import com.github.cao.awa.kalmia.annotation.auto.network.unsolve.AutoData;
import com.github.cao.awa.kalmia.annotation.auto.serializer.AutoJsonSerializer;
import com.github.cao.awa.kalmia.framework.reflection.ReflectionFramework;
import com.github.cao.awa.kalmia.network.packet.Packet;
import com.github.zhuaidadaya.rikaishinikui.handler.universal.entrust.EntrustEnvironment;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Field;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class JsonSerializeFramework extends ReflectionFramework {
    private static final Logger LOGGER = LogManager.getLogger("JsonSerializerFramework");
    private final Map<Class<? extends JsonSerializer<?>>, Class<?>[]> serializerToTarget = ApricotCollectionFactor.hashMap();
    private final Map<Class<?>, JsonSerializer<?>> targetToSerializer = ApricotCollectionFactor.hashMap();

    @Override
    public void work() {
        // Working stream...
        reflection().getTypesAnnotatedWith(Auto.class)
                    .stream()
                    .filter(this :: match)
                    .map(this :: cast)
                    .forEach(this :: build);
    }

    public boolean match(Class<?> clazz) {
        return clazz.isAnnotationPresent(AutoJsonSerializer.class) && JsonSerializer.class.isAssignableFrom(clazz);
    }

    public Class<? extends JsonSerializer<?>> cast(Class<?> clazz) {
        return EntrustEnvironment.cast(clazz);
    }

    public void build(Class<? extends JsonSerializer<?>> type) {
        try {
            JsonSerializer<?> serializer = type.getConstructor()
                                               .newInstance();
            AutoJsonSerializer annotation = type.getAnnotation(AutoJsonSerializer.class);
            Class<?>[] target = annotation.target();

            this.serializerToTarget.put(type,
                                        target
            );

            LOGGER.info("Register auto json serializer for type(s) {}: {}",
                        target,
                        serializer.getClass()
                                  .getName()
            );
            registerSerializer(serializer,
                               serializer.target()
            );
        } catch (Exception e) {
            LOGGER.error(e);
        }
    }

    public void registerSerializer(JsonSerializer<?> serializer, @Nullable Class<?>... matchType) {
        if (matchType == null) {
            return;
        }

        List<String> targetRegistered = ApricotCollectionFactor.arrayList();

        for (Class<?> t : matchType) {
            if (t == null) {
                return;
            }

            JsonSerializer<?> current = this.targetToSerializer.get(t);

            if (current != null) {
                LOGGER.warn("Failed register the json serializer '{}' because this type '{}' has been used by '{}'",
                            serializer.getClass()
                                      .getName(),
                            t.getName(),
                            current.getClass()
                                   .getName()
                );
                continue;
            }

            this.targetToSerializer.put(t,
                                        serializer
            );

            targetRegistered.add(t.getName());
        }

        LOGGER.info("Json serializer {} registered, targeted to {}",
                    serializer.getClass()
                              .getName(),
                    targetRegistered
        );
    }

    public Class<?>[] target(JsonSerializer<?> serializer) {
        return typeTarget(EntrustEnvironment.cast(serializer.getClass()));
    }

    public Class<?>[] typeTarget(Class<? extends JsonSerializer<?>> type) {
        return this.serializerToTarget.get(type);
    }

    private LinkedList<Field> autoFields(Object object) throws NoSuchFieldException {
        Class<Packet<?>> clazz = EntrustEnvironment.cast(object.getClass());
        assert clazz != null;
        LinkedList<Field> fields = ApricotCollectionFactor.linkedList();
        for (Field e : clazz.getDeclaredFields()) {
            if (e.isAnnotationPresent(AutoData.class)) {
                fields.add(ensureAccessible(clazz.getDeclaredField(e.getName()),
                                            object
                ));
            }
        }
        return fields;
    }

    public <T> JSONObject payload(T object) throws Exception {
        LinkedList<Field> fields = autoFields(object);

        JSONObject json = new JSONObject();

        for (Field field : fields) {
            AutoData data = field.getAnnotation(AutoData.class);

            String key = data.key()
                             .equals("") ? field.getName() : data.key();

            serialize(json,
                      key,
                      field.get(object),
                      field
            );
        }

        return json;
    }

    public <T> T create(T object, JSONObject json) throws Exception {
        LinkedList<Field> fields = autoFields(object);

        for (Field field : fields) {
            AutoData data = field.getAnnotation(AutoData.class);

            String key = data.key()
                             .equals("") ? field.getName() : data.key();

            Object deserialized = deserialize(json,
                                              key,
                                              field.get(object),
                                              field
            );

            System.out.println(deserialized);

            field.set(object,
                      deserialized
            );
        }

        return object;
    }

    public void serialize(JSONObject json, String key, Object object, Field field) {
        JsonSerializer<?> serializer = getSerializer(field.getType());

        if (serializer == null) {
            if (object instanceof JsonSerializable<?> serializable) {
                json.put(key,
                         serializable.serialize()
                );
            }
        } else {
            serializer.serialize(json,
                                 key,
                                 EntrustEnvironment.cast(object)
            );
        }
    }

    public Object deserialize(JSONObject json, String key, Object object, Field field) {
        JsonSerializer<?> serializer = getSerializer(field.getType());

        if (serializer == null) {
            if (JsonSerializable.class.isAssignableFrom(object.getClass())) {
                try {
                    JsonSerializable<?> serializable = (JsonSerializable<?>) object.getClass()
                                                                                   .getConstructor()
                                                                                   .newInstance();
                    serializable.deserialize(json.getJSONObject(key));
                    return serializable;
                } catch (Exception e) {
                    return null;
                }
            } else {
                return null;
            }
        } else {
            return serializer.deserialize(json,
                                          key
            );
        }
    }

    public <T> JsonSerializer<T> getSerializer(Class<T> type) {
        if (type == null) {
            return null;
        }
        JsonSerializer<T> serializer = EntrustEnvironment.cast(this.targetToSerializer.get(type));
        if (serializer == null) {
            serializer = EntrustEnvironment.cast(getSerializer(type.getSuperclass()));
            if (serializer == null) {
                for (Class<?> aInterface : type.getInterfaces()) {
                    serializer = EntrustEnvironment.cast(getSerializer(aInterface));
                    if (serializer != null) {
                        break;
                    }
                }
            }
        }
        return serializer;
    }
}

