package com.jeremy.gradle_lession;

import android.content.Context;
import android.content.res.Configuration;
import android.util.Log;

import androidx.annotation.NonNull;

import com.jeremy.appdelegate.AppLifecycleDelegate;
import com.jlpay.delegate.anontation.AppComponent;

import java.io.Serializable;

@AppComponent
public class MainApplication extends AppBase implements Serializable {

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        Log.d("AppDelegate", "attachBaseContext: MainApplication ->  " + base);
//        AppLifecycleDelegate.get().attachBaseContext(base);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("AppDelegate", "onCreate: MainApplication");
//        AppLifecycleDelegate.get().onCreate();
    }

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }
}
