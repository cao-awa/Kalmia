package com.github.kusa233.kalmia.server.network.http.tls

import io.netty.buffer.ByteBuf
import io.netty.channel.ChannelHandlerContext
import io.netty.handler.codec.ByteToMessageDecoder
import io.netty.handler.ssl.SslContext
import io.netty.handler.ssl.SslHandler
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger

/**
 * Detects whether a connection on the shared HTTP port speaks TLS.
 *
 * The plain HTTP and the HTTPS listener share one socket, so the very first
 * bytes of a connection decide its protocol: a TLS record starts with a
 * handshake content type and a `3.x` version, which
 * [SslHandler.isEncrypted] recognizes. When the connection is TLS, an
 * [SslHandler] is inserted at the head of the pipeline and the buffered bytes
 * are replayed from there, so they are decrypted before reaching the HTTP
 * decoder; otherwise the bytes continue as plain HTTP.
 *
 * The handler removes itself once the protocol is known, so a connection pays
 * this cost only once.
 */
internal class KalmiaTlsDetectorHandler(
    private val sslContext: SslContext
) : ByteToMessageDecoder() {
    companion object {
        private val LOGGER: Logger = LogManager.getLogger("KalmiaTlsDetectorHandler")

        /**
         * Name of the inserted [SslHandler] in the pipeline.
         */
        const val SSL_HANDLER_NAME: String = "kalmia-tls"

        /**
         * A TLS record header and an HTTP method are both recognizable within
         * the first 5 bytes, which is what [SslHandler.isEncrypted] inspects.
         */
        private const val DETECT_BYTES: Int = 5
    }

    override fun decode(context: ChannelHandlerContext, input: ByteBuf, output: MutableList<Any>) {
        // Wait until enough bytes are buffered to tell the protocols apart.
        if (input.readableBytes() < DETECT_BYTES) {
            return
        }

        val pipeline = context.pipeline()
        // Take the bytes out of the cumulation buffer first, so removing this
        // handler does not emit them a second time.
        val bytes = input.readRetainedSlice(input.readableBytes())
        val encrypted = SslHandler.isEncrypted(bytes)

        if (encrypted) {
            pipeline.addFirst(SSL_HANDLER_NAME, SslHandler(this.sslContext.newEngine(context.alloc())))
            LOGGER.debug("Detected a TLS connection on the shared port, starting the SSL handshake")
        }

        pipeline.remove(this)
        // Replay from the head of the pipeline: a TLS connection then enters the
        // newly inserted SslHandler, a plain one goes straight to the HTTP decoder.
        pipeline.fireChannelRead(bytes)
    }
}
