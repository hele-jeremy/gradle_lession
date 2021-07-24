package com.jeremy.gradle_lession;

import android.app.Application;

import com.jlpay.delegate.anontation.AppComponent;

@AppComponent
public class MainApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
    }
}
