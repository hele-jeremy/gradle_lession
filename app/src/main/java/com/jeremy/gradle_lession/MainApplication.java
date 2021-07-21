package com.jeremy.gradle_lession;

import android.app.Application;

import com.jeremy.appdelegate.anontation.AppComponent;

@AppComponent
public class MainApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
    }
}
