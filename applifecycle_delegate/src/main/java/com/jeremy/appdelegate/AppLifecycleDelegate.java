package com.jeremy.appdelegate;

import android.content.Context;
import android.content.res.Configuration;

import java.util.TreeSet;

public final class AppLifecycleDelegate {

    private TreeSet<DelegateMeta> mAppDelegateMetas = new TreeSet<>();

    private AppLifecycleDelegate() {
        loadModuleComponentsInfo();
    }

    private static final class SingletonHolder {
        private static final AppLifecycleDelegate INSTANCE = new AppLifecycleDelegate();
    }

    public static AppLifecycleDelegate get() {
        return SingletonHolder.INSTANCE;
    }

    /**
     * 字节码插桩将需要进行生命周期派发的类在该方法中添加到mApplications集合中
     */
    private void loadModuleComponentsInfo() {
        new TestAppLifecycleDelegate().collect(mAppDelegateMetas);
    }


    /**
     * 通过反射调用attachBaseContext方法
     *
     * @param base
     */
    public void attachBaseContext(Context base) {
        for (DelegateMeta meta : mAppDelegateMetas) {
            /*try {
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
            }*/


            meta.getAppDelegate().attachBaseContext(base);
        }
    }


    public void onCreate() {
        for (DelegateMeta meta : mAppDelegateMetas) {
            meta.getAppDelegate().onCreate();
        }
    }


    public void onTrimMemory(int level) {
        for (DelegateMeta meta : mAppDelegateMetas) {
            meta.getAppDelegate().onTrimMemory(level);
        }
    }


    public void onLowMemory() {
        for (DelegateMeta meta : mAppDelegateMetas) {
            meta.getAppDelegate().onLowMemory();
        }
    }


    public void onTerminate() {
        for (DelegateMeta meta : mAppDelegateMetas) {
            meta.getAppDelegate().onTerminate();
        }
    }


    public void onConfigurationChanged(Configuration newConfig) {
        for (DelegateMeta meta : mAppDelegateMetas) {
            meta.getAppDelegate().onConfigurationChanged(newConfig);
        }
    }
}
