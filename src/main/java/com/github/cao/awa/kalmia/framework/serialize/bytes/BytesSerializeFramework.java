package com.github.cao.awa.kalmia.framework.serialize.bytes;

import com.github.cao.awa.apricot.annotations.auto.Auto;
import com.github.cao.awa.apricot.io.bytes.reader.BytesReader;
import com.github.cao.awa.apricot.util.collection.ApricotCollectionFactor;
import com.github.cao.awa.kalmia.annotations.auto.network.unsolve.AutoData;
import com.github.cao.awa.kalmia.annotations.auto.serializer.AutoBytesSerializer;
import com.github.cao.awa.kalmia.framework.reflection.ReflectionFramework;
import com.github.cao.awa.kalmia.mathematic.base.Base256;
import com.github.cao.awa.kalmia.mathematic.base.SkippedBase256;
import com.github.cao.awa.kalmia.network.packet.Packet;
import com.github.cao.awa.viburnum.util.bytes.BytesUtil;
import com.github.zhuaidadaya.rikaishinikui.handler.universal.entrust.EntrustEnvironment;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.Nullable;

import java.io.ByteArrayOutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class BytesSerializeFramework extends ReflectionFramework {
    private static final Logger LOGGER = LogManager.getLogger("ByteSerializerFramework");
    private final Map<Class<?>, BytesSerializer<?>> typeToSerializer = ApricotCollectionFactor.hashMap();
    private final Map<Long, BytesSerializer<?>> idToSerializer = ApricotCollectionFactor.hashMap();
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
        return clazz.isAnnotationPresent(AutoBytesSerializer.class) && BytesSerializer.class.isAssignableFrom(clazz);
    }

    public Class<? extends BytesSerializer<?>> cast(Class<?> clazz) {
        return EntrustEnvironment.cast(clazz);
    }

    public void build(Class<? extends BytesSerializer<?>> type) {
        try {
            BytesSerializer<?> serializer = type.getConstructor()
                                                .newInstance();
            AutoBytesSerializer annotation = type.getAnnotation(AutoBytesSerializer.class);
            long id = annotation.value();
            Class<?>[] target = annotation.target();
            this.typeToId.put(type,
                              id
            );
            this.typeToTarget.put(type,
                                  target
            );

            LOGGER.info("Register auto bytes serializer({}): {}",
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

        BytesSerializer<?> current = this.idToSerializer.get(id);

        if (current != null) {
            LOGGER.warn("Failed register the bytes serializer {} because id {} has been used by {}",
                        serializer.getClass()
                                  .getName(),
                        id,
                        current.getClass()
                               .getName()
            );
            return;
        }
        for (Class<?> t : matchType) {
            this.typeToSerializer.put(t,
                                      serializer
            );
        }
        this.idToSerializer.put(id,
                                serializer
        );

        LOGGER.info("Bytes serializer {} registered by id {}, targeted to {}",
                    serializer.getClass()
                              .getName(),
                    id,
                    Arrays.stream(matchType)
                          .filter(Objects :: nonNull)
                          .map(Class :: getName)
                          .collect(Collectors.toList())
        );
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

    public <T> byte[] payload(T object) throws Exception {
        LinkedList<Field> fields = autoFields(object);

        ByteArrayOutputStream output = new ByteArrayOutputStream();

        for (Field field : fields) {
            output.write(
                    serialize(field.get(object),
                              field
                    )
            );
        }

        return output.toByteArray();
    }

    public <T> T create(T object, BytesReader reader) throws Exception {
        LinkedList<Field> fields = autoFields(object);

        for (Field field : fields) {
            Object deserialized = deserialize(field.getType(),
                                              reader
            );
            field.set(object,
                      deserialized
            );
        }

        return object;
    }

    public byte[] serialize(Object object, Field field) {
        BytesSerializer<?> serializer = getSerializer(field.getType());

        if (Modifier.isAbstract(field.getType()
                                     .getModifiers()) && serializer == null) {
            if (object instanceof BytesSerializable<?> serializable) {
                return BytesUtil.concat(
                        BytesUtil.arrau(1),
                        Base256.tagToBuf(serializable.getClass()
                                                     .getName()
                                                     .length()),
                        serializable.getClass()
                                    .getName()
                                    .getBytes(StandardCharsets.UTF_8),
                        serializable.serialize()
                );
            }

            System.out.println(object.getClass());

            serializer = getSerializer(object.getClass());

            return BytesUtil.concat(BytesUtil.arrau(2),
                                    SkippedBase256.longToBuf(serializer.id()),
                                    serializer.serialize(EntrustEnvironment.cast(object))
            );
        } else {
            if (object instanceof BytesSerializable<?> serializable) {
                return BytesUtil.concat(
                        BytesUtil.arrau(- 1),
                        serializable.serialize()
                );
            }
            return BytesUtil.concat(
                    BytesUtil.arrau(- 1),
                    serializer.serialize(EntrustEnvironment.cast(object))
            );
        }
    }

    public Object deserialize(Class<?> type, BytesReader reader) throws Exception {
        switch (reader.read()) {
            case - 1 -> {
                if (BytesSerializable.class.isAssignableFrom(type)) {
                    BytesSerializable<?> serializable = (BytesSerializable<?>) type.getConstructor()
                                                                                   .newInstance();
                    serializable.deserialize(reader);
                    return serializable;
                }
                return getSerializer(type).deserialize(reader);
            }
            case 1 -> {
                BytesSerializable<?> serializable = (BytesSerializable<?>) Class.forName(new String(reader.read(Base256.tagFromBuf(reader.read(2))),
                                                                                                    StandardCharsets.UTF_8
                                                                                ))
                                                                                .getConstructor()
                                                                                .newInstance();
                serializable.deserialize(reader);
                return serializable;
            }
            case 2 -> {
                return getSerializer(SkippedBase256.readLong(reader)).deserialize(reader);
            }
            default -> {
                return null;
            }
        }
    }

    public <T> BytesSerializer<T> getSerializer(Class<T> type) {
        if (type == null) {
            return null;
        }
        BytesSerializer<T> serializer = EntrustEnvironment.cast(this.typeToSerializer.get(type));
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

    public <T> BytesSerializer<T> getSerializer(T o) {
        return EntrustEnvironment.cast(getSerializer(o.getClass()));
    }

    public <T> BytesSerializer<T> getSerializer(long id) {
        return EntrustEnvironment.cast(this.idToSerializer.get(id));
    }
}