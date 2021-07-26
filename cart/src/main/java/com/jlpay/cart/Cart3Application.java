package com.jlpay.cart;

import android.content.Context;
import android.content.res.Configuration;
import android.util.Log;

import com.jeremy.appdelegate.IAppLifecycleDelegate;
import com.jlpay.delegate.anontation.ModuleComponent;

@ModuleComponent(priority = 129)
public class Cart3Application implements IAppLifecycleDelegate {
    @Override
    public void attachBaseContext(Context base) {

    }

    @Override
    public void onCreate() {
        Log.d("CartAppDelegate", "onCreate: Cart3Application");
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
