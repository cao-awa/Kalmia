package com.github.cao.awa.kalmia.test.junit.descriptor

import org.junit.platform.engine.TestDescriptor
import org.junit.platform.engine.UniqueId
import org.junit.platform.engine.support.descriptor.AbstractTestDescriptor
import org.junit.platform.engine.support.descriptor.EngineDescriptor

class KalmiaTestDescriptor(id: UniqueId): AbstractTestDescriptor(id, "Kalmia") {
    override fun getType(): TestDescriptor.Type {
        return TestDescriptor.Type.TEST
    }
}