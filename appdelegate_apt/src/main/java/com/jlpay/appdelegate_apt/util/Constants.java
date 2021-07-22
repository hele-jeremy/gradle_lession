package com.jlpay.appdelegate_apt.util;

//常量类
public final class Constants {
    //日志打印tag
    public static final String TAG_LOGGER = "->delegate>";
    //模块gradle文件中配置的参数的key,注解处理器可以根据这个key获取对应的值
    public static final String KEY_COMPONENT_NAME = "DELEGATE_COMPONENT_NAME";
    //@ModuleComponent注解的全路径名称
    public static final String ANNOTATION_MODULECOMPONENT = "com.jlpay.delegate.anontation.ModuleComponent";

}
