package com.github.kusa233.kalmia.launch.config

import java.util.LinkedList

object KalmiaLaunchDefaultConfig: KalmiaLaunchConfig() {
    private fun throwWhenSet(): Nothing {
        error("Cannot set config in default server config instance")
    }

    override fun printConfigDetails(print: Boolean): KalmiaLaunchConfig {
        throwWhenSet()
    }

    override fun entrypoint(entrypoint: LinkedList<String>): KalmiaLaunchConfig {
        throwWhenSet()
    }

    override fun error(error: Throwable): KalmiaLaunchConfig {
        throwWhenSet()
    }
}