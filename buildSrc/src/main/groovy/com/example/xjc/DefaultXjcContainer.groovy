package com.example.xjc

import org.gradle.api.internal.AbstractNamedDomainObjectContainer
import org.gradle.api.internal.CollectionCallbackActionDecorator
import org.gradle.api.internal.file.FileOperations
import org.gradle.internal.reflect.Instantiator

class DefaultXjcContainer extends AbstractNamedDomainObjectContainer<DefaultXjc> {
    private final FileOperations fileOperations

    DefaultXjcContainer(Instantiator instantiator, FileOperations fileOperations) {
        super(DefaultXjc.class, instantiator, CollectionCallbackActionDecorator.NOOP)
        this.fileOperations = fileOperations
    }

    @Override
    protected DefaultXjc doCreate(String s) {
        return this.getInstantiator().newInstance(DefaultXjc.class,
                s, this.fileOperations.copySpec())
    }
}
