package com.example.xjc

import org.gradle.api.Action
import org.gradle.api.Named
import org.gradle.api.file.CopySpec

public class DefaultXjc implements Named {
    String name
    CopySpec contents

    String main
    List<String> sourceDirs

    DefaultXjc(String name, CopySpec contents) {
        this.name = name
        this.contents = contents
    }

    @Override
    String getName() {
        return name
    }

    CopySpec contents(Action<? super CopySpec> action) {
        action.execute(contents)
        return contents
    }
}
