package com.jeremy.gradle_lession;

import android.content.Context;
import android.content.res.Configuration;
import android.util.Log;

import com.jeremy.appdelegate.IAppLifecycleDelegate;
import com.jlpay.base.BaseApplication;
import com.jlpay.delegate.anontation.ModuleComponent;


@ModuleComponent(priority = 1)
public class ModuleApplication implements IAppLifecycleDelegate {
    @Override
    public void attachBaseContext(Context base) {
        Log.d("AppDelegate", "attachBaseContext: ModuleApplication -> " + base);
    }

    @Override
    public void onCreate() {
        Log.d("AppDelegate", "onCreate: ModuleApplication  " + BaseApplication.getINSTANCE());
    }

    @Override
    public void onTrimMemory(int level) {

    }

    @Override
    public void onLowMemory() {

    }

    @Override
    public void onTerminate() {

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {

    }
}
