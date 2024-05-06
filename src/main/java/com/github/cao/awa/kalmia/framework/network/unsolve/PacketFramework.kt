package com.github.cao.awa.kalmia.framework.network.unsolve

import com.github.cao.awa.apricot.annotations.auto.Auto
import com.github.cao.awa.apricot.io.bytes.reader.BytesReader
import com.github.cao.awa.apricot.util.collection.ApricotCollectionFactor
import com.github.cao.awa.kalmia.annotations.auto.network.unsolve.AutoSolvedPacket
import com.github.cao.awa.kalmia.env.KalmiaEnv
import com.github.cao.awa.kalmia.framework.reflection.ReflectionFramework
import com.github.cao.awa.kalmia.mathematic.base.SkippedBase256
import com.github.cao.awa.kalmia.network.packet.Packet
import com.github.cao.awa.kalmia.network.packet.UnsolvedPacket
import com.github.cao.awa.kalmia.network.packet.factor.unsolve.UnsolvedPacketFactor
import com.github.cao.awa.lilium.catheter.Catheter
import com.github.zhuaidadaya.rikaishinikui.handler.universal.entrust.EntrustEnvironment
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import java.lang.reflect.Constructor
import java.lang.reflect.InvocationTargetException
import java.util.function.Function

class PacketFramework : ReflectionFramework() {
    companion object {
        private val LOGGER: Logger = LogManager.getLogger("PacketFramework")
    }

    private val constructors: MutableMap<Class<out Packet<*>>, Constructor<out Packet<*>>> =
        ApricotCollectionFactor.hashMap()
    private val ids: MutableMap<Class<out Packet<*>>, ByteArray> = ApricotCollectionFactor.hashMap()

    override fun work() {
        // Working stream...
        Catheter.of(reflection().getTypesAnnotatedWith(Auto::class.java))
            .filter(this::match)
            .vary(this::cast)
            .each(this::build)
    }

    fun match(clazz: Class<*>): Boolean =
        clazz.isAnnotationPresent(AutoSolvedPacket::class.java) && Packet::class.java.isAssignableFrom(clazz)

    fun cast(clazz: Class<*>): Class<out Packet<*>>? = EntrustEnvironment.cast(clazz)

    fun build(packet: Class<out Packet<*>>?) {
        val id = packet!!.getAnnotation(AutoSolvedPacket::class.java).id

        this.constructors[packet] = packet.getConstructor()
        this.ids[packet] = SkippedBase256.longToBuf(id)

        val function = Function<ByteArray?, UnsolvedPacket<*>?> { data: ByteArray? ->
            AutoUnsolved(
                data,
                packet,
                this
            )
        }

        LOGGER.info(
            "Register unsolved {}: {}",
            id,
            packet.name
        )
        UnsolvedPacketFactor.register(
            id,
            function
        )
    }

    fun solve(clazz: Class<out Packet<*>>, reader: BytesReader): Packet<*>? {
        try {
            val packet = this.constructors[clazz]?.newInstance() ?: return null
            fetchMethod(packet, "solves", BytesReader::class.java).invoke(packet, reader)
            return packet
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
        return null
    }

    fun id(type: Class<out Packet<*>>): ByteArray? = this.ids[type]

    fun id(type: Packet<*>): ByteArray? = this.ids[type.javaClass]

    @Throws(Exception::class)
    fun payload(packet: Packet<*>?): ByteArray {
        return KalmiaEnv.BYTES_SERIALIZE_FRAMEWORK.payload(packet)
    }

    @Throws(Exception::class)
    fun create(packet: Packet<*>?, reader: BytesReader) {
        KalmiaEnv.BYTES_SERIALIZE_FRAMEWORK.create(
            packet,
            reader
        )
    }

    private class AutoUnsolved(
        data: ByteArray?,
        private val clazz: Class<out Packet<*>>,
        private val framework: PacketFramework
    ) : UnsolvedPacket<Packet<*>?>(data) {
        override fun packet(): Packet<*>? = create()

        @Throws(InvocationTargetException::class, InstantiationException::class, IllegalAccessException::class)
        private fun create(): Packet<*>? {
            val packet = this.framework.solve(
                this.clazz,
                reader()
            )
            val p = EntrustEnvironment.cast<Packet<*>>(packet!!.receipt(receipt()))
            println(p)
            return p
        }

        override fun requireCrypto(): Boolean = this.clazz.getAnnotation(AutoSolvedPacket::class.java).crypto
    }
}
