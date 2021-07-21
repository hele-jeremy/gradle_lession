package com.jlpay.cart;

import android.app.Application;

import com.jeremy.appdelegate.anontation.ModuleComponent;

@ModuleComponent(priority = 4)
public class CartApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
    }
}
