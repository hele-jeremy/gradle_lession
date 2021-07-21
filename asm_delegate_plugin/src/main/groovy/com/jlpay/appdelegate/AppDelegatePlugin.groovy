package com.jlpay.appdelegate

import com.android.build.gradle.AppExtension
import org.gradle.api.Plugin
import org.gradle.api.Project

public class AppDelegatePlugin implements Plugin<Project> {


    @Override
    void apply(Project project) {
        System.out.println("start ----------- registering AppDelegateTransform --------------")
        def android = project.extensions.getByType(AppExtension)
        //注册transform任务
        AppDelegateTransform appDelegateTransform = new AppDelegateTransform(project)
        android.registerTransform(appDelegateTransform)
        System.out.println("end ----------- registering AppDelegateTransform --------------")
    }
}
