# Basic usage

## Foreword

Don't use coroutine in request handler scope, it already running on coroutine scope, do this instead of improving
performance, it actually impacts the behaviors.

## Basic structure

```kotlin
object TestEntry {
    @JvmStatic
    fun entry() {
        val api = http {
            route("/test") {
                get {
                    // Handle request here...
                    println("Something here...")

                    // Return a result to response the request.
                    html {
                        head {
                            charset(StandardCharsets.UTF_8)
                        }
                        body {
                            p {
                                +"Hello world!"
                            }
                        }
                    }
                }
            }
        }

        KalmiaHttpServer(api).start()
    }
}
```

## Request arguments

> Default your server launch on 12345 port here

The 'request argument' is the url contents after '?' such as 'http://127.0.0.1:12345/test?arg=1', the 'arg=1' is a
argument.

Define arg extractor and call it in request scope to get the argument value:

```kotlin
object TestEntry {
    @JvmStatic
    fun entry() {
        val input = arg<String>("input")
        val api = http {
            route("/test") {
                get {
                    val theInput = input(this)

                    // Return a result to response the request.
                    html {
                        head {
                            charset(StandardCharsets.UTF_8)
                        }
                        body {
                            p {
                                +"Input argument is $theInput"
                            }
                        }
                    }
                }
            }
        }

        KalmiaHttpServer(api).start()
    }
}
```

The ``arg`` method has two arguments, first is url argument name, is required, second is missable flag, is optional, if
missable is true, Kalmia will return the default value of the type.

### Default value

When missable flag is true, Kalmia will get the usually default value, such as ``arg<Int>`` is 0, ``arg<String>`` is an
empty string, or ETC.

You can also set custom default value manually:

```kotlin
object TestEntry {
    @JvmStatic
    fun entry() {
        val input = arg<String>("input", true).defaultValue("The input")
        // There same to above-mentioned.
    }
}
```

## Placeholders

The 'placeholder' just like its name, is a part in the url.

Define placeholder extractor and call it in request scope to get the placeholder value:

```kotlin
object TestEntry {
    @JvmStatic
    fun entry() {
        val placeholder = placeholder<String>("username")
        val api = http {
            route("/getUser/{username}") {
                get {
                    val username = placeholder(this)

                    // Return a result to response the request.
                    html {
                        head {
                            charset(StandardCharsets.UTF_8)
                        }
                        body {
                            p {
                                +"The user  is $username"
                            }
                        }
                    }
                }
            }
        }

        KalmiaHttpServer(api).start()
    }
}
```

Unlike ``arg``, it only one name argument, it match to route defined placeholder, therefore, their names must be the
same.

## Delegate way

Use kotlin keyword ``by`` to delegate ``arg`` or ``placeholder``, and use it in your codes, instead of call
``xxx(this)``:

```kotlin
object TestEntry {
    @JvmStatic
    fun entry() {
        val input by arg<Int>("input")
        val placeholder by placeholder<String>("test")

        val api = http {
            route("/test/{test}") {
                get {
                    html {
                        head {
                            charset(StandardCharsets.UTF_8)
                        }
                        body {
                            p {
                                +"Input arg 'input' is '$input', placeholder is '$placeholder'"
                            }
                        }
                    }
                }
            }
        }

        KalmiaHttpServer(api).start()
    }
}
```

> Default your server launch on 12345 port here

When you visit ``http://127.0.0.1:12345/test/awa?input=1234``, you will see a page shown
``Input arg 'input' is '1234', placeholder is 'awa'``

The delegate ``arg`` or ``placeholder`` can only access in request scope, cannot access in other locations, otherwise
Kalmia will throw a ``IllegalStateException`` to notice it.

## Placeholder route
You can use placeholder to create routes, this will save you time from writing ``{xxx}`` several times:

```kotlin
object TestEntry {
    @JvmStatic
    fun entry() {
        val usernameHolder = placeholder<String>("username")
        val functionHolder = placeholder<Int>("function")
        val username by usernameHolder
        val function by functionHolder

        val api = http {
            route("/wiki", usernameHolder, functionHolder) {
                get {
                    html {
                        body {
                            p {
                                +"User'{$username}' currently accessing server function '$function'"
                            }
                        }
                    }
                }
            }
        }

        KalmiaHttpServer(api).start()
    }
}
```

The order of the string and placeholder is arbitrary, you can swap orders it as you like.

Unfortunately, you can't use delegate way to build routes, only raw placeholder can do this, so for future conveniences, you maybe need define a ``val xxx by xxxHolder``, and use ``xxx`` in the next stages, abandon the ``xxxHolder``.

Or maybe you want to use ``xxx(this)`` to get the value, it also ok, the choice is yours.

## Typed arg and typed placeholder builder

Usually custom data class building ways:

```kotlin
object TestEntry {
    @JvmStatic
    fun entry() {
        val username = arg<String>("username", false)
        val password = arg<String>("password", false)

        data class LoginRequest(val username: String, val password: String)

        val api = http {
            route("test") {
                get {
                    val name = username(this)
                    val pws = password(this)
                    val loginRequest = LoginRequest(name, pws)
                    // Call auth plugin codes here...
                }
            }
        }

        KalmiaHttpServer(api).start()
    }
}
```

In Kalmia, you can use ``build`` method to build your custom class in request scope, just input the args and constructor.

And build method can only input most 7 args or placeholders, if your code ned more input, maybe you need to think is
there a problem with your design architecture?

```kotlin
object TestEntry {
    @JvmStatic
    fun entry() {
        val username = arg<String>("username", false)
        val password = arg<String>("password", false)

        data class LoginRequest(val username: String, val password: String)

        val api = http {
            route("test") {
                get {
                    val loginRequest = build(
                        username,
                        password,
                        // Use lambda constructor.
                        ::LoginRequest
                    )
                    // Call auth plugin codes here...
                }
            }
        }

        KalmiaHttpServer(api).start()
    }
}
```

This sample using ``arg``, it can also use in ``placeholder``, but cannot mix uses.

Even you are using delegate(``by``) arg or placeholder, build method is still usable, you can use build like extractor
mode, because build not only ``build(TypedHttpArgument<T1>, TypedHttpArgument<T2> ... TypedHttpArgument<T7>, R)`` and
``build(TypedHttpUrlPlaceholder<T1>, TypedHttpUrlPlaceholder<T2> ... TypedHttpUrlPlaceholder<T7>, R)`` , it also
supports ``build(T1, T2 ... T7, R)``.

## Abort

Kalmia uses a scoped abort model where execution and error handling are strictly separated into non-overlapping lifetimes.

In Kalmia, aborting execution is not an exceptional case.\
It is a first-class, structured control flow with explicit scope boundaries.

The `abortWith()` or `abortIf()` methods define when to abort, and `.ifAbort { }` defines how aborted execution is
rendered into a
response:

```kotlin
object TestEntry {
    @JvmStatic
    fun entry() {
        val http = http {
            route("/test") {
                get {
                    // Simulation code wrongs.
                    abortWith(
                        NullPointerException("Test if logic error occurs NPE"),
                        HttpResponseStatus.BAD_REQUEST,
                        this
                    )
                }.ifAbort(NullPointerException::class) { exception ->
                    LOGGER.error(exception)
                    val httpProtocolVersion: HttpVersion = protocolVersion()
                    LOGGER.error("Http protocol version: $httpProtocolVersion")
                    val status: HttpResponseStatus = status()
                    LOGGER.error("Http status: $status")
                }
            }
        }

        KalmiaHttpServer(http).start()
    }
}
```

The client will receive data similar to:

```json
{
    "error_message": "Test if logic error occurs NPE",
    "stacktrace": [
        "java.lang.NullPointerException: Test if logic error occurs NPE",
        " - at MainKt.testError$lambda$0$0$0(Main.kt:68)",
        " - at com.github.cao.awa.kalmia.server.network.http.handler.KalmiaHttpRequestHandler.handle(KalmiaHttpRequestHandler.kt:42)",
        " - at com.github.cao.awa.kalmia.server.network.http.pipeline.KalmiaHttpRequestPipeline$handleFull$1.invokeSuspend$lambda$0(KalmiaHttpRequestPipeline.kt:107)",
        " - at com.github.cao.awa.kalmia.server.network.pipeline.KalmiaRequestPipeline.abortable(KalmiaRequestPipeline.kt:22)",
        " - at com.github.cao.awa.kalmia.server.network.http.pipeline.KalmiaHttpRequestPipeline$handleFull$1.invokeSuspend(KalmiaHttpRequestPipeline.kt:102)",
        " - at kotlin.coroutines.jvm.internal.BaseContinuationImpl.resumeWith(ContinuationImpl.kt:34)",
        " - at kotlinx.coroutines.DispatchedTask.run(DispatchedTask.kt:100)",
        " - at kotlinx.coroutines.internal.LimitedDispatcher$Worker.run(LimitedDispatcher.kt:124)",
        " - at kotlinx.coroutines.scheduling.TaskImpl.run(Tasks.kt:89)",
        " - at kotlinx.coroutines.scheduling.CoroutineScheduler.runSafely(CoroutineScheduler.kt:586)",
        " - at kotlinx.coroutines.scheduling.CoroutineScheduler$Worker.executeTask(CoroutineScheduler.kt:820)",
        " - at kotlinx.coroutines.scheduling.CoroutineScheduler$Worker.runWorker(CoroutineScheduler.kt:717)",
        " - at kotlinx.coroutines.scheduling.CoroutineScheduler$Worker.run(CoroutineScheduler.kt:704)"
    ],
    "internal_error_name": "Internal Server Error",
    "http_meta": {
        "http_version": "HTTP/1.1"
    },
    "error": "Server protocol (Kalmia/1.0.0, HTTP/1.1) error: Internal Server Error"
}
```

All abort scopes are copied from the source context, which Kalmia automatically collects.\
You can modify the scope data in the abort context.

## Custom combinator

You can use ``combinator`` in ``arg`` or ``placeholder`` creating to define some custom combinate logics, you can have
multiple combinators instead of single combinator, just repeat call ``combinator`` method again.

```kotlin
object TestEntry {
    @JvmStatic
    fun entry() {
        val username = arg<String>("username").combinator { content ->
            if (content.length < 5) {
                abortWith(
                    IllegalArgumentException("Username length must more than 5 characters"),
                    HttpResponseStatus.BAD_REQUEST,
                    this
                )
            }
            content
        }

        val api = http {
            route("/register") {
                get {
                    val name = username(this)
                    println("User $name registered")
                    html {
                        body {
                            p {
                                +"User $name registered now!"
                            }
                        }
                    }
                }
            }
        }

        KalmiaHttpServer(api).start()
    }
}
```

## Headers

Use ``headers()`` to get request headers, every time call ``headers()`` will copy once, don't call repeatedly if it's
not necessary:

```kotlin
object TestEntry {
    @JvmStatic
    fun entry() {
        val api = http {
            route("/test") {
                get {
                    val headers = headers()
                    html {
                        body {
                            p {
                                for ((name, value) in headers) {
                                    +"Header '$name': $value'"
                                }
                            }
                        }
                    }
                }
            }
        }

        KalmiaHttpServer(api).start()
    }
}
```

## Arguments

Use ``arguments`` to get request arguments (after url '?'):

```kotlin
object TestEntry {
    @JvmStatic
    fun entry() {
        val api = http {
            route("/test") {
                get {
                    val arguments = arguments()
                    html {
                        body {
                            p {
                                for ((name, value) in arguments) {
                                    +"Argument '$name': $value'"
                                }
                            }
                        }
                    }
                }
            }
        }

        KalmiaHttpServer(api).start()
    }
}
```

## Body

Use ``body()`` to get request body data, may get one of many types:

```kotlin
object TestEntry {
    @JvmStatic
    fun entry() {
        val api = http {
            route("/test") {
                get {
                    val body = body()
                    html {
                        body {
                            p {
                                when (body) {
                                    is KalmiaHttpEmptyBody -> {}
                                    is KalmiaHttpTextBody -> {}
                                    is KalmiaHttpJsonBody -> {}
                                    is KalmiaHttpUrlencodedBody -> {}
                                    // Or more.
                                    else -> {}
                                }
                            }
                        }
                    }
                }
            }
        }

        KalmiaHttpServer(api).start()
    }
}
```

# Assets manager mode

Use ``-jar Kalmia-{kalmia_version}.jar`` to run a Kalmia HTTP server will automatically running on assets manager mode, if
kalmia running on assets manager mode, when url not fetch (such as ``http://127.0.0.1/test``), then Kalmia will
automatically redirect to ``http://127.0.0.1/test/index.html``, if still not found, finally, it will get an error
response, you can modify ``error_page`` config in ``configs/kalmia_http.json`` config file to custom your 404 page,

The API code equivalent to:

```kotlin
http {
    // Setup static asset path.
    assets(assetManagerConfig.assetPath())

    // Redirect all no registered query to 404 page.
    ifAbort(HttpPathNotRegisteredException::class) {
        withAsset(redirectAsset = assetManagerConfig.errorPage())
    }
}
```

# PHP

Currently, Kalmia can execute PHP scripts through PHP-CGI, but support is incomplete and can currently only be used for
single-file PHP scripts.

Simple sample:

```php
<?php

ob_clean();

echo '<!DOCTYPE html>
<html>
<head><meta charset="UTF-8"><title>CGI Connection test</title></head>
<body>
<h1>CGI Connection test success</h1>
<hr>
<h2>Server information</h2>
<ul>';

$cgi_vars = [
    'SERVER_SOFTWARE'   => 'Web server software',
    'GATEWAY_INTERFACE' => 'CGI Version',
    'REQUEST_METHOD'    => 'Request method',
    'QUERY_STRING'      => 'Query string',
    'SCRIPT_NAME'       => 'Script name',
    'PHP_SELF'          => 'PHP Script path',
];

foreach ($cgi_vars as $key => $desc) {
    $value = $_SERVER[$key] ?? 'No settings';
    echo "<li><strong>{$desc}：</strong> {$value}</li>";
}

echo '<li><strong>PHP Version: </strong> ' . PHP_VERSION . '</li>';
echo '</ul>';

echo '<details>
<summary>Click to view all $_SERVER Variables</summary>
<pre>';
print_r($_SERVER);
echo '</pre>
</details>';

echo '</body></html>';
```

# Structured Responses and HTTP Metadata

By default, Kalmia treats HTTP responses as **structured data**.

When a handler returns a Kotlin object, Kalmia serializes it and **injects HTTP metadata** into the response payload:

But Kalmia does not encourage embedding transport concerns into domain models.\
HTTP metadata injection is a transport-level concern and is configurable.

```json
{
    "type": "post",
    "http_meta": {
        "http_version": "HTTP/1.1",
        "http_status": 200
    }
}
```

This unified response model allows:

* Non-HTTP clients (CLI tools, MQ consumers, test harnesses) to consume responses directly
* Easier debugging and inspection
* Transport-agnostic result handling

Transport metadata is always derived from the response description.\
It never influences handler semantics.

HTTP metadata injection is configurable and can be disabled for stricter HTTP/body separation:

```kotlin
object TestEntry {
    @JvmStatic
    fun entry() {
        // NOTE: Disable HTTP metadata injection ('instructHttpMetadata')
        // This will automatically disable status code and version injection.
        KalmiaHttpServer.instructHttpMetadata = false
        KalmiaHttpServer.instructHttpStatusCode = false
        KalmiaHttpServer.instructHttpVersionCode = false
    }
}
```

Kalmia automatically serializes Kotlin data classes using [Cason](https://github.com/cao-awa/Cason), a lightweight,
type-safe JSON/JSON5 library.

# Total Handlers and 204 No Content

A handler in Kalmia is a total function from request scope to a single response value.\
There is no such thing as a “partially constructed response” in Kalmia.\
It may describe response metadata, but it cannot partially construct a response.

To return `204 No Content`, use `NoContentResponse` explicitly:

```kotlin
object TestEntry {
    @JvmStatic
    fun entry() {
        val api = http {
            route("/test") {
                get {
                    NoContentResponse
                }
            }
        }

        KalmiaHttpServer(api).start()
    }
}
```

Or if a return value is missing, Kalmia will automatically return a `204 NO CONTENT` response.

# Performance

## Benchmark Test

Tested by [OHA](https://crates.io/crates/oha) on an ``Intel I5 10600 CPU``, Ubuntu 24.04, with default settings

With JVM options (`-server -XX:+UseZGC`)

Using the simple test case, assets manager managed 'assets' directory (21kb home page), use ``GET`` to fetch ``http://127.0.0.1:12345``.

| Transport / Memory | 128M      |
|----------|-----------|
| NIO      | 29000 RPS |
| EPOLL    | 37000 RPS |
| IO_URING | 32000 RPS |
