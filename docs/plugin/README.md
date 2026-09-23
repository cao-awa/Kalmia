# Custom plugin

Kalmia allows plugin load, you can only depend on Kalmia, and write code:

```kotlin
package com.xxx.plugin.entry

import com.github.kusa233.kalmia.launch.config.KalmiaLaunchConfig

object TestPlugin {
    @JvmStatic
    fun entry(config: KalmiaLaunchConfig) {
        // Write your codes here!
    }
}
```

and write plugin metadata in ``META-INF/plugin.json``:

```json
{
    "name": "test-plugin",
    "entrypoint": "com.xxx.plugin.entry.TestPlugin#entry"
}
```

then build jar and put into ``libs/`` directory, your plugin can be automatically load by Kalmia, your codes in ``entry``
method will be executed.

Certainly, you also need to define your plugin in ``launch.json``, you can use your plugin name or full method location
to define entrypoint:

```json
{
    "entrypoint": [
        "test-plugin"
    ]
}
```

or

```json
{
    "entrypoint": [
        "com.xxx.plugin.entry.TestPlugin#entry"
    ]
}
```

## Notice
Resident services like http server or other resident services must call ``KalmiaStatus.registerLifecycle``,  ``KalmiaStatus.registerReloadListener``, ``KalmiaStatus.registerStopListener``, and when stopped, must call ``KalmiaStatus.completedLifecyle``.

If plugin is only simple logics, don't register the lifecycles.

## Method requirement

The plugin entrypoint must in a kotlin object class instead of kotlin class, and must annotate with ``@JvmStatic``,
optionally, input arg can choose 3 different way:

```kotlin
package com.xxx.plugin.entry

import com.github.kusa233.kalmia.launch.config.KalmiaLaunchConfig

object TestPlugin {
    @JvmStatic
    fun entrypoint1(config: KalmiaLaunchConfig) {
        // First choose, got a launching config.
    }

    @JvmStatic
    fun entrypoint2(args: Array<String>) {
        // Second choose, got the main entrypoint args.
    }

    @JvmStatic
    fun entrypoint3() {
        // Third choose, no anything input.
    }
}
```

If method doesn't match to anyone entrypoint way, Kalmia cannot execute your codes.

## Depends

If your plugin is depended on other plugins, you can write ``depends_on`` in metadata:

```json
{
    "name": "test-plugin",
    "entrypoint": "com.xxx.plugin.entry.TestPlugin#entry",
    "depends_on": [
        "other-plugin-1",
        "other-plugin-2"
    ]
}
```

All depend on plugins must load before your plugin, correctly define:

```json
{
    "entrypoint": [
        "other-plugin-1",
        "other-plugin-2",
        "test-plugin"
    ]
}
```

Incorrectly:

```json
{
    "entrypoint": [
        "test-plugin",
        "other-plugin-1",
        "other-plugin-2"
    ]
}
```

## Fallback
> NOTICE:\
> A plugin can only take once fallback execution, if fallback still errors, Kalmia will fast-fall shutdown.

You can define a fallback method in plugin metadata, when depend on plugins doesn't loaded or your plugin code got an
exception, Kalmia will execute the fallback path:

```json
{
    "name": "test-plugin",
    "entrypoint": "com.xxx.plugin.entry.TestPlugin#entry",
    "fallback": "com.xxx.plugin.entry.TestPlugin#fallback"
}
```

And you must ensure the fallback method is present and satisfy the requirements.

Sample:

```kotlin
package com.xxx.plugin.entry

import com.github.kusa233.kalmia.launch.config.KalmiaLaunchConfig

object TestPlugin {
    @JvmStatic
    fun entry(config: KalmiaLaunchConfig) {
        throw RuntimeException("Error in main entrypoint")
    }

    @JvmStatic
    fun fallback(config: KalmiaLaunchConfig) {
        // Handle others code logic here.
    }
}
```

If stage into fallback path, you can use ``KalmiaLaunchConfig.error()`` got the exception that last time happened, it may help to complete fallback logics.

Or use a special signature method, receive a ``Throwable`` instance to got the exception:
```kotlin
package com.xxx.plugin.entry

import com.github.kusa233.kalmia.launch.config.KalmiaLaunchConfig

object TestPlugin {
    @JvmStatic
    fun entry(config: KalmiaLaunchConfig) {
        throw RuntimeException("Error in main entrypoint")
    }

    fun fallback(cause: Throwable) {
        // Handle others code logic here.
    }
}
```

## Resource cleans
If your plugin has created some static data, your must use ``registerCleaner`` to clear the data when Kalmia reloading:
```kotlin
class Other {
    // Something code here.
    fun close() {
        // Close resources.
    }
}

class TestDataClass {
    companion object {
        private var INSTANCE: Other? = null
        
        fun init() {
            INSTANCE = Other()
            
            registerCleaner("your_cleaner_name") {
                INSTANCE.close()
                // Key point, clear the references.
                INSTANCE = null
            }
        }
    }
}
```
When Kalmia reloading, ``unload`` hook will be trigger, you can write your unload method in plugin metadata: 
```kotlin
package com.xxx.plugin.entry

object TestPlugin {

    // NOTICE:
    // Unload method doesn't present any input args, can only be empty.
    @JvmStatic
    fun unload() {
        // Your codes here.
    }
}
```

In metadata:
```json
{
    "unload": "com.xxx.plugin.entry.TestPlugin#unload"
}
``` 

And, ``Don't use the 'val' to define an constant that refernce to your custom class``!

## Shared context
You can put and get data from ``KalmiaLaunchConfig``, use ``config[key] = data`` to set data, use ``config[key]`` to get data, data key and value can only is `` String``.

## Repeat definition
Plugin cannot define twice or more times in ``entrypoint``, it cause fast-fall directly.

## Unnamed plugin
You can also don't write ``plugin.json`` metadata, but you need to explicit declare the full method name in ``launch.json``, Kalmia will load it as unnamed plugin.

But if you want to write ``plugin.json``, the ``name`` and ``entrypoint`` is required, ``depends_on`` and ``fallback`` is optional.

