package com.jeremy.gradle_lession;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.jlpay.delegate.anontation.AppComponent;

@AppComponent
public class MainApplication2 extends Application {

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


}
