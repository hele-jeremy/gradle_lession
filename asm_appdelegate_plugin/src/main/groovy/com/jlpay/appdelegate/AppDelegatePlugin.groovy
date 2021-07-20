package com.jlpay.appdelegate

import com.android.build.gradle.AppExtension
import org.gradle.api.Plugin
import org.gradle.api.Project

public class AppDelegatePlugin implements Plugin<Project> {


    @Override
    void apply(Project target) {
        System.out.print("AppDelegatePlugin  ---  apply  ----- " + target.name)


        def android = project.extensions.getByType(AppExtension)

        println(" ----------- registering AppDelegateTransform --------------")

        AppDelegateTransform appDelegateTransform = new AppDelegateTransform()
        android.registerTransform(appDelegateTransform)
    }
}
