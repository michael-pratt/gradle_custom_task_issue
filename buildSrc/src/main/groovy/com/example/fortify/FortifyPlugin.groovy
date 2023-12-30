package com.example.fortify

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.api.tasks.Exec
import org.gradle.api.tasks.TaskState
import org.gradle.api.tasks.compile.JavaCompile
import org.gradle.internal.reflect.Instantiator

import javax.inject.Inject

class FortifyPlugin implements Plugin<Project> {


    final Instantiator instantiator

    @Inject
    FortifyPlugin(Instantiator instantiator) {
        this.instantiator = instantiator
    }

    @Override
    void apply(Project project) {
        project.getLogger().debug("Enabling Fortify plugin for project {}", project.getName())

        DefaultFortifyExtensionContainer fortifyContainer =
                project.extensions.create("fortify", DefaultFortifyExtensionContainer.class,
                        instantiator, project)


        fortifyContainer.whenObjectAdded { DefaultFortifyExtension fortify ->
            buildFortifyNamedTask(project, fortify)
        }

    }

    Task buildFortifyNamedTask(Project project, DefaultFortifyExtension fortify) {

        // Create the top level scan task to run post compile
        final Task scanTask = project.tasks.create("fortify${fortify.name}Scan", Exec.class, { Exec task ->
            task.doFirst { Exec first ->
                project.mkdir(fortify.scanPhase.outputDir)

                first.executable = fortify.executable
                first.args = fortify.scanPhase.args.split() as List<?>
            }
            task.doFirst { Exec first ->
                project.logger.info("Executing ${first.path} with args ${first.args}")
            }
        })

        // For all projects, if it has a compile Java Task install a fortify compile task to
        // run after it. However the fortify compile task should run only in the event
        // that the Java task did work and has source files.
        // Make sure that the scan task depends on this compile task.
        project.allprojects.each { subproject ->
            subproject.tasks.whenTaskAdded { subprojectAddedTask ->

                if(subprojectAddedTask.name.equals("compileJava") &&
                    subprojectAddedTask instanceof JavaCompile) {

                    JavaCompile javaCompile = subprojectAddedTask

                    final Task fortifyCompileTask = subproject.tasks.create("fortify${fortify.name}Compile", Exec.class, { Exec task ->
                        task.doFirst {
                            task.executable = fortify.executable
                            task.args = (fortify.compilePhase.args.split() + ["-cp", javaCompile.classpath.asPath] + javaCompile.source.files) as List<?>
                        }
                        task.doFirst {
                            project.logger.info("Executing ${task.path} with args ${task.args}")
                        }

                        task.mustRunAfter(javaCompile)

                        task.onlyIf { javaCompile.didWork && javaCompile.source.files }
                    })

                    scanTask.dependsOn(fortifyCompileTask)
                }
            }
        }

        fortify.task = scanTask

        return project.tasks.create("fortify${fortify.name}", { task ->
            task.description = "Fortify task for ${fortify.name}"
        }).dependsOn(scanTask)
    }
}
