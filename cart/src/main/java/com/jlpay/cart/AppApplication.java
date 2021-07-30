package com.jlpay.cart;

import android.content.Context;
import android.util.Log;

import com.jlpay.base.BaseApplication;
import com.jlpay.delegate.anontation.AppComponent;

@AppComponent
public class AppApplication extends BaseApplication {

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("AppDelegate", "onCreate: AppApplication  " + BaseApplication.getINSTANCE());
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }
}
