package com.github.cao.awa.kalmia.entrypoint;

import com.github.cao.awa.kalmia.command.KalmiaCommandExecutor;
import com.github.cao.awa.kalmia.entrypoint.exception.KalmiaEntrypointStageFailedException;
import com.github.cao.awa.kalmia.launch.config.KalmiaLaunchConfig;
import com.github.cao.awa.kalmia.constant.KalmiaInformation;
import com.github.cao.awa.kalmia.entrypoint.lib.KalmiaLibraryLoader;
import com.github.cao.awa.kalmia.locker.KalmiaEntrypointLocker;
import com.github.cao.awa.kalmia.plugin.KalmiaPluginDependenciesManager;
import com.github.cao.awa.kalmia.status.KalmiaStatus;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.util.Scanner;

public class KalmiaEntrypoint {
    private static final Logger LOGGER = LogManager.getLogger("KalmiaEntryPoint");
    public static final KalmiaPluginDependenciesManager DEPENDENCIES_MANAGER = new KalmiaPluginDependenciesManager();
    private static String[] launchArgs = new String[0];
    public static final long START_TIME = System.currentTimeMillis();
    private static final KalmiaEntrypointLocker LOCKER = new KalmiaEntrypointLocker();

    public static void main(String... args) {
        launchArgs = args;
        LOGGER.info("Starting Kalmia({}) server...",
                KalmiaInformation.VERSION
        );

        Scanner scanner = new Scanner(System.in);
        if (launch(args)) {
            while (KalmiaStatus.isRunning()) {
                if (KalmiaStatus.isReloading()) {
                    DEPENDENCIES_MANAGER.getCleaners()
                            .forEach((name, cleaner) -> {
                                LOGGER.info("Clearing resources for '{}'",
                                        name
                                );
                                cleaner.invoke();
                            });

                    LOGGER.info("Cleaning cleaners...");
                    DEPENDENCIES_MANAGER.clearCleaners();

                    KalmiaEntrypoint.reload();
                }

                if (scanner.hasNextLine()) {
                    String command = scanner.nextLine();
                    KalmiaCommandExecutor.executeCommand(command);
                } else {
                    LOCKER.waitFor();
                }
            }
        }

        LOGGER.info("Stopping Kalmia");
        LOGGER.info("Kalmia stopped");
    }

    private static boolean launch(String[] args) {
        LOGGER.info("Kalmia running on directory '{}'",
                new File("").getAbsolutePath()
        );

        try {
            KalmiaLaunchConfig config = KalmiaLaunchConfig.createConfig(new File("configs/launch.json"));
            KalmiaKotlinEntrypoint.printConfigs(config);

            if (KalmiaStatus.isReloading()) {
                KalmiaKotlinEntrypoint.unloadPlugins();
            }

            KalmiaLibraryLoader.loadJars();
            KalmiaKotlinEntrypoint.entryToDeclared(
                    config,
                    args
            );

            return true;
        } catch (KalmiaEntrypointStageFailedException ex) {
            LOGGER.error("Failed to startup Kalmia server on entrypoint stage: '{}'",
                    ex.stage,
                    ex.cause
            );
        }

        return false;
    }

    public static void reload() {
        LOGGER.info("Reloading Kalmia({}) server...",
                KalmiaInformation.VERSION
        );

        LOCKER.offer();

        if (KalmiaStatus.isReloading()) {
            KalmiaStatus.completeReload();
        }

        launch(launchArgs);
    }
}