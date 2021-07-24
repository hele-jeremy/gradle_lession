package com.jlpay.appdelegate.util

import java.util.jar.JarEntry
import java.util.jar.JarFile;

class TransformUtil {

    static File INSERT_BYTE_CODE_CLASS_FILE
    static ArrayList<String> TRANSFORM_CLASS_NAME_LIST

    static boolean shouldProcessPreDexJar(String path) {
        return !path.contains("com.android.support") && !path.contains("/android/m2repository")
    }

    /**
     * 在jar包中找指定的类
     * @param src
     * @param dest
     */
    static List<String> scanJar(File src, File dest) {
        List<String> list = new ArrayList<>()
        if (src) {
            def jarFiles = new JarFile(src)
            def entries = jarFiles.entries()
            while (entries != null && entries.hasMoreElements()) {
                JarEntry element = entries.nextElement()
                def name = element.getName()

                if (TransConstans.INSERT_BYTE_CODE_CLASS_FILE_NAME == name) {
                    //找到需要进行字节码插桩的类的jar包
                    // 标记这个jar包包含 AppLifecycleDelegate.class
                    //后续扫描结束后，我们会生成注册代码到这个jar文件里
                    INSERT_BYTE_CODE_CLASS_FILE = dest
                } else if (name.startsWith(TransConstans.PACKAGE_OF_GENERATE_APPDELEGATE_FILE)) {
                    //找到我们注解自动生成的类
                    list.add(name.substring(name.lastIndexOf("/") + 1))
                }
            }

            jarFiles.close()
        }

        return list
    }


    static boolean isTargetClass(File file) {
        Logger.i("isTargetClass : " + file.name)
        if (file != null && file.name.startsWith(TransConstans.MODULE_NAME_PREFIX) && file.name.endsWith(TransConstans.MODULE_NAME_SUFFIX)) {
            return true
        }
        return false
    }

}
