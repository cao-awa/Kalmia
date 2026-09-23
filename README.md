# Kalmia

> Kalmia is still evolving.
> The runtime and HTTP stack are already usable for real projects, while the surrounding ecosystem is still under active development.\
> Kalmia focuses on functional programming, structured control flow, minimal reflection.

Kalmia is a Kotlin-first web runtime for developers who prefer
explicit code to annotations and compile-time safety over runtime magic.

Instead of discovering routes through reflection,
Kalmia lets you build APIs as ordinary Kotlin values:

* No annotation routing.
* Minimal reflection.
* Type-safe request extraction.
* Structured control flow.

Kalmia is designed for developers who want correctness, predictability, and explicit behavior over implicit magic, only using reflection during startup to [bootstrap user code](https://github.com/cao-awa/Kalmia/tree/main/docs/entrypoint/README.md).

<img src="https://raw.githubusercontent.com/cao-awa/Kalmia/main/kalmia-icon.png" width="150" alt="Kalmia Icon">

# Why Kalmia?
Kalmia lets you write services as ordinary Kotlin code.

Instead of annotations, reflection and runtime routing discovery,
everything is defined as explicit Kotlin values.

That means:

* Routes are Kotlin values, not runtime registrations.
* Parameters are extracted with types, not strings.
* Control flow is explicit through abort scopes.

## Hot Reload

Kalmia supports reloading without restarting the JVM by **replacing the route graph**, not by redefining loaded classes.

This requires the application to be **recompiled** after code changes. Once a new graph is built, Kalmia atomically swaps it into service:

* Existing requests continue using the old graph until completion.
* New requests are dispatched to the new graph immediately.
* The JVM process itself is never restarted.

This approach keeps reload semantics explicit and predictable while avoiding JVM class redefinition.

# Getting started
Add codes to your ``build.gradle`` file:
```groovy
repositories {
    maven {
        url 'https://jitpack.io'
    }
}

dependencies {
    implementation 'com.github.cao-awa:Kalmia:{kalmia_version}'
}
```

For the versions, see [JitPack](https://jitpack.io/#cao-awa/Kalmia).

Now you're ready to build your first Kalmia application.

# Quick Start
Use ``./gradlew run`` to launch Kalmia quickly, Kalmia will generate a config file on first startup, it seems like:

```json
{
    "print_config_details": true,
    "entrypoint": [
        "com.github.kusa233.kalmia.server.network.http.entrypoint.KalmiaHttpServerEntrypoint#entry"
    ]
}
```

It means Kalmia will call the entrypoint ``com.github.kusa233.kalmia.server.network.http.entrypoint.KalmiaHttpServerEntrypoint#entry``, this entrypoint method starts an asset manager http server (as mentioned above).

It can automatically serve HTML or other files in assets path, or redirect path to "path/index.html" file.

If you are using gradle task to launch Kalmia and want to run your codes, you also need to change the entrypoint configuration, please see [entrypoint document](https://github.com/cao-awa/Kalmia/tree/main/docs/entrypoint/README.md).

Put jars into ``{working_dir}/libs/``, and change the entrypoint config, build your own services.

## Development environment 
For details, see [entrypoint document](https://github.com/cao-awa/Kalmia/tree/main/docs/entrypoint/README.md).

### HTTP server
Example code:
```kotlin
@JvmStatic
fun entry() {
    val username by arg<String>("username")

    val api = http {
        route("/hello") {
            get {
                // Response rendered HTML page.
                html {
                    body {
                        p {
                            +"Hello $username!"
                        }
                    }
                }
            }
        }
    }

    KalmiaHttpServer(api).start()
}
```

Launch Kalmia and open ``http://127.0.0.1:12345/hello?username=Kalmia`` to view the hello page.

For details, see [HTTP document](https://github.com/cao-awa/Kalmia/tree/main/docs/http/README.md).

## Production environment 
Use java command ``java -jar Kalmia-{kalmia_version}.jar -server`` to run a Kalmia http server with [Assets manager mode](https://github.com/cao-awa/Kalmia/tree/main/docs/http/README.md#assets-manager-mode).

# CLI command
See [command document](https://github.com/cao-awa/Kalmia/tree/main/docs/command/README.md).

# View count
![](https://count.getloli.com/@@cao-awa.kalmia?name=%40cao-awa.kalmia&padding=7&offset=0&align=top&scale=1&pixelated=1&darkmode=auto)
