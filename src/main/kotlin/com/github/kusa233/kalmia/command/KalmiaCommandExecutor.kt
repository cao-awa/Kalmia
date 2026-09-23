package com.github.kusa233.kalmia.command

import com.github.cao.awa.cason.serialize.parser.JSONParser
import com.github.kusa233.kalmia.entrypoint.KalmiaEntrypoint
import com.github.kusa233.kalmia.constant.KalmiaInformation
import com.github.kusa233.kalmia.status.KalmiaStatus
import com.github.kusa233.kalmia.status.KalmiaStatus.reload
import com.github.kusa233.kalmia.status.KalmiaStatus.stop
import com.github.kusa233.kalmia.time.KalmiaDate
import com.sun.management.HotSpotDiagnosticMXBean
import org.apache.logging.log4j.Level
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import org.apache.logging.log4j.core.config.Configurator
import oshi.SystemInfo
import oshi.software.os.OperatingSystem
import java.io.File
import java.lang.management.BufferPoolMXBean
import java.lang.management.ManagementFactory
import java.nio.charset.StandardCharsets
import java.util.Locale
import java.util.Locale.getDefault
import javax.management.ObjectName
import kotlin.math.min


object KalmiaCommandExecutor {
    private val LOGGER: Logger = LogManager.getLogger("KalmiaCommandExecutor")
    private val RUNTIME: Runtime = Runtime.getRuntime()
    private val SYSTEM_INFO: SystemInfo = SystemInfo()
    private val OPERATING_SYSTEM: OperatingSystem = SYSTEM_INFO.operatingSystem
    private val HELPERS: MutableMap<String, String> = mutableMapOf<String, String>().also {
        it["help"] = "Show this command helper list"
        it["?"] = it["help"]!!
        it["stop"] = "Stop all running lifecycle and stop Kalmia"
        it["exit"] = it["stop"]!!
        it["reload"] = "Stop all running lifecycle and reload Kalmia"
        it["memory"] = "Get JVM memory information"
        it["mem"] = it["memory"]!!
        it["threads"] = "Get all threads information"
        it["thread"] = "Get thread information of target thread id, need an integer input <thread_id>"
        it["deadlock"] = "Get all deadlock threads information"
        it["uptime"] = "Get server running uptime"
        it["pid"] = "Get server running pid"
        it["jvmargs"] = "Get jvm launch arguments"
        it["env"] = "Get all environment variables"
        it["version"] = "Get versions information"
        it["osinfo"] = "Get operating system information"
        it["gc"] = "Try call System.gc() to start once garbage collection"
        it["status"] = "Get basic runtime information for Kalmia"
        it["services"] = "Get all registered services name and type"
        it["thread_dump"] = "Dump all thread status and stacktrace to <dump_file_name>"
        it["threaddump"] = it["thread_dump"]!!
        it["heap_dump"] = "Dump all heap memory data to <dump_file_name>"
        it["heapdump"] = it["heap_dump"]!!
        it["loglevel"] = "Get or change loglevel, change loglevel need a string input <loglevel>"
        it["plugins"] = "Get all plugins load status"
        it["plugin"] = "Get a plugin details information, need an input <plugin_name>"
        it["config"] = "Get a config content, need an input <config_name>"
    }

    @JvmStatic
    fun executeCommand(sourceCommand: String) {
        try {
            val command = if (sourceCommand.contains(" ")) {
                sourceCommand.substring(0, sourceCommand.indexOf(" "))
            } else {
                sourceCommand
            }
            val params = if (sourceCommand.contains(" ")) {
                val paramsCommand = sourceCommand.substring(sourceCommand.indexOf(" ") + 1)
                paramsCommand.split(" ")
            } else {
                emptyList()
            }
            if (params.isEmpty()) {
                when (command) {
                    "?", "help" -> handleHelpCommand()
                    "stop", "exit" -> stop()
                    "reload" -> reload()
                    "mem", "memory" -> handleMemoryCommand()
                    "threads" -> handleThreadsCommand()
                    "deadlock" -> handleDeadlockCommand()
                    "uptime" -> handleUptimeCommand()
                    "pid" -> handlePidCommand()
                    "jvmargs" -> handleJvmArgsCommand()
                    "env" -> handleEnvCommand()
                    "version" -> handleVersionCommand()
                    "osinfo" -> handleOsInfoCommand()
                    "gc" -> handleGcCommand()
                    "status" -> handleStatusCommand()
                    "services" -> handleServicesCommand()
                    "thread_dump", "threaddump" -> handleThreadDumpCommand(params)
                    "loglevel" -> handleLogLevelCommand()
                    "plugins" -> handlePluginsCommand()
                    else -> unknownCommand(command)
                }
            } else {
                when (command) {
                    "thread" -> handleThreadCommand(params)
                    "help" -> handleHelpCommand(params)
                    "threaddump", "thread_dump" -> handleThreadDumpCommand(params)
                    "heapdump", "heap_dump" -> handleHeapDumpCommand(params)
                    "loglevel" -> handleLogLevelCommand(params)
                    "plugin" -> handlePluginCommand(params)
                    "config" -> handleConfigCommand(params)
                    else -> commandCannotRunWithParams(command)
                }
            }
        } catch (e: Exception) {
            LOGGER.info("Failed to execute command '{}'", sourceCommand, e)
        }
    }

    fun unknownCommand(command: String) {
        LOGGER.info(
            "Unknown command: {}",
            command
        )
    }

    fun commandCannotRunWithParams(command: String) {
        LOGGER.info(
            "Command '{}' cannot input parameters",
            command
        )
    }

    fun handleConfigCommand(params: List<String>) {
        LOGGER.info("-- Config --")
        val configFile = if (params[0].endsWith(".json")) {
            File("configs/${params[0]}")
        } else {
            File("configs/${params[0]}.json")
        }
        if (configFile.exists() && configFile.isFile) {
            val json = JSONParser.parseObject(configFile.readText(StandardCharsets.UTF_8))
            LOGGER.info(json.toString(true, "    "))
        } else {
            LOGGER.error("Config '{}' not found", params[0])
        }
    }

    fun handlePluginCommand(params: List<String>) {
        LOGGER.info("-- Plugin --")
        val dependenciesManager = KalmiaEntrypoint.DEPENDENCIES_MANAGER
        val plugin = dependenciesManager.getPlugin(params[0])
        if (plugin != null) {
            val isLoaded = if (dependenciesManager.isPluginLoaded(plugin.name)) {
                "loaded"
            } else {
                "not loaded"
            }
            LOGGER.info("Status: {}", isLoaded)
            LOGGER.info("Name: {}", plugin.name)
            LOGGER.info("Entrypoint: {}", plugin.entrypoint)
            if (plugin.fallback != "") {
                LOGGER.info("Fallback: {}", plugin.fallback)
            } else {
                LOGGER.info("No fallback path")
            }
            if (plugin.unload != "") {
                LOGGER.info("Unload: {}", plugin.unload)
            } else {
                LOGGER.info("No unload path")
            }
            if (plugin.dependsOn.isNotEmpty()) {
                LOGGER.info("Depends on: ")
                for (depends in plugin.dependsOn) {
                    LOGGER.info("    + {}", depends)
                }
            }
        } else {
            LOGGER.error("Plugin '{}' not found", params[0])
        }
    }

    fun handlePluginsCommand() {
        LOGGER.info("-- Plugins --")
        val dependenciesManager = KalmiaEntrypoint.DEPENDENCIES_MANAGER
        for ((_, plugin) in dependenciesManager.getPlugins()) {
            val isLoaded = if (dependenciesManager.isPluginLoaded(plugin.name)) {
                "loaded"
            } else {
                "not loaded"
            }
            LOGGER.info("Plugin '{}' {}", plugin.name, isLoaded)
        }
    }

    fun handleLogLevelCommand(params: List<String>) {
        LOGGER.info("-- Log level --")
        LOGGER.info("Current logger level: {}", LOGGER.level.name())
        val level = when (params[0].uppercase(getDefault())) {
            "OFF" -> Level.OFF
            "FATAL" -> Level.FATAL
            "ERROR" -> Level.ERROR
            "WARN" -> Level.WARN
            "INFO" -> Level.INFO
            "DEBUG" -> Level.DEBUG
            "TRACE" -> Level.TRACE
            "ALL" -> Level.ALL
            else -> {
                LOGGER.warn("Cannot set loglevel, because no such level: '{}'", params[0].uppercase(getDefault()))
                null
            }
        }
        if (level != null) {
            LOGGER.info("Setting loglevel to '{}'", level.name())
            val context = LogManager.getContext(false)
            for (logger in context.loggerRegistry.loggers) {
                Configurator.setLevel(logger, level)
            }
            Configurator.setRootLevel(level)
            LOGGER.info("Loglevel set to '{}' now", level.name())
        }
    }

    fun handleLogLevelCommand() {
        LOGGER.info("-- Log level --")
        LOGGER.info("Current logger level: {}", LOGGER.level.name())
    }

    fun handleHeapDumpCommand(params: List<String>) {
        LOGGER.info("-- Heap dump --")
        try {
            val server = ManagementFactory.getPlatformMBeanServer()
            val beanName = ObjectName.getInstance("com.sun.management:type=HotSpotDiagnostic")
            val mxBean = ManagementFactory.newPlatformMXBeanProxy(
                server, beanName.canonicalName, HotSpotDiagnosticMXBean::class.java
            )
            LOGGER.info("Heap dumping...")
            val dumpFile = File(params[0])
            dumpFile.parentFile.mkdirs()
            mxBean.dumpHeap(dumpFile.absolutePath, false)
            LOGGER.info("Heap dump already written to file '{}'", dumpFile.absolutePath)
        } catch (e: Exception) {
            LOGGER.error("Failed to dump heap", e)
        }
    }

    fun handleThreadDumpCommand(params: List<String>) {
        val threads = Thread.getAllStackTraces()
        LOGGER.info("-- Thread dump --")
        val builder = StringBuilder()
        builder.append("-- Kalmia thread dump --").append("\n")
        builder.append("Kalmia: ").append(KalmiaInformation.VERSION).append("\n")
        builder.append("JVM: ")
            .append(System.getProperty("java.vm.name"))
            .append(" ")
            .append(System.getProperty("java.vm.version")).append("\n")
        builder.append("Threads: ").append(threads.size)
        builder.append("\n")
        for ((thread, stacktrace) in threads) {
            builder.append("\nThread '")
            builder.append(thread.name)
            builder.append("' (id: ")
            builder.append(thread.threadId())
            builder.append("): ")
            builder.append(thread.state)
            builder.append("\n")
            builder.append("Is alive: ").append(thread.isAlive).append("\n")
            builder.append("Is virtual: ").append(thread.isVirtual).append("\n")
            builder.append("Is daemon: ").append(thread.isDaemon).append("\n")
            builder.append("Is interrupted: ").append(thread.isInterrupted).append("\n")
            builder.append("priority: ").append(thread.priority).append("\n")
            builder.append("Stacktrace:").append("\n")
            for (element in stacktrace) {
                builder.append("    - ")
                builder.append(element.toString())
                builder.append("\n")
            }
        }

        if (params.isNotEmpty()) {
            val dumpFile = File(params[0])
            dumpFile.parentFile.mkdirs()
            dumpFile.writeText(builder.toString())
            LOGGER.info("Thread dump already written to file '{}'", dumpFile.absolutePath)
        } else {
            LOGGER.info(builder.toString())
        }
    }

    fun handleServicesCommand() {
        LOGGER.info("-- Registered services --")
        for ((name, service) in KalmiaStatus.registeredLifecycle()) {
            LOGGER.info("Service '{}': {}", name, service::class.simpleName)
        }
    }

    fun handleStatusCommand() {
        LOGGER.info("-- Kalmia status --")
        LOGGER.info("Kalmia: {}", KalmiaInformation.VERSION)
        LOGGER.info("JVM: {} {}", System.getProperty("java.vm.name"), System.getProperty("java.vm.version"))
        LOGGER.info(
            "OS: {}({}), {}",
            System.getProperty("os.name"),
            System.getProperty("os.arch"),
            System.getProperty("os.version")
        )
        LOGGER.info("Uptime: {}", KalmiaDate.formatTime(System.currentTimeMillis() - KalmiaEntrypoint.START_TIME))
        val threads = Thread.getAllStackTraces()
        LOGGER.info("Threads count: {}", threads.size)
        val runningThreadCount = threads.keys.filter {
            it.state == Thread.State.RUNNABLE
        }.size
        LOGGER.info("Running threads count: {}", runningThreadCount)
        val blockedhreadCount = threads.keys.filter {
            it.state == Thread.State.BLOCKED
        }.size
        LOGGER.info("Blocked threads count: {}", blockedhreadCount)
        val waitingThreadCount = threads.keys.filter {
            it.state == Thread.State.WAITING
        }.size
        LOGGER.info("Waiting threads count: {}", waitingThreadCount)
        LOGGER.info("Deadlock threads: {}", getDeadlockThreads().size)
        LOGGER.info("Registered service count: {}", KalmiaStatus.registeredLifecycle().size)
        LOGGER.info("Registered services: {}", KalmiaStatus.registeredLifecycle().keys.toString())
        LOGGER.info("Total memory: {}MB", RUNTIME.totalMemory() / 1024 / 1024)
        LOGGER.info("Max memory: {}MB", RUNTIME.maxMemory() / 1024 / 1024)
        LOGGER.info("Free memory: {}MB", RUNTIME.freeMemory() / 1024 / 1024)
    }

    fun handleGcCommand() {
        val bean = ManagementFactory.getRuntimeMXBean()
        val jvmArgs = bean.inputArguments
        if (jvmArgs.contains("-XX:+DisableExplicitGC")) {
            LOGGER.warn("JVM already disabled explicit GC, cannot run 'gc' command")
        } else {
            LOGGER.info("Starting garbage collect")
            System.gc()
        }
    }

    fun handleEnvCommand() {
        LOGGER.info("-- Environment variables --")
        for ((key, value) in System.getenv()) {
            LOGGER.info("{}: {}", key, value)
        }
    }

    fun handleOsInfoCommand() {
        val hardware = SYSTEM_INFO.hardware
        LOGGER.info("-- OS info --")
        LOGGER.info("Total memory: {}GB", (hardware.memory.total / 1024F / 1024F / 1024F).toString().let {
            it.substring(0, min(it.length, it.indexOf(".") + 2))
        })
        LOGGER.info("CPU Name: {}", hardware.processor.processorIdentifier.name)
        LOGGER.info("Physical processors: {}", hardware.processor.physicalProcessorCount)
        LOGGER.info("Logical processors: {}", hardware.processor.logicalProcessorCount)
    }

    fun handleVersionCommand() {
        LOGGER.info("-- Versions --")
        LOGGER.info("Kalmia: {}", KalmiaInformation.VERSION)
        LOGGER.info("JVM: {} {}", System.getProperty("java.vm.name"), System.getProperty("java.vm.version"))
        LOGGER.info(
            "OS: {}({}), {}",
            System.getProperty("os.name"),
            System.getProperty("os.arch"),
            System.getProperty("os.version")
        )
    }

    fun handleHelpCommand(params: List<String>) {
        val command = params[0]
        val helper = HELPERS[command]

        if (helper != null) {
            LOGGER.info("-- Command: '{}' helper --", command)
            LOGGER.info("{}: {}", command, helper)
        } else {
            LOGGER.info("No such command: '{}'", command)
        }
    }

    fun handleHelpCommand() {
        LOGGER.info("-- Command helper --")
        LOGGER.info("help(or '?'): {}", HELPERS["help"])
        LOGGER.info("stop, exit: {}", HELPERS["stop"])
        LOGGER.info("reload: {}", HELPERS["reload"])
        LOGGER.info("memory(or 'mem'): {}", HELPERS["memory"])
        LOGGER.info("threads: {}", HELPERS["threads"])
        LOGGER.info("deadlock: {}", HELPERS["deadlock"])
        LOGGER.info("uptime: {}", HELPERS["uptime"])
        LOGGER.info("pid: {}", HELPERS["pid"])
        LOGGER.info("jvmargs: {}", HELPERS["jvmargs"])
        LOGGER.info("env: {}", HELPERS["env"])
        LOGGER.info("version: {}", HELPERS["version"])
        LOGGER.info("osinfo: {}", HELPERS["osinfo"])
        LOGGER.info("gc: {}", HELPERS["gc"])
        LOGGER.info("status: {}", HELPERS["status"])
        LOGGER.info("services: {}", HELPERS["services"])
        LOGGER.info("threaddump(or 'thread_dump': {}", HELPERS["threaddump"])
        LOGGER.info("heapdump(or 'heap_dump'): {}", HELPERS["heapdump"])
        LOGGER.info("loglevel: {}", HELPERS["loglevel"])
        LOGGER.info("loglevel <loglevel>: {}", HELPERS["loglevel"])
        LOGGER.info("plugins: {}", HELPERS["plugins"])
        LOGGER.info("plugin <plugin_name>: {}", HELPERS["plugin"])
        LOGGER.info("config <config_name>: {}", HELPERS["config"])
        LOGGER.info("thread <thread_id>: {}", HELPERS["thread"])
    }

    fun handleJvmArgsCommand() {
        val bean = ManagementFactory.getRuntimeMXBean()
        val jvmArgs = bean.inputArguments
        LOGGER.info("-- JVM arguments --")
        for (arg in jvmArgs) {
            LOGGER.info(arg)
        }
    }

    fun handleUptimeCommand() {
        val uptime = System.currentTimeMillis() - KalmiaEntrypoint.START_TIME
        LOGGER.info("-- Uptime --")

        LOGGER.info("Kalmia server has been running {}", KalmiaDate.formatTime(uptime))
    }

    fun handleMemoryCommand() {
        val memoryMXBean = ManagementFactory.getMemoryMXBean()
        LOGGER.info("-- Memory --")
        LOGGER.info("Total memory: {}MB", RUNTIME.totalMemory() / 1024 / 1024)
        LOGGER.info("Max memory: {}MB", RUNTIME.maxMemory() / 1024 / 1024)
        LOGGER.info("Free memory: {}MB", RUNTIME.freeMemory() / 1024 / 1024)
        val nonHeapMemoryUsage = memoryMXBean.nonHeapMemoryUsage
        if (nonHeapMemoryUsage.max == -1L) {
            LOGGER.info("Non-heap memory max: unlimited")
        } else {
            LOGGER.info("Non-heap memory max: {}MB", nonHeapMemoryUsage.max / 1024 / 1024)
        }
        LOGGER.info("Non-heap memory committed: {}MB", nonHeapMemoryUsage.committed / 1024 / 1024)
        LOGGER.info("Non-heap memory used: {}MB", nonHeapMemoryUsage.used / 1024 / 1024)
        val pools = ManagementFactory.getMemoryPoolMXBeans()
        for (pool in pools) {
            if (pool.name.lowercase(Locale.getDefault()).contains("metaspace")) {
                val usage = pool.getUsage()
                if (usage.max == -1L) {
                    LOGGER.info("Metaspace max: unlimited")
                } else {
                    LOGGER.info("Metaspace max: {}MB", usage.max / 1024 / 1024)
                }
                LOGGER.info("Metaspace committed: {}MB", usage.committed / 1024 / 1024)
                LOGGER.info("Metaspace used: {}MB", usage.used / 1024 / 1024)
                break
            }
        }
        val bufferPools = ManagementFactory.getPlatformMXBeans(BufferPoolMXBean::class.java)
        for (bufferPool in bufferPools) {
            if (bufferPool.name == "direct") {
                LOGGER.info("Direct memory total capacity: {}MB", bufferPool.totalCapacity / 1024 / 1024)
                LOGGER.info("Direct memory used: {}MB", bufferPool.memoryUsed / 1024 / 1024)
                LOGGER.info("Direct memory count: {}", bufferPool.count)
                break
            }
        }
    }

    fun handlePidCommand() {
        val pid = ProcessHandle.current().pid()
        LOGGER.info("-- PID --")
        LOGGER.info("Current PID: {}", pid)
    }

    fun handleThreadsCommand() {
        LOGGER.info("-- Threads --")
        val allStackTraces = Thread.getAllStackTraces()
        val deadlockThreads = mutableListOf<Thread>()
        LOGGER.info("# All threads")
        for (thread in allStackTraces.keys) {
            if (isDeadlockThread(thread)) {
                deadlockThreads.add(thread)
            }
            LOGGER.info("Thread '{}'(id: {}):  {}", thread.name, thread.threadId(), thread.state)
        }
        if (deadlockThreads.isNotEmpty()) {
            LOGGER.warn("# Deadlock threads")
            for (thread in deadlockThreads) {
                LOGGER.warn("Thread '{}'(id: {}):  {}, DEADLOCK", thread.name, thread.threadId(), thread.state)
            }
        }
    }

    fun handleDeadlockCommand() {
        val bean = ManagementFactory.getThreadMXBean()
        val deadlockThreads = bean.findDeadlockedThreads()
        LOGGER.info("-- Deadlock threads --")
        if (deadlockThreads != null && deadlockThreads.isNotEmpty()) {
            for (threadId in deadlockThreads) {
                val thread = getThread(threadId)!!
                LOGGER.info("Deadlock thread '{}'(id: {}): {}", thread.name, thread.threadId(), thread.state)
                printThreadDetails(thread)
                LOGGER.info("Thread stacktrace: ")
                printThreadStacktrace(thread)
            }
        } else {
            LOGGER.info("No deadlock threads found")
        }
    }

    fun handleThreadCommand(params: List<String>) {
        if (params.isNotEmpty()) {
            try {
                val id = params[0].toLong()
                val targetThread: Thread? = getThread(id)
                if (targetThread == null) {
                    LOGGER.info("Thread with id '{}' not found", id)
                } else {
                    LOGGER.info("-- Thread '{}'(id: {}) --", targetThread.name, targetThread.threadId())
                    printThreadDetails(targetThread)
                    LOGGER.info("Thread stacktrace:")
                    printThreadStacktrace(targetThread)
                }
                return
            } catch (_: Exception) {

            }
        }
        LOGGER.warn("Command 'thread' usage: thread [id]")
        return
    }

    fun printThreadDetails(thread: Thread) {
        LOGGER.info("Thread is alive: {}", thread.isAlive)
        LOGGER.info("Thread is virtual: {}", thread.isVirtual())
        LOGGER.info("Thread is daemon: {}", thread.isDaemon)
        LOGGER.info("Thread is interrupted: {}", thread.isInterrupted)
        LOGGER.info("Thread priority: {}", thread.priority)
        var deadlock = isDeadlockThread(thread)
        LOGGER.info("Thread deadlock: {}", deadlock)
    }

    fun getDeadlockThreads(): List<Thread> {
        val bean = ManagementFactory.getThreadMXBean()
        return bean.findDeadlockedThreads()?.map {
            getThread(it)!!
        } ?: emptyList()
    }

    fun isDeadlockThread(thread: Thread): Boolean {
        return getDeadlockThreads().contains(thread)
    }

    fun printThreadStacktrace(thread: Thread) {
        for (stacktrace in thread.stackTrace) {
            LOGGER.info(" - {}", stacktrace.toString())
        }
    }

    fun getThread(id: Long): Thread? {
        val allStackTraces = Thread.getAllStackTraces()
        for (thread in allStackTraces.keys) {
            if (thread.threadId() == id) {
                return thread
            }
        }
        return null
    }
}