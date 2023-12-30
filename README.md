This repo contains 2 custom Gradle plugins:

- Fortify
- XJC

The XJC plugin contains a custom task named `XkcTask`, which extends `SourceTask`, that generates Java code from XSD files. For brevitiy, the task is a no-op to demonstrate a certain behavior.

The tl;dr of the behavior is that the _Fortify_ plugin is affecting the behavior of the _XJC_ plugin and I'm not sure why.

When you run the following task as-is in the repo, the output should be:

```
./gradlew :schemas:serviceAXjc

> Configure project :schemas
XjcTask constructor: xjc.sourceDirs is null

> Task :schemas:serviceAXjc
XjcTask doStuff: xjc.sourceDirs is [ServiceA, ServiceB]

BUILD SUCCESSFUL in 1s
4 actionable tasks: 4 executed
```

Notice the `null` in the output from the XJC task. When you edit `build.gradle` and disable the _Fortify_ plugin, the output is as follows:

```
./gradlew :schemas:serviceAXjc
XjcTask constructor: xjc.sourceDirs is [ServiceA, ServiceB]

> Task :schemas:serviceAXjc
XjcTask doStuff: xjc.sourceDirs is [ServiceA, ServiceB]

BUILD SUCCESSFUL in 314ms
4 actionable tasks: 1 executed, 3 up-to-date
```

The `null` output is gone, and the `XjcTask` has correctly output the field from the task instead.

The question is: why is the `sourceDirs` field `null` when the _Fortify_ plugin is enabled, but not so when that plugin is disabled?
