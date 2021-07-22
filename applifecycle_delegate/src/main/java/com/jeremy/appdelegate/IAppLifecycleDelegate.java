package com.jeremy.appdelegate;

import android.content.Context;
import android.content.res.Configuration;

public interface IAppLifecycleDelegate {
    void attachBaseContext(Context base);
    void onCreate();
    void onTrimMemory(int level);
    void onLowMemory();
    void onTerminate();
    void onConfigurationChanged(Configuration newConfig);
}
