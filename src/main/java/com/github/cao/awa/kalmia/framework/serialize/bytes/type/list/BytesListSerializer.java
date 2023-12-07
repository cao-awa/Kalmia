package com.github.cao.awa.kalmia.framework.serialize.bytes.type.list;

import com.github.cao.awa.apricot.io.bytes.reader.BytesReader;
import com.github.cao.awa.apricot.util.collection.ApricotCollectionFactor;
import com.github.cao.awa.kalmia.annotations.auto.serializer.AutoBytesSerializer;
import com.github.cao.awa.kalmia.env.KalmiaEnv;
import com.github.cao.awa.kalmia.framework.serialize.bytes.BytesSerializable;
import com.github.cao.awa.kalmia.framework.serialize.bytes.BytesSerializeFramework;
import com.github.cao.awa.kalmia.framework.serialize.bytes.BytesSerializer;
import com.github.cao.awa.kalmia.mathematic.base.SkippedBase256;
import com.github.zhuaidadaya.rikaishinikui.handler.universal.entrust.EntrustEnvironment;

import java.io.ByteArrayOutputStream;
import java.lang.reflect.Constructor;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * The serializer used to {@link List} in {@link BytesSerializeFramework}.<br>
 *
 * @author cao_awa
 * @author 草二号机
 * @see BytesSerializer
 * @see BytesSerializeFramework
 * @see List
 * @since 1.0.0
 */
@AutoBytesSerializer(value = 500, target = List.class)
public class BytesListSerializer implements BytesSerializer<List<?>> {
    private static final byte[] EMPTY = new byte[]{- 1};

    public static boolean checkType(List<?> list) {
        Object first = list.get(0);

        boolean isNull = first == null;

        if (list.size() == 1) {
            return true;
        }

        for (int i = 1; i < list.size(); i++) {
            if (isNull) {
                if (list.get(i) != null) {
                    return false;
                }
            } else {
                if (! first.equals(list.get(i)
                                       .getClass())) {
                    return false;
                }
            }
        }
        return true;
    }

    public static Class<?> firstType(List<?> list) {
        Object object = list.get(0);
        if (object == null) {
            return null;
        }
        return object.getClass();
    }

    @Override
    public byte[] serialize(List<?> list) {
        if (list.size() == 0) {
            return EMPTY;
        }
        ByteArrayOutputStream output = new ByteArrayOutputStream();

        try {
            // Write the size of list.
            output.write(SkippedBase256.intToBuf(list.size()));

            // If the type of list is all consistent, then just write type once.
            if (checkType(list)) {
                // Get type to write.
                Class<?> type = firstType(list);

                // TODO
//                if (type == null) {
//                    output.write(5);
//                }

                if (BytesSerializable.class.isAssignableFrom(type)) {
                    // Write marker byte used to distinguish type mode.
                    // Mode 0 is type all consistent serializable mode.
                    output.write(0);

                    // Write type (name mode).
                    String className = type.getName();

                    output.write(SkippedBase256.intToBuf(className.length()));
                    output.write(className.getBytes(StandardCharsets.UTF_8));

                    // Do serialize for all.
                    for (Object object : list) {
                        output.write(((BytesSerializable<?>) object).serialize());
                    }
                } else {
                    BytesSerializer<Object> serializer = EntrustEnvironment.cast(KalmiaEnv.BYTES_SERIALIZE_FRAMEWORK.getSerializer(type));
                    assert serializer != null;

                    // Write marker byte used to distinguish type mode.
                    // Mode 1 is type all consistent serializer mode.
                    output.write(1);

                    // Write type (id mode).
                    output.write(SkippedBase256.longToBuf(serializer.id()));

                    // Do serialize for all.
                    for (Object object : list) {
                        output.write(serializer.serialize(object));
                    }
                }
            } else {
                // Write marker byte used to distinguish type mode.
                // Mode 2 is type not consistent mode.
                output.write(2);

                ByteArrayOutputStream wrap = new ByteArrayOutputStream();

                // Repeat counter used to count repeat type, do not write type multi times.
                // Used to reduces type writing.
                int repeatCounter = 0;
                int counter = 0;

                Class<?> targetType = firstType(list);

                // Prepare for next step uses.
                BytesSerializer<Object> serializer = null;

                for (Object object : list) {
                    counter++;

                    Class<?> type = object.getClass();

                    // If current type is not match to the last type, then reset the repeat counter, no repeating now.
                    if (! targetType.equals(type)) {
                        // Write the repeat counter data and buffer data.
                        output.write(SkippedBase256.intToBuf(repeatCounter));
                        output.write(wrap.toByteArray());

                        // Clear buffer.
                        wrap.reset();

                        // Reset repeat counter.
                        repeatCounter = 0;
                    }

                    if (object instanceof BytesSerializable<?> serializable) {
                        // Write marker at head.
                        if (repeatCounter == 0) {
                            // Write type mode marker, 3 is serializable name mode.
                            wrap.write(3);

                            // Write type (name mode).
                            String className = type.getName();
                            wrap.write(SkippedBase256.intToBuf(className.length()));
                            wrap.write(className.getBytes(StandardCharsets.UTF_8));
                        }

                        // Do serialize for once.
                        wrap.write(serializable.serialize());
                    } else {
                        // Write marker at data  head.
                        if (repeatCounter == 0) {
                            serializer = EntrustEnvironment.cast(KalmiaEnv.BYTES_SERIALIZE_FRAMEWORK.getSerializer(type));

                            // Write type mode marker, 4 is serializer id mode.
                            wrap.write(4);

                            // Write type (id mode).
                            assert serializer != null;
                            wrap.write(SkippedBase256.longToBuf(serializer.id()));
                        }

                        // Do serialize for once.
                        assert serializer != null;
                        wrap.write(serializer.serialize(object));
                    }

                    repeatCounter++;

                    // Done the rest buffer data.
                    if (counter == list.size()) {
                        output.write(SkippedBase256.intToBuf(repeatCounter));
                        output.write(wrap.toByteArray());
                    }

                    // Make expected target type to last type.
                    targetType = type;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return EMPTY;
        }

        return output.toByteArray();
    }

    @Override
    public List<?> deserialize(BytesReader reader) {
        try {
            reader.flag();

            if (reader.read() == - 1) {
                return ApricotCollectionFactor.arrayList();
            }

            reader.back();

            int size = SkippedBase256.readInt(reader);

            List<Object> result = ApricotCollectionFactor.arrayList(size);
            switch (reader.read()) {
                // Mode 0 is type all consistent serializable mode.
                case 0 -> {
                    String className = new String(reader.read(SkippedBase256.readInt(reader)),
                                                  StandardCharsets.UTF_8
                    );

                    Constructor<?> constructor = Class.forName(className)
                                                      .getConstructor();

                    for (int i = 0; i < size; i++) {
                        if (constructor.newInstance() instanceof BytesSerializable<?> serializable) {
                            serializable.deserialize(reader);
                            result.add(serializable);
                        }
                    }
                }
                // Mode 1 is type all consistent serializer mode.
                case 1 -> {
                    BytesSerializer<?> serializer = KalmiaEnv.BYTES_SERIALIZE_FRAMEWORK.getSerializer(SkippedBase256.readLong(reader));
                    for (int i = 0; i < size; i++) {
                        result.add(serializer.deserialize(reader));
                    }
                }
                // Mode 2 is type not consistent mode.
                case 2 -> {
                    for (int i = 0; i < size; ) {
                        int current = SkippedBase256.readInt(reader);

                        switch (reader.read()) {
                            // Mode 3 is serializable name mode in not consistent mode.
                            case 3 -> {
                                // Read as ByteSerializable deserialize.
                                String className = new String(reader.read(SkippedBase256.readInt(reader)),
                                                              StandardCharsets.UTF_8
                                );

                                Constructor<?> constructor = Class.forName(className)
                                                                  .getConstructor();

                                for (int c = 0; c < current; c++) {
                                    // Deserialize the bytes in the ByteSerializable instance and put it to result.
                                    if (constructor.newInstance() instanceof BytesSerializable<?> serializable) {
                                        serializable.deserialize(reader);
                                        result.add(serializable);
                                    }

                                    // Increase the size counter.
                                    i++;
                                }
                            }
                            // Mode 3 is serializer id mode in not consistent mode.
                            case 4 -> {
                                // Read as ByteSerializer deserialize.
                                BytesSerializer<?> serializer = KalmiaEnv.BYTES_SERIALIZE_FRAMEWORK.getSerializer(SkippedBase256.readLong(reader));

                                for (int c = 0; c < current; c++) {
                                    // Deserialize the bytes and put it to result.
                                    result.add(serializer.deserialize(reader));

                                    // Increase the size counter.
                                    i++;
                                }
                            }
                        }
                    }
                }
            }
            return result;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
