package com.jlpay.delegate;

public final class DelegateMeta implements Comparable<DelegateMeta> {

    private int priority = 100;
    private Class<?> appDelegateClazz;

    public DelegateMeta(int priority, Class<?> appDelegateClazz) {
        this.priority = priority;
        this.appDelegateClazz = appDelegateClazz;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public Class<?> getAppDelegateClazz() {
        return appDelegateClazz;
    }

    public void setAppDelegateClazz(Class<?> appDelegateClazz) {
        this.appDelegateClazz = appDelegateClazz;
    }

    @Override
    public int compareTo(DelegateMeta delegateMeta) {
        return this.priority = delegateMeta.getPriority();
    }

    @Override
    public String toString() {
        return "DelegateMeta{" +
                "priority=" + priority +
                ", appDelegateClazz=" + appDelegateClazz +
                '}';
    }
}