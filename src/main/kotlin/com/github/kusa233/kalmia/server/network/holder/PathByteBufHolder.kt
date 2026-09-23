package com.github.kusa233.kalmia.server.network.holder

import io.netty.buffer.ByteBuf
import io.netty.buffer.ByteBufHolder
import java.net.URLDecoder
import java.nio.charset.StandardCharsets

abstract class PathByteBufHolder(private val holder: ByteBufHolder): ByteBufHolder {
    override fun content(): ByteBuf = this.holder.content()

    override fun copy(): ByteBufHolder = this.holder.copy()

    override fun duplicate(): ByteBufHolder = this.holder.duplicate()

    override fun retainedDuplicate(): ByteBufHolder = this.holder.retainedDuplicate()

    override fun replace(content: ByteBuf?): ByteBufHolder = this.holder.replace(content)

    override fun retain(): ByteBufHolder = this.holder.retain()

    override fun retain(increment: Int): ByteBufHolder = this.holder.retain(increment)

    override fun touch(): ByteBufHolder = this.holder.touch()

    override fun touch(hint: Any?): ByteBufHolder = this.holder.touch(hint)

    override fun refCnt(): Int = this.holder.refCnt()

    override fun release(): Boolean = this.holder.release()

    override fun release(decrement: Int): Boolean = this.holder.release(decrement)

    abstract fun path(): String
}