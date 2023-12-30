package com.example.fortify

import groovy.transform.Canonical
import org.gradle.api.Action
import org.gradle.api.Named
import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.api.artifacts.Configuration
import org.gradle.api.file.FileTree
import org.gradle.api.internal.AbstractNamedDomainObjectContainer
import org.gradle.api.internal.plugins.DslObject
import org.gradle.api.plugins.JavaPlugin
import org.gradle.api.tasks.SourceSet

import java.text.SimpleDateFormat

@Canonical
class DefaultFortifyExtension  implements Named {

    String executable = "sourceanalyzer"
    String jdk = "1.8"
    String name
    String version

    def buildId

    FortifyCompilePhaseExtension compilePhase

    FortifyScanPhaseExtension scanPhase

    Task task

    DefaultFortifyExtension(String name, Project project) {
        this.name = name.toUpperCase()
        this.version = formatTimeStamp()
        buildId = "${this.name}-Fortify-${version}"

        this.compilePhase = new FortifyCompilePhaseExtension(this, project)
        this.scanPhase = new FortifyScanPhaseExtension(this, project)
    }

    static def formatTimeStamp() {
        new SimpleDateFormat("yyyyMMdd-HHmm").format(new Date())
    }

    Task getTask() {
        return task
    }

    void setTask(Task task) {
        this.task = task
    }

    @Canonical
    class FortifyCompilePhaseExtension {
        String args

        FortifyCompilePhaseExtension(DefaultFortifyExtension fortifyExtension, Project project) {
            args = "-source ${fortifyExtension.jdk} -b ${fortifyExtension.buildId}"
        }
    }

    @Canonical
    class FortifyScanPhaseExtension {
        String args

        String outputDir
        String outputFile

        FortifyScanPhaseExtension(DefaultFortifyExtension fortifyExtension, Project project) {
            outputDir = "${project.getRootProject().getBuildDir()}${File.separator}reports"
            outputFile = "${fortifyExtension.buildId}.fpr"
            String outputFileFullPath = "${outputDir}${File.separator}${outputFile}"

            args = "-disable-source-bundling -source ${fortifyExtension.jdk} -b ${fortifyExtension.buildId} -scan -f ${outputFileFullPath}"
        }
    }
}
