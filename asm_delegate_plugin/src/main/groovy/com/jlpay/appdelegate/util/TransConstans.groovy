package com.jlpay.appdelegate.util;

class TransConstans {
    //transform任务名
    static final String TRANSFORM_NAME = "AppDelegateTransform"
    static final String CLAZZ = ".class"
    //扫描类需要查找的指定的包名
    static final String PACKAGE_OF_GENERATE_APPDELEGATE_FILE = "com/jlpay/appdelegate/template"
    public static final String INSERT_BYTE_CODE_CLASS_PATH = "com/jeremy/appdelegate/AppLifecycleDelegate"
    //要进行字节码插桩的类
    static final String INSERT_BYTE_CODE_CLASS_FILE_NAME = INSERT_BYTE_CODE_CLASS_PATH + CLAZZ
    //字节码插入的目标方法
    public static final String HACK_INIT_METHOD = "loadModuleComponentsInfo"
    //扫描类需要实现的接口
    public static final String SCAN_CLASS_MATCH_INTERFACE = "com/jeremy/appdelegate/ILifeDelegateGroup"
}
