package com.jeremy.plugin

import com.android.build.gradle.AppExtension
import org.gradle.api.Plugin
import org.gradle.api.Project

public class LifeCyclePlugin implements Plugin<Project> {

    @Override
    void apply(Project project) {
        System.out.print("<<<<<自定义LifeCyclePlugin打印>>>>>")

        def android = project.extensions.getByType(AppExtension)

        println(" ----------- registering LifecycleTransform --------------")

        LifeCycleTransform lifeCycleTransform = new LifeCycleTransform()
        android.registerTransform(lifeCycleTransform)
    }
}