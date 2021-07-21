package com.jlpay.base;

import android.app.Application;

public class BaseApplication extends Application {

    public static Application INSTANCE;


    @Override
    public void onCreate() {
        super.onCreate();
        INSTANCE = (Application)getApplicationContext();
    }
}
