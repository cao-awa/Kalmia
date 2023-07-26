package com.github.cao.awa.kalmia.framework.serialize;

import com.github.cao.awa.apricot.annotation.auto.Auto;
import com.github.cao.awa.apricot.io.bytes.reader.BytesReader;
import com.github.cao.awa.apricot.util.collection.ApricotCollectionFactor;
import com.github.cao.awa.kalmia.annotation.auto.serializer.AutoSerializer;
import com.github.cao.awa.kalmia.framework.reflection.ReflectionFramework;
import com.github.cao.awa.kalmia.framework.serialize.serializer.BytesSerializer;
import com.github.zhuaidadaya.rikaishinikui.handler.universal.entrust.EntrustEnvironment;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class ByteSerializeFramework extends ReflectionFramework {
    private static final Logger LOGGER = LogManager.getLogger("ByteSerializerFramework");
    private final Map<Class<?>, BytesSerializer<?>> typeToSerializers = ApricotCollectionFactor.hashMap();
    private final Map<Long, BytesSerializer<?>> idToSerializers = ApricotCollectionFactor.hashMap();
    private final Map<Class<? extends BytesSerializer<?>>, Long> typeToId = ApricotCollectionFactor.hashMap();
    private final Map<Class<? extends BytesSerializer<?>>, Class<?>[]> typeToTarget = ApricotCollectionFactor.hashMap();

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
        return clazz.isAnnotationPresent(AutoSerializer.class) && BytesSerializer.class.isAssignableFrom(clazz);
    }

    public Class<? extends BytesSerializer<?>> cast(Class<?> clazz) {
        return EntrustEnvironment.cast(clazz);
    }

    public void build(Class<? extends BytesSerializer<?>> type) {
        try {
            BytesSerializer<?> serializer = type.getConstructor()
                                                .newInstance();
            AutoSerializer annotation = type.getAnnotation(AutoSerializer.class);
            long id = annotation.value();
            Class<?>[] target = annotation.target();
            this.typeToId.put(type,
                              id
            );
            this.typeToTarget.put(type,
                                  target
            );

            LOGGER.info("Register auto serializer({}): {}",
                        id,
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

    public Class<?>[] target(BytesSerializer<?> serializer) {
        return typeTarget(EntrustEnvironment.cast(serializer.getClass()));
    }

    public Class<?>[] typeTarget(Class<? extends BytesSerializer<?>> type) {
        return this.typeToTarget.get(type);
    }

    public long id(BytesSerializer<?> serializer) {
        return typeId(EntrustEnvironment.cast(serializer.getClass()));
    }

    public long typeId(Class<? extends BytesSerializer<?>> type) {
        return this.typeToId.getOrDefault(type,
                                          - 1L
        );
    }

    public final <T> void registerSerializer(BytesSerializer<T> serializer, @Nullable Class<?>... matchType) {
        if (matchType == null) {
            return;
        }

        long id = serializer.id();

        BytesSerializer<?> current = this.idToSerializers.get(id);

        if (current != null) {
            LOGGER.warn("Failed register the serializer {} because id {} has been used by {}",
                        serializer.getClass()
                                  .getName(),
                        id,
                        current.getClass()
                               .getName()
            );
            return;
        }
        for (Class<?> t : matchType) {
            this.typeToSerializers.put(t,
                                       serializer
            );
        }
        this.idToSerializers.put(id,
                                 serializer
        );

        LOGGER.info("Serializer {} registered by id {}, targeted to {}",
                    serializer.getClass()
                              .getName(),
                    id,
                    Arrays.stream(matchType)
                          .filter(Objects :: nonNull)
                          .map(Class :: getName)
                          .collect(Collectors.toList())
        );
    }

    public byte[] serialize(Object object) throws Exception {
        if (object instanceof BytesSerializable serializable) {
            return serializable.serialize();
        }
        return getSerializer(object.getClass()).serialize(EntrustEnvironment.cast(object));
    }

    public Object deserialize(Class<?> type, BytesReader reader) throws Exception {
        if (BytesSerializable.class.isAssignableFrom(type)) {
            BytesSerializable serializable = (BytesSerializable) type.getConstructor()
                                                                     .newInstance();
            serializable.deserialize(reader);
            return serializable;
        }
        return getSerializer(type).deserialize(reader);
    }

    public <T> BytesSerializer<T> getSerializer(Class<T> type) {
        if (type == null) {
            return null;
        }
        BytesSerializer<T> serializer = EntrustEnvironment.cast(this.typeToSerializers.get(type));
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

    public <T> BytesSerializer<T> getSerializer(long id) {
        return EntrustEnvironment.cast(this.idToSerializers.get(id));
    }
}