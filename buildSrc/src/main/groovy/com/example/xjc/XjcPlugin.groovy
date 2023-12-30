package com.example.xjc

import com.sun.tools.xjc.Driver
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.api.internal.file.FileOperations
import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.api.tasks.SourceTask
import org.gradle.api.tasks.TaskProvider
import org.gradle.api.tasks.compile.JavaCompile
import org.gradle.internal.reflect.Instantiator
import org.gradle.jvm.tasks.Jar

import javax.inject.Inject

class XjcPlugin implements Plugin<Project> {

    private final Instantiator instantiator
    private final FileOperations fileOperations


    JavaPluginExtension javaConvention

    @Inject
    XjcPlugin(Instantiator instantiator, FileOperations fileOperations)
    {
        this.instantiator = instantiator
        this.fileOperations = fileOperations
    }

    @Override
    void apply(Project project) {
        project.pluginManager.apply(SchemaBasePlugin.class)
        javaConvention = project.getExtensions().getByType(JavaPluginExtension.class)

        DefaultXjcContainer xjcContainer = project.extensions.create("xjc",
                DefaultXjcContainer.class, instantiator, fileOperations)

        def defaultConfiguration = project.configurations.named("default")

        def assembleTask = project.tasks.named("assemble")

        xjcContainer.configureEach { xjc ->
            def configuration = project.configurations.create(xjc.name)
            defaultConfiguration.get().extendsFrom = defaultConfiguration.get().extendsFrom + [configuration]
            TaskProvider<XjcTask> jarTask = buildTasks(xjc, project)
            assembleTask.get().dependsOn(jarTask)
            project.artifacts.add(configuration.name, jarTask)
        }
    }

    def buildTasks(DefaultXjc xjc, Project project) {

        // Task: Convert .xsd and .jxb files into .java files using
        // the XJC tool
        //
        // Inputs: .xsd, .jxb files
        // Outputs: .java files
        return project.tasks.register("${xjc.name}Xjc", XjcTask.class, xjc, project)

    }
}

