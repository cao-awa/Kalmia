package com.github.cao.awa.kalmia.framework.serialize.type.list;

import com.github.cao.awa.apricot.io.bytes.reader.BytesReader;
import com.github.cao.awa.apricot.util.collection.ApricotCollectionFactor;
import com.github.cao.awa.kalmia.annotation.auto.serializer.AutoSerializer;
import com.github.cao.awa.kalmia.env.KalmiaEnv;
import com.github.cao.awa.kalmia.framework.serialize.BytesSerializable;
import com.github.cao.awa.kalmia.framework.serialize.serializer.BytesSerializer;
import com.github.cao.awa.kalmia.mathematic.base.SkippedBase256;
import com.github.zhuaidadaya.rikaishinikui.handler.universal.entrust.EntrustEnvironment;

import java.io.ByteArrayOutputStream;
import java.lang.reflect.Constructor;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@AutoSerializer(value = 500, target = List.class)
public class ListSerializer implements BytesSerializer<List<?>> {
    private static final byte[] EMPTY = new byte[]{- 1};

    public static boolean checkType(List<?> list) {
        Class<?> first = list.get(0)
                             .getClass();
        for (int i = 1; i < list.size(); i++) {
            if (! first.equals(list.get(i)
                                   .getClass())) {
                return false;
            }
        }
        return true;
    }

    public static Class<?> firstType(List<?> list) {
        return list.get(0)
                   .getClass();
    }

    @Override
    public byte[] serialize(List<?> objects) throws Exception {
        if (objects.size() < 1) {
            return EMPTY;
        }
        ByteArrayOutputStream output = new ByteArrayOutputStream();

        try {
            output.write(SkippedBase256.intToBuf(objects.size()));

            if (checkType(objects)) {
                Class<?> type = firstType(objects);
                if (BytesSerializable.class.isAssignableFrom(type)) {
                    output.write(0);

                    String className = type.getName();

                    output.write(SkippedBase256.intToBuf(className.length()));
                    output.write(className.getBytes(StandardCharsets.UTF_8));
                    for (Object object : objects) {
                        output.write(((BytesSerializable) object).serialize());
                    }
                } else {
                    BytesSerializer<Object> serializer = EntrustEnvironment.cast(KalmiaEnv.serializeFramework.getSerializer(type));
                    assert serializer != null;
                    output.write(1);
                    output.write(SkippedBase256.longToBuf(serializer.id()));
                    for (Object object : objects) {
                        output.write(serializer.serialize(object));
                    }
                }
            } else {
                output.write(2);

                ByteArrayOutputStream wrap = new ByteArrayOutputStream();

                int repeatCounter = 0;
                int counter = 0;

                Class<?> targetType = firstType(objects);

                // Prepare for next step uses.
                BytesSerializer<Object> serializer = null;

                for (Object object : objects) {
                    counter++;

                    Class<?> type = object.getClass();

                    if (! targetType.equals(type)) {
                        output.write(SkippedBase256.intToBuf(repeatCounter));
                        output.write(wrap.toByteArray());

                        wrap.reset();

                        repeatCounter = 0;
                    }

                    if (object instanceof BytesSerializable serializable) {
                        // Write marker at head.
                        if (repeatCounter == 0) {
                            wrap.write(3);

                            String className = type.getName();
                            wrap.write(SkippedBase256.intToBuf(className.length()));
                            wrap.write(className.getBytes(StandardCharsets.UTF_8));
                        }

                        wrap.write(serializable.serialize());

                        repeatCounter++;
                    } else {
                        // Write marker at head.
                        if (repeatCounter == 0) {
                            serializer = EntrustEnvironment.cast(KalmiaEnv.serializeFramework.getSerializer(type));

                            wrap.write(4);

                            assert serializer != null;
                            wrap.write(SkippedBase256.longToBuf(serializer.id()));
                        }

                        assert serializer != null;
                        wrap.write(serializer.serialize(object));

                        repeatCounter++;
                    }

                    if (counter == objects.size()) {
                        output.write(SkippedBase256.intToBuf(repeatCounter));
                        output.write(wrap.toByteArray());
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return output.toByteArray();
    }

    @Override
    public List<?> deserialize(BytesReader reader) throws Exception {
        reader.flag();

        if (reader.read() == - 1) {
            return ApricotCollectionFactor.newArrayList();
        }

        reader.back();

        int size = SkippedBase256.readInt(reader);

        List<Object> result = ApricotCollectionFactor.newArrayList(size);
        switch (reader.read()) {
            case 0 -> {
                String className = new String(reader.read(SkippedBase256.readInt(reader)),
                                              StandardCharsets.UTF_8
                );

                Constructor<?> constructor = Class.forName(className)
                                                  .getConstructor();

                for (int i = 0; i < size; i++) {
                    if (constructor.newInstance() instanceof BytesSerializable serializable) {
                        serializable.deserialize(reader);
                        result.add(serializable);
                    }
                }
            }
            case 1 -> {
                BytesSerializer<?> serializer = KalmiaEnv.serializeFramework.getSerializer(SkippedBase256.readLong(reader));
                for (int i = 0; i < size; i++) {
                    result.add(serializer.deserialize(reader));
                }
            }
            case 2 -> {
                for (int i = 0; i < size; ) {
                    int current = SkippedBase256.readInt(reader);
                    if (reader.read() == 3) {
                        // Read as ByteSerializable deserialize.
                        String className = new String(reader.read(SkippedBase256.readInt(reader)),
                                                      StandardCharsets.UTF_8
                        );

                        Constructor<?> constructor = Class.forName(className)
                                                          .getConstructor();

                        for (int c = 0; c < current; c++) {
                            // Deserialize the bytes in the ByteSerializable instance and put it to result.
                            if (constructor.newInstance() instanceof BytesSerializable serializable) {
                                serializable.deserialize(reader);
                                result.add(serializable);
                            }

                            // Increase the size counter.
                            i++;
                        }
                    } else if (
                        // Here back 1 step, because last read cause the cursor moves 1 step forward.
                            reader.back(1)
                                  .read() == 4) {
                        // Read as ByteSerializer deserialize.
                        BytesSerializer<?> serializer = KalmiaEnv.serializeFramework.getSerializer(SkippedBase256.readLong(reader));

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
        return result;
    }

    @Override
    public List<?> initializer() {
        return ApricotCollectionFactor.newArrayList();
    }

    public static void main(String[] args) {
        try {
            KalmiaEnv.setupServer();

            List<Object> www = new ArrayList<>();

            www.add(123);
//            www.add(12);
            www.add("99w");
//            www.add(114);

            ListSerializer serializer = new ListSerializer();

            byte[] payload = serializer.serialize(www);

            System.out.println(Arrays.toString(payload));

            List<Object> deserialized = (List<Object>) serializer.deserialize(new BytesReader(payload));

            System.out.println(deserialized);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
