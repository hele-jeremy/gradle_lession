package com.jlpay.appdelegate

import com.android.build.gradle.AppExtension
import com.android.build.gradle.AppPlugin
import com.jlpay.appdelegate.util.TransformUtil
import org.gradle.api.Plugin
import org.gradle.api.Project
import com.jlpay.appdelegate.util.Logger

class AppDelegatePlugin implements Plugin<Project> {


    @Override
    void apply(Project project) {
        //当前模块是否是com.android.application模块
        def isApp = project.plugins.hasPlugin(AppPlugin)
        if (isApp) {
            Logger.i("start ----------- registering AppDelegateTransform --------------")
            def android = project.extensions.getByType(AppExtension)
            //注册transform任务
            def appDelegateTransform = new AppDelegateTransform(project)
            android.registerTransform(appDelegateTransform)
            Logger.i("end ----------- registering AppDelegateTransform --------------")
        }

    }
}
