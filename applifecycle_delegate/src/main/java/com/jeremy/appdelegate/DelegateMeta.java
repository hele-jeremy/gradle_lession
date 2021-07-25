package com.jeremy.appdelegate;

public final class DelegateMeta implements Comparable<DelegateMeta> {

    private int priority;
    private IAppLifecycleDelegate appDelegate;

    public DelegateMeta(int priority, IAppLifecycleDelegate appDelegate) {
        this.priority = priority;
        this.appDelegate = appDelegate;
    }

    public static DelegateMeta newBuild(int priority, IAppLifecycleDelegate appDelegate) {
        return new DelegateMeta(priority, appDelegate);
    }


    public int getPriority() {
        return priority;
    }

    public IAppLifecycleDelegate getAppDelegate() {
        return appDelegate;
    }

    @Override
    public int compareTo(DelegateMeta delegateMeta) {
        return this.priority = delegateMeta.getPriority();
    }

    @Override
    public String toString() {
        return "DelegateMeta{" +
                "priority=" + priority +
                ", appDelegate=" + appDelegate +
                '}';
    }
}