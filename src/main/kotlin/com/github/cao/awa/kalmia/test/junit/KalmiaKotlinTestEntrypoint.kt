package com.github.cao.awa.kalmia.test.junit

import com.github.cao.awa.kalmia.entrypoint.KalmiaEntrypoint
import org.junit.jupiter.api.extension.BeforeAllCallback
import org.junit.jupiter.api.extension.ExtensionContext

class KalmiaKotlinTestEntrypoint: BeforeAllCallback {
    override fun beforeAll(context: ExtensionContext) {
        KalmiaEntrypoint.main()
    }
}