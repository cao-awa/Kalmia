# Custom entrypoint
Kalmia will generate a config file when it first startup, it seems like:
```json
{
    "print_config_details": true,
    "entrypoint": [
        "com.github.kusa233.kalmia.server.network.http.entrypoint.KalmiaHttpServerEntrypoint#entry"
    ]
}
```

You can declare an entrypoint in config with ``entrypoint`` key:
```json
{
    "entrypoint": [
        "com.github.xxx.entry.SampleEntrypoint#entry"
    ]
}
```

And writes code like this:

```kotlin
package com.github.xxx.entry

import com.github.kusa233.kalmia.launch.config.KalmiaLaunchConfig
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger

object SampleEntrypoint {
    private val LOGGER: Logger = LogManager.getLogger("SampleEntrypoint")

    @JvmStatic
    fun entry(launchConfig: KalmiaLaunchConfig) {
        LOGGER.info("External plugin test success!")
    }
}
```
And build a  jar, and put it into ``{working_path}/libs/ `` path, and start Kalmia runtime, Kalmia will autoload your jars, don't use ``shadowJar``, shadowJar will pack useless classes that already provided by Kalmia jar.

The ``entrypoint`` config must declare full package name and class name or plugin name (See [plugin document](https://github.com/cao-awa/Kalmia/tree/main/docs/plugin/README.md)), use symbol '#' to split entrypoint method.

The entrypoint must receive a  ``KalmiaLaunchConfig`` instance or ``Array<String>`` argument or empty parameter, and must annotated by ``@JvmStatic``, it also must is an kotlin object instead of kotlin class.

The ``entry`` name can be other anything, just modify the name declare before '#' symbol.

Bootstrap will automatically invoke this entrypoint and executes your code.

## When missing
If ``entrypoint`` config are missing or defined to empty:
`````json
{
    "entrypoint": ""
}
`````

Kalmia will automatically reset it to ``com.github.kusa233.kalmia.server.network.http.entrypoint.KalmiaHttpServerEntrypoint#entry``, an asset manager web server, you may need to configure the ``asset_path``, ``error_page`` and other configs in ``configs/kalmia_http.json``.

