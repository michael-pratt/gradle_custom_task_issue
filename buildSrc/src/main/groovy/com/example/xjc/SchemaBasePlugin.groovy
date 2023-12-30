package com.example.xjc

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.BasePlugin
import org.gradle.api.plugins.JavaBasePlugin

class SchemaBasePlugin implements Plugin<Project> {

    @Override
    void apply(Project project) {
        project.pluginManager.apply(BasePlugin.class)
        project.pluginManager.apply(JavaBasePlugin.class)

        project.configurations.create("compile") {
            visible = false
            transitive = true
        }

    }
}
