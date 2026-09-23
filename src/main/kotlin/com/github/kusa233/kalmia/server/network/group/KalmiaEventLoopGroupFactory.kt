package com.github.kusa233.kalmia.server.network.group

import com.google.common.util.concurrent.ThreadFactoryBuilder
import io.netty.channel.*
import io.netty.channel.epoll.Epoll
import io.netty.channel.epoll.EpollIoHandler
import io.netty.channel.epoll.EpollServerSocketChannel
import io.netty.channel.kqueue.KQueue
import io.netty.channel.kqueue.KQueueIoHandler
import io.netty.channel.kqueue.KQueueServerSocketChannel
import io.netty.channel.local.LocalIoHandler
import io.netty.channel.local.LocalServerChannel
import io.netty.channel.nio.NioIoHandler
import io.netty.channel.socket.nio.NioServerSocketChannel
import io.netty.channel.uring.IoUring
import io.netty.channel.uring.IoUringIoHandler
import io.netty.channel.uring.IoUringServerSocketChannel
import java.util.concurrent.ThreadFactory

abstract class KalmiaEventLoopGroupFactory internal constructor(
    private val name: String,
    val channel: Class<out ServerChannel>
) {
    companion object {
        private val THREAD_FACTORY: ThreadFactory = Thread.ofPlatform().factory()
        private val NIO: KalmiaEventLoopGroupFactory =
            object :
                KalmiaEventLoopGroupFactory("NIO", NioServerSocketChannel::class.java) {
                override fun newIoHandlerFactory(): IoHandlerFactory = NioIoHandler.newFactory()
            }
        private val EPOLL: KalmiaEventLoopGroupFactory =
            object : KalmiaEventLoopGroupFactory("Epoll", EpollServerSocketChannel::class.java) {
                override fun newIoHandlerFactory(): IoHandlerFactory = EpollIoHandler.newFactory()
            }
        private val IO_URING: KalmiaEventLoopGroupFactory =
            object : KalmiaEventLoopGroupFactory("Io_Uring", IoUringServerSocketChannel::class.java) {
                override fun newIoHandlerFactory(): IoHandlerFactory = IoUringIoHandler.newFactory()
            }
        private val KQUEUE: KalmiaEventLoopGroupFactory =
            object : KalmiaEventLoopGroupFactory("Kqueue", KQueueServerSocketChannel::class.java) {
                override fun newIoHandlerFactory(): IoHandlerFactory = KQueueIoHandler.newFactory()
            }
        private val LOCAL: KalmiaEventLoopGroupFactory =
            object : KalmiaEventLoopGroupFactory("Local", LocalServerChannel::class.java) {
                override fun newIoHandlerFactory(): IoHandlerFactory = LocalIoHandler.newFactory()
            }

        fun remote(): KalmiaEventLoopGroupFactory {
            if (Epoll.isAvailable()) {
                return EPOLL
            }
            if (KQueue.isAvailable()) {
                return KQUEUE
            }
            return NIO
        }

        fun nio(): KalmiaEventLoopGroupFactory = NIO

        fun epoll(): KalmiaEventLoopGroupFactory = EPOLL

        fun ioUring(): KalmiaEventLoopGroupFactory = IO_URING

        fun kqueue(): KalmiaEventLoopGroupFactory = KQUEUE

        fun local(): KalmiaEventLoopGroupFactory = LOCAL

        fun validate(io: KalmiaEventLoopGroupFactory): KalmiaEventLoopGroupFactory {
            if (io == local()) {
                return io
            }

            if (io == epoll() && Epoll.isAvailable()) {
                return io
            }

            if (io == kqueue() && KQueue.isAvailable()) {
                return io
            }

            if (io == ioUring() && IoUring.isAvailable()) {
                return io
            }

            return NIO
        }
    }

    protected abstract fun newIoHandlerFactory(): IoHandlerFactory

    private fun createThreadFactory(): ThreadFactory {
        return ThreadFactoryBuilder()
            .setNameFormat("Netty ${this.name} / #%d")
            .setThreadFactory(THREAD_FACTORY)
            .setDaemon(true)
            .build()
    }

    fun createEventLoopGroup(count: Int = 1): EventLoopGroup {
        synchronized(this) {
            return MultiThreadIoEventLoopGroup(count, createThreadFactory(), newIoHandlerFactory())
        }
    }
}
