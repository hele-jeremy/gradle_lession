package com.jlpay.cart;


import android.content.Context;
import android.content.res.Configuration;
import android.util.Log;

import com.jeremy.appdelegate.IAppLifecycleDelegate;
import com.jlpay.delegate.anontation.ModuleComponent;

@ModuleComponent
public final class CartTestApplication implements IAppLifecycleDelegate {
    @Override
    public void attachBaseContext(Context base) {
        Log.d("AppDelegate", "attachBaseContext: CartTestApplication -> " + base);
    }

    @Override
    public void onCreate() {
        Log.d("AppDelegate", "onCreate: CartTestApplication");
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
