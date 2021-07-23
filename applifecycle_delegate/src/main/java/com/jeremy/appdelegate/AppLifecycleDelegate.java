package com.jeremy.appdelegate;

import android.app.Application;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.res.Configuration;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class AppLifecycleDelegate {

    private List<Application> mApplications = new ArrayList<Application>();

    private AppLifecycleDelegate() {
    }


    private static final class SingletonHolder {
        private static final AppLifecycleDelegate INSTANCE = new AppLifecycleDelegate();
    }

    public static AppLifecycleDelegate get() {
        return SingletonHolder.INSTANCE;
    }

    public List<Application> getApplications() {
        return mApplications;
    }

    /**
     * 通过反射调用attachBaseContext方法
     *
     * @param base
     */
    public void attachBaseContext(Context base) {
        for (Application app : mApplications) {
            try {
                Class<ContextWrapper> contextWrapper = ContextWrapper.class;
                Method attachBaseContextMethod = contextWrapper.getDeclaredMethod("attachBaseContext", Context.class);
                attachBaseContextMethod.setAccessible(true);
                attachBaseContextMethod.invoke(app, base);
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }catch (IllegalAccessException e){
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
    }


    public void onCreate() {
        for (Application app : mApplications) {
            app.onCreate();
        }
    }


    public void onTrimMemory(int level) {
        for (Application app : mApplications) {
            app.onTrimMemory(level);
        }
    }


    public void onLowMemory() {
        for (Application app : mApplications) {
            app.onLowMemory();
        }
    }


    public void onTerminate() {
        for (Application app : mApplications) {
            app.onTerminate();
        }
    }


    public void onConfigurationChanged(Configuration newConfig) {
        for (Application app : mApplications) {
            app.onConfigurationChanged(newConfig);
        }
    }
}
