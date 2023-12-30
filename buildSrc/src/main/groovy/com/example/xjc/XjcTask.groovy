package com.example.xjc

import com.sun.tools.xjc.Driver
import org.gradle.api.DefaultTask
import org.gradle.api.Project
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.tasks.InputDirectory
import org.gradle.api.tasks.InputFile
import org.gradle.api.tasks.Internal
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.SourceTask
import org.gradle.api.tasks.TaskAction

import javax.inject.Inject


/**
 * A gradle task for generating .java source code files from
 * a set of .xsd and .jxb files using the XJC tool.
 */
public class XjcTask extends SourceTask {

    @Internal
    DefaultXjc xjc

    @Inject
    XjcTask(DefaultXjc xjc, Project project) {
        this.xjc = xjc

	getLogger().lifecycle("XjcTask constructor: xjc.sourceDirs is ${xjc.sourceDirs}")

        source(project.layout.projectDirectory)
    }

    @TaskAction
    void doStuff() {
        getLogger().lifecycle("XjcTask doStuff: xjc.sourceDirs is ${xjc.sourceDirs}")
    }
}

