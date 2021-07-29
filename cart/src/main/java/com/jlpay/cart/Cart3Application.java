package com.jlpay.cart;

import android.content.Context;
import android.content.res.Configuration;
import android.util.Log;

import com.jeremy.appdelegate.IAppLifecycleDelegate;
import com.jlpay.delegate.anontation.ModuleComponent;

@ModuleComponent(priority = 3)
public class Cart3Application implements IAppLifecycleDelegate {
    @Override
    public void attachBaseContext(Context base) {
        Log.d("AppDelegate", "attachBaseContext: Cart3Application -> " + base);
    }

    @Override
    public void onCreate() {
        Log.d("AppDelegate", "onCreate: Cart3Application");
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
