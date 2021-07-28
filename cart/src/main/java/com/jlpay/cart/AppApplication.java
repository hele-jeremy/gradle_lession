package com.jlpay.cart;

import android.app.Application;
import android.content.Context;

import com.jlpay.delegate.anontation.AppComponent;

@AppComponent
public class AppApplication extends Application {

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }
}
