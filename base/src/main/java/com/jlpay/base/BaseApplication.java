package com.jlpay.base;

import android.app.Application;

public class BaseApplication extends Application {

    private static Application INSTANCE;


    @Override
    public void onCreate() {
        super.onCreate();
        INSTANCE = (Application)getApplicationContext();
    }

    public static Application getINSTANCE() {
        return INSTANCE;
    }
}
