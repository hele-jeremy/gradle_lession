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


    //https://blog.csdn.net/weixin_48470176/article/details/107476714
    //https://blog.csdn.net/qq_35410620/article/details/100559319
    //TreeSet比较数据丢失的问题
    @Override
    public int compareTo(DelegateMeta delegateMeta) {
        try {
            int ret = this.priority - delegateMeta.getPriority();
            if (ret > 0 || ret == 0) {
                return 1;
            } else {
                return -1;
            }
//            return this.priority - delegateMeta.getPriority(); //两个值比较如果priority的值相同的情况下返回0可能会有数据丢失的问题
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