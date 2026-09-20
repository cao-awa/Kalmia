package com.github.cao.awa.kalmia.test.junit

import com.github.cao.awa.kalmia.entrypoint.KalmiaEntrypoint
import com.github.cao.awa.kalmia.test.junit.descriptor.KalmiaTestDescriptor
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import org.junit.platform.engine.EngineDiscoveryRequest
import org.junit.platform.engine.ExecutionRequest
import org.junit.platform.engine.TestDescriptor
import org.junit.platform.engine.TestEngine
import org.junit.platform.engine.TestExecutionResult
import org.junit.platform.engine.UniqueId
import org.junit.platform.engine.support.descriptor.EngineDescriptor

class KalmiaTestEngine: TestEngine {
    companion object {
        private val LOGGER: Logger = LogManager.getLogger("KalmiaTestEngine")
    }

    override fun getId(): String {
        return "kalmia-engine"
    }

    override fun discover(
        discoveryRequest: EngineDiscoveryRequest,
        uniqueId: UniqueId
    ): TestDescriptor {
        LOGGER.info("Discovering KalmiaTestDescriptor")
        val root = EngineDescriptor(uniqueId, "Kalmia")

        root.addChild(KalmiaTestDescriptor(uniqueId.append("test", "entry")))

        return root
    }

    override fun execute(request: ExecutionRequest) {
        LOGGER.info("Executing kalmia test")
        val root = request.rootTestDescriptor

        val test = root.children.single()

        val listener = request.engineExecutionListener

        listener.executionStarted(root)

        listener.executionStarted(test)

        try {
            KalmiaEntrypoint.main()

            listener.executionFinished(test, TestExecutionResult.successful())

            listener.executionFinished(root, TestExecutionResult.successful())
        } catch (e: Throwable) {
            listener.executionFinished(test, TestExecutionResult.failed(e))

            listener.executionFinished(root, TestExecutionResult.failed(e))
        }
    }
}