package com.github.cao.awa.kalmia.bootstrap

object ConnectTest {
    @JvmStatic
    fun main(args: Array<String>) {
        testConnect()
    }

    private fun testConnect() {
        Kalmia.startClient()
    }
}
