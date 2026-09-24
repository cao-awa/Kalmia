package com.github.kusa233.kalmia.server.network.websocket.entrypoint

import com.github.kusa233.kalmia.launch.config.KalmiaLaunchConfig
import com.github.kusa233.kalmia.server.network.http.KalmiaHttpServer
import com.github.kusa233.kalmia.server.network.http.builder.http
import com.github.kusa233.kalmia.server.network.http.config.KalmiaHttpServerConfig
import com.github.kusa233.kalmia.server.network.http.exception.path.HttpPathNotRegisteredException
import com.github.kusa233.kalmia.server.network.websocket.builder.websocket
import com.github.kusa233.kalmia.server.network.websocket.config.KalmiaWebsocketServerConfig
import com.github.kusa233.kalmia.server.network.websocket.entrypoint.service.KalmiaWebSocketService
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import java.io.File

object KalmiaWebsocketServerEntrypoint {
    private val LOGGER: Logger = LogManager.getLogger("KalmiaHttpServerEntrypoint")

    @JvmStatic
    fun start(config: KalmiaLaunchConfig) {
        val serverConfig = KalmiaWebsocketServerConfig.create(File("configs/kalmia_http.json"))

        val nettyConfig = serverConfig.nettyServerConfig()

        if (config.printConfigDetails()) {
            // Netty configs.
            LOGGER.info("-- Netty configs --")
            LOGGER.info("Config 'io': {}", nettyConfig.ioName())
            LOGGER.info("Config 'backlog': {}", nettyConfig.backlog())
            LOGGER.info("Config 'keep_alive': {}", nettyConfig.keepalive())
            LOGGER.info("Config 'rcv_buffer': {}", nettyConfig.rcvBuf())
            LOGGER.info("Config 'reuse_address': {}", nettyConfig.reuseAddr())
            LOGGER.info("Config 'allocator': {}", nettyConfig.allocatorName())
            LOGGER.info("Config 'tcp_no_delay': {}", nettyConfig.tcpNoDelay())
        }

        KalmiaWebSocketService.start(websocket {
        })
    }
}