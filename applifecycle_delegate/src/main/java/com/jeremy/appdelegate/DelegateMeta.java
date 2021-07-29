package com.jeremy.appdelegate;

public final class DelegateMeta implements Comparable<DelegateMeta> {

    //执行的优先级，值越小优先级越高
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
        try {
            int ret = this.priority - delegateMeta.getPriority();
            return  ret == 0 ? 1 : ret;
//            return this.priority - delegateMeta.getPriority();
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public String toString() {
        return "DelegateMeta{" +
                "priority=" + priority +
                ", appDelegate=" + appDelegate +
                '}';
    }
}