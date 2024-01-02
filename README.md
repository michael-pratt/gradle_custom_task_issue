Trying to figure out why the Fortify custom plugin causes the `java-module-1` subproject to break with the following error:

```
./gradlew tasks
> Task :tasks FAILED

FAILURE: Build failed with an exception.

* What went wrong:
Execution failed for task ':tasks'.
> Could not create task ':java-module-1:compileJava'.
   > DefaultTaskContainer#create(String, Class, Action) on task set cannot be executed in the current context.
```

This problem does not occure when the Fortify plugin is disabled in `build.gradle`.

The plugin does hook into the `JavaCompile` task for all subprojects, but it's not clear why this causes the above error.

Note that the Fortify plugin uses `configureEach`. When switched to `whenTaskAdded` the above problem goes away, but it causes other issues in the project related to the custom XJC plugin.
