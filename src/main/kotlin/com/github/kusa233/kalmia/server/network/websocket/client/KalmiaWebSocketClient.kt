package com.github.kusa233.kalmia.server.network.websocket.client

import com.github.kusa233.kalmia.server.network.websocket.client.adapter.KalmiaWebSocketClientAdapter
import com.github.kusa233.kalmia.status.KalmiaStatus
import kotlin.reflect.KClass

class KalmiaWebSocketClient {
    private val handler: MutableMap<KClass<*>, (Any) -> Unit> = mutableMapOf()
    private lateinit var connection: KalmiaWebSocketConnection
    private lateinit var adapter: KalmiaWebSocketClientAdapter

    fun connect(host: String, port: Int) {
        this.connection = KalmiaWebSocketConnection(this)
        this.connection.connect(host, port)
        KalmiaStatus.registerReloadListener {
            disconnect()
        }
    }

    fun disconnect() {
        this.connection.close()
    }

    @Suppress("UNCHECKED_CAST")
    fun <T : Any> registerHandler(clazz: KClass<T>, handler: (T) -> Unit) {
        this.handler[clazz] = {
            handler(it as T)
        }
    }

    fun setAdapter(adapter: KalmiaWebSocketClientAdapter) {
        this.adapter = adapter
    }

    fun fireMessage(msg: Any) {
        this.handler[msg::class]?.invoke(msg)
    }

    fun sendMessage(msg: Any) {
        this.adapter.sendMessage(msg)
    }
}