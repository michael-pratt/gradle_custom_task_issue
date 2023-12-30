package com.example.fortify

import org.gradle.api.Project
import org.gradle.api.internal.AbstractNamedDomainObjectContainer
import org.gradle.api.internal.CollectionCallbackActionDecorator
import org.gradle.internal.reflect.Instantiator

class DefaultFortifyExtensionContainer extends AbstractNamedDomainObjectContainer<DefaultFortifyExtension> {

    private final Project project

    DefaultFortifyExtensionContainer(Instantiator instantiator, Project project) {
        super(DefaultFortifyExtension.class, instantiator,  CollectionCallbackActionDecorator.NOOP)
        this.project = project
    }

    @Override
    protected DefaultFortifyExtension doCreate(String s) {
        return this.getInstantiator().newInstance(DefaultFortifyExtension.class,
                s, this.project)
    }
}
