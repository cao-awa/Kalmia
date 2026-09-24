package com.github.kusa233.kalmia.server.network.http.entrypoint

import com.github.kusa233.kalmia.launch.config.KalmiaLaunchConfig
import com.github.kusa233.kalmia.server.network.http.KalmiaHttpServer
import com.github.kusa233.kalmia.server.network.http.builder.http
import com.github.kusa233.kalmia.server.network.http.config.KalmiaHttpServerConfig
import com.github.kusa233.kalmia.server.network.http.entrypoint.service.KalmiaHttpService
import com.github.kusa233.kalmia.server.network.http.exception.path.HttpPathNotRegisteredException
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import java.io.File

object KalmiaHttpServerEntrypoint {
    private val LOGGER: Logger = LogManager.getLogger("KalmiaHttpServerEntrypoint")

    @JvmStatic
    fun start(config: KalmiaLaunchConfig) {
        val serverConfig = KalmiaHttpServerConfig.create(File("configs/kalmia_http.json"))

        val nettyConfig = serverConfig.nettyServerConfig()
        val assetManagerConfig = serverConfig.assetManagerConfig()

        if (config.printConfigDetails()) {
            // Asset manager configs
            LOGGER.info("-- Asset manager configs --")
            LOGGER.info("Config 'enable': {}", assetManagerConfig.enable())
            LOGGER.info("Config 'asset_path': {}", assetManagerConfig.assetPath())
            LOGGER.info("Config 'error_page': {}", assetManagerConfig.errorPage())
            LOGGER.info("Config 'cache': {}", assetManagerConfig.cache())

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

        KalmiaHttpService.start(http {
            // Redirect all no registered query to 404 page.
            ifAbort(HttpPathNotRegisteredException::class) {
                withAsset(redirectAsset = assetManagerConfig.errorPage())
            }
        })
    }
}