package com.github.cao.awa.kalmia.framework.serialize.bytes

import com.github.cao.awa.apricot.annotations.auto.Auto
import com.github.cao.awa.apricot.io.bytes.reader.BytesReader
import com.github.cao.awa.apricot.util.collection.ApricotCollectionFactor
import com.github.cao.awa.kalmia.annotations.auto.network.unsolve.AutoAllData
import com.github.cao.awa.kalmia.annotations.auto.network.unsolve.AutoData
import com.github.cao.awa.kalmia.annotations.auto.serializer.AutoBytesSerializer
import com.github.cao.awa.kalmia.framework.reflection.ReflectionFramework
import com.github.cao.awa.kalmia.mathematic.base.Base256
import com.github.cao.awa.kalmia.mathematic.base.SkippedBase256
import com.github.cao.awa.kalmia.network.packet.Packet
import com.github.cao.awa.lilium.catheter.Catheter
import com.github.cao.awa.viburnum.util.bytes.BytesUtil
import com.github.zhuaidadaya.rikaishinikui.handler.universal.entrust.EntrustEnvironment
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import org.jetbrains.annotations.NotNull
import java.io.ByteArrayOutputStream
import java.lang.reflect.Field
import java.lang.reflect.Modifier
import java.nio.charset.StandardCharsets
import java.util.*

class BytesSerializeFramework : ReflectionFramework() {
    companion object {
        private val LOGGER: Logger = LogManager.getLogger("ByteSerializerFramework")
    }

    private val typeToSerializer: MutableMap<Class<*>?, BytesSerializer<*>> = ApricotCollectionFactor.hashMap()
    private val idToSerializer: MutableMap<Long, BytesSerializer<*>> = ApricotCollectionFactor.hashMap()
    private val typeToId: MutableMap<Class<out BytesSerializer<*>>, Long> = ApricotCollectionFactor.hashMap()
    private val typeToTarget: MutableMap<Class<out BytesSerializer<*>>, Array<Class<*>>> =
        ApricotCollectionFactor.hashMap()

    override fun work() {
        // Working stream...
        Catheter.of(reflection().getTypesAnnotatedWith(Auto::class.java))
            .filter(this::match)
            .vary(this::cast)
            .each(this::build)
    }

    fun match(clazz: Class<*>): Boolean {
        return clazz.isAnnotationPresent(AutoBytesSerializer::class.java) && BytesSerializer::class.java.isAssignableFrom(
            clazz
        )
    }

    fun cast(clazz: Class<*>): Class<out BytesSerializer<*>?> {
        return clazz.let(EntrustEnvironment::cast)!!
    }

    fun build(type: Class<out BytesSerializer<*>>) {
        try {
            val serializer = type.getConstructor()
                .newInstance()
            val annotation = type.getAnnotation(AutoBytesSerializer::class.java)
            val id = annotation.value
            val target: Array<Class<*>> = annotation.target.map { it.java }.toTypedArray()
            this.typeToId[type] = id
            this.typeToTarget[type] = target

            LOGGER.info(
                "Register auto bytes serializer({}): {}",
                id,
                serializer.javaClass.name
            )
            registerSerializer(
                serializer,
                *serializer.target()
            )
        } catch (e: Exception) {
            LOGGER.error(e)
        }
    }

    fun target(serializer: BytesSerializer<*>): Array<Class<*>> {
        return typeTarget(EntrustEnvironment.cast(serializer.javaClass)!!)
    }

    private fun typeTarget(type: Class<out BytesSerializer<*>>): Array<Class<*>> {
        return this.typeToTarget[type]!!
    }

    fun id(serializer: BytesSerializer<*>): Long {
        return typeId(EntrustEnvironment.cast(serializer.javaClass)!!)
    }

    private fun typeId(type: Class<out BytesSerializer<*>>): Long {
        return this.typeToId.getOrDefault(
            type,
            -1L
        )
    }

    fun <T> registerSerializer(serializer: BytesSerializer<T>, vararg matchType: Class<*>) {
        val id = serializer.id()

        val current = this.idToSerializer[id]

        if (current != null) {
            LOGGER.warn(
                "Failed register the bytes serializer {} because id {} has been used by {}",
                serializer.javaClass.name,
                id,
                current.javaClass.name
            )
            return
        }
        for (t in matchType) {
            this.typeToSerializer[t] = serializer
        }
        this.idToSerializer[id] = serializer

        LOGGER.info("Bytes serializer {} registered by id {}, targeted to {}",
            serializer.javaClass.name,
            id,
            Catheter.of(matchType)
                .filter(Objects::nonNull)
                .vary { it.name }
                .list()
        )
    }

    @Throws(NoSuchFieldException::class)
    private fun <T> autoFields(target: T): LinkedList<Field> {
        val clazz = EntrustEnvironment.cast<Class<Packet<*>>>(target!!::class.java)!!
        val fields = ApricotCollectionFactor.linkedList<Field>()
        val autoAll = clazz.isAnnotationPresent(AutoAllData::class.java)
        for (field in clazz.declaredFields) {
            if ((autoAll && !Modifier.isStatic(field.modifiers)) || field.isAnnotationPresent(
                    AutoData::class.java
                )
            ) {
                fields.add(ensureAccessible(clazz.getDeclaredField(field.name)))
            }
        }
        return fields
    }

    @Throws(Exception::class)
    fun <T> payload(target: T): ByteArray {
        val fields = autoFields(target)

        val output = ByteArrayOutputStream()

        for (field in fields) {
            output.write(
                serialize(
                    field[target],
                    field
                )
            )
        }

        return output.toByteArray()
    }

    @Throws(Exception::class)
    fun <T> create(target: T, reader: BytesReader): T {
        val fields = autoFields(target)

        for (field in fields) {
            val deserialized = deserialize(
                field.type,
                reader
            )
            field[target] = deserialized
        }

        return target
    }

    fun serialize(target: Any, field: Field): ByteArray {
        var serializer = getSerializer(field.type)

        if (Modifier.isAbstract(
                field.type.modifiers
            ) && serializer == null
        ) {
            if (target is BytesSerializable<*>) {
                return BytesUtil.concat(
                    BytesUtil.array(1),
                    Base256.tagToBuf(
                        target.javaClass.name.length
                    ),
                    target.javaClass
                        .name
                        .toByteArray(StandardCharsets.UTF_8),
                    target.serialize()
                )
            }

            serializer = getSerializer(target.javaClass)

            return BytesUtil.concat(
                BytesUtil.array(2),
                SkippedBase256.longToBuf(serializer!!.id()),
                serializer.serialize(EntrustEnvironment.cast(target))
            )
        } else {
            if (target is BytesSerializable<*>) {
                return BytesUtil.concat(
                    BytesUtil.array(-1),
                    target.serialize()
                )
            }
            return BytesUtil.concat(
                BytesUtil.array(-1),
                serializer!!.serialize(EntrustEnvironment.cast(target))
            )
        }
    }

    fun serialize(target: Any): ByteArray {
        val serializer: BytesSerializer<*>? = getSerializer(target.javaClass)
        if (serializer == null) {
            if (target is BytesSerializable<*>) {
                return target.serialize()
            }
            return BytesUtil.EMPTY
        }
        return serializer.serialize(EntrustEnvironment.cast(target))
    }

    @Throws(Exception::class)
    fun deserialize(type: Class<*>, reader: BytesReader): Any? {
        when (reader.read().toInt()) {
            -1 -> {
                if (BytesSerializable::class.java.isAssignableFrom(type)) {
                    val serializable = type.getConstructor()
                        .newInstance() as BytesSerializable<*>
                    serializable.deserialize(reader)
                    return serializable
                }
                return getSerializer(type)!!.deserialize(reader)
            }

            1 -> {
                val serializable = Class.forName(
                    String(
                        reader.read(Base256.tagFromBuf(reader.read(2))),
                        StandardCharsets.UTF_8
                    )
                )
                    .getConstructor()
                    .newInstance() as BytesSerializable<*>
                serializable.deserialize(reader)
                return serializable
            }

            2 -> {
                return getSerializer<Any>(SkippedBase256.readLong(reader)).deserialize(reader)
            }

            else -> {
                return null
            }
        }
    }

    fun <T> getSerializer(@NotNull type: Class<T>): BytesSerializer<T>? {
        var serializer = EntrustEnvironment.cast<BytesSerializer<T>>(this.typeToSerializer[type] ?: return null)
        if (serializer == null) {
            serializer = EntrustEnvironment.cast(getSerializer(type.superclass) ?: return null)
            if (serializer == null) {
                for (aInterface in type.interfaces) {
                    serializer = EntrustEnvironment.cast(getSerializer(aInterface) ?: return null)
                    if (serializer != null) {
                        break
                    }
                }
            }
        }
        return serializer
    }

    fun <T> getSerializer(o: T): BytesSerializer<T> {
        return getSerializer(o!!::class.java)?.let(EntrustEnvironment::cast)!!
    }

    fun <T> getSerializer(id: Long): BytesSerializer<T> {
        return this.idToSerializer[id]?.let(EntrustEnvironment::cast)!!
    }

    @Throws(Exception::class)
    fun <T> breakRefs(o: T): T? {
        return EntrustEnvironment.cast(
            deserialize(
                o!!::class.java,
                BytesReader.of(serialize(o))
            )!!
        )
    }
}