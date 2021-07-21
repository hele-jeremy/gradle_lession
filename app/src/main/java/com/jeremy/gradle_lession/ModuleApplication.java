package com.jeremy.gradle_lession;

import android.app.Application;

import com.jeremy.appdelegate.anontation.ModuleComponent;


@ModuleComponent(priority = 1)
public class ModuleApplication  extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
    }
}
