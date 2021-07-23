package com.jeremy.appdelegate;


import java.util.TreeSet;

public class TestAppLifecycleDelegate implements ILifeDelegateGroup {

    @Override
    public void collect(TreeSet<DelegateMeta> delegateMetaList) {
        delegateMetaList.add(DelegateMeta.newBuild(1, new TestApp()));
    }
}
