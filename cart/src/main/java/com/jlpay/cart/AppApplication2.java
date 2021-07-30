package com.jlpay.cart;

import android.app.Application;
import android.content.res.Configuration;
import android.util.Log;

import androidx.annotation.NonNull;

import com.jlpay.base.BaseApplication;
import com.jlpay.delegate.anontation.AppComponent;

@AppComponent
public class AppApplication2 extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("AppDelegate", "onCreate: AppApplication2  " + BaseApplication.getINSTANCE());
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
    }
}
