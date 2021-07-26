package com.jlpay.appdelegate.util;

class TransConstans {
    //transform任务名
    static final String TRANSFORM_NAME = "AppDelegateTransform"
    static final String CLAZZ = ".class"
    //扫描类需要查找的指定的包名
    static final String PACKAGE_OF_GENERATE_APPDELEGATE_FILE = "com/jlpay/appdelegate/template"
    //要进行字节码插桩的类
    static final String INSERT_BYTE_CODE_CLASS_FILE_NAME = "com/jeremy/appdelegate/AppLifecycleDelegate" + CLAZZ

    static final String SEPARATOR = "\$\$"
    //生成类 类名的前缀
    static final String MODULE_NAME_PREFIX = "Jlpay" + SEPARATOR;
    //生成类 类名的后缀
    static final String MODULE_NAME_SUFFIX = SEPARATOR + "AppDelegate" + CLAZZ

    //字节码插入的目标方法
    static final String HACK_INIT_METHOD = "loadModuleComponentsInfo"
    //扫描类需要实现的接口
    public static final String SCAN_CLASS_MATCH_INTERFACE  = "com/jeremy/appdelegate/ILifeDelegateGroup"
}
