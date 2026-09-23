package com.github.kusa233.kalmia.server.network.config

import com.github.kusa233.kalmia.server.network.group.KalmiaEventLoopGroupFactory
import io.netty.buffer.ByteBufAllocator
import io.netty.channel.WriteBufferWaterMark

object KalmiaNettyServerDefaultConfig: KalmiaNettyServerConfig() {
    private fun throwWhenSet(): Nothing {
        error("Cannot set config in default server config instance")
    }

    override fun io(io: KalmiaEventLoopGroupFactory): KalmiaNettyServerConfig {
        throwWhenSet()
    }

    override fun backlog(backlog: Int): KalmiaNettyServerConfig {
        throwWhenSet()
    }

    override fun keepalive(keepalive: Boolean): KalmiaNettyServerConfig {
        throwWhenSet()
    }

    override fun rcvBuf(rcvBuf: Int): KalmiaNettyServerConfig {
        throwWhenSet()
    }

    override fun sndBuf(sndBuf: Int): KalmiaNettyServerConfig {
        throwWhenSet()
    }

    override fun reuseAddr(reuseAddr: Boolean): KalmiaNettyServerConfig {
        throwWhenSet()
    }

    override fun allocator(allocator: ByteBufAllocator): KalmiaNettyServerConfig {
        throwWhenSet()
    }

    override fun writeBufferWaterMark(waterMark: WriteBufferWaterMark): KalmiaNettyServerConfig {
        throwWhenSet()
    }

    override fun tcpNoDelay(noDelay: Boolean): KalmiaNettyServerConfig {
        throwWhenSet()
    }
}