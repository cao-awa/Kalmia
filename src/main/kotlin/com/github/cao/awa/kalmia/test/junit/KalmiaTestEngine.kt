package com.github.cao.awa.kalmia.test.junit

import com.github.cao.awa.kalmia.entrypoint.KalmiaEntrypoint
import com.github.cao.awa.kalmia.test.junit.descriptor.KalmiaTestDescriptor
import org.junit.platform.engine.EngineDiscoveryRequest
import org.junit.platform.engine.ExecutionRequest
import org.junit.platform.engine.TestDescriptor
import org.junit.platform.engine.TestEngine
import org.junit.platform.engine.TestExecutionResult
import org.junit.platform.engine.UniqueId
import org.junit.platform.engine.support.descriptor.EngineDescriptor

class KalmiaTestEngine: TestEngine {
    override fun getId(): String {
        return "kalmia-engine"
    }

    override fun discover(
        discoveryRequest: EngineDiscoveryRequest,
        uniqueId: UniqueId
    ): TestDescriptor {
        val root = EngineDescriptor(uniqueId, "Kalmia")

        root.addChild(KalmiaTestDescriptor(uniqueId.append("test", "entry")))

        return root
    }

    override fun execute(request: ExecutionRequest) {
        val root = request.rootTestDescriptor

        val listener = request.engineExecutionListener

        listener.executionStarted(root)

        try {
            KalmiaEntrypoint.main()

            listener.executionFinished(root, TestExecutionResult.successful())
        } catch (e: Throwable) {
            listener.executionFinished(root, TestExecutionResult.failed(e))
        }
    }
}