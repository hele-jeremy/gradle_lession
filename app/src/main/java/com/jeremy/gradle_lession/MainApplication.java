package com.jeremy.gradle_lession;

import android.content.Context;
import android.content.res.Configuration;
import android.util.Log;

import com.jlpay.base.BaseApplication;
import com.jlpay.delegate.anontation.AppComponent;

import java.io.Serializable;

@AppComponent
public class MainApplication extends BaseApplication implements Serializable {


    //        AppLifecycleDelegate.get().attachBaseContext(base);
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        Log.d("AppDelegate", "attachBaseContext: MainApplication ->  " + base);
    }


    //        AppLifecycleDelegate.get().onCreate();
    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("AppDelegate", "onCreate: MainApplication");
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }


}
