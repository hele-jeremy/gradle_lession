package com.jlpay.appdelegate.util

import com.jlpay.asm.ScanAppComponentClassVisitor
import com.jlpay.asm.ScanClassVisitor
import org.apache.commons.lang3.StringUtils
import org.objectweb.asm.ClassReader
import org.objectweb.asm.Opcodes

import java.util.jar.JarEntry
import java.util.jar.JarFile

class TransformUtil {
    //要插桩子节码的文件
    static File INSERT_BYTE_CODE_CLASS_FILE
    public static ArrayList<String> SCAN_APT_GENERATE_CLASS_LIST = new ArrayList<>()
    public static Map<File, List<String>> SCAN_APPCOMPONENT_MARK_CLASS = new HashMap<>()

    /**
     * 过滤掉不需要扫描的jar包
     * @param path
     * @return
     * /android/m2repository 这个目录是android sdk提供support以及官方三房库的本地仓库地址不需要进行扫描
     */
    static boolean shouldProcessPreDexJar(String path) {
        return !path.contains("androidx.") && !path.contains("/android/m2repository")
    }

    static boolean shouldProcessClass(String path) {
        return StringUtils.isNotEmpty(path) && path.startsWith(TransConstans.PACKAGE_OF_GENERATE_APPDELEGATE_FILE) && path.endsWith(TransConstans.CLAZZ)
    }


    static boolean shouldScanAppcomponnetClass(String path) {
        return StringUtils.isNotEmpty(path) && path.endsWith(TransConstans.CLAZZ)
    }


    /**
     * 在jar包中找指定的类
     * @param src
     * @param dest
     */
    static void scanJar(File src, File dest) {
        if (src) {
            //构建Jar包类型的文件对象
            def jarFiles = new JarFile(src)
            //遍历jar包中的文件
            def entries = jarFiles.entries()
            while (entries != null && entries.hasMoreElements()) {
                JarEntry element = entries.nextElement()
                def name = element.getName()
                if (TransConstans.INSERT_BYTE_CODE_CLASS_FILE_NAME == name) {
                    //找到需要进行字节码插桩的类的jar包
                    // 标记这个jar包包含 AppLifecycleDelegate.class
                    //后续扫描结束后，我们会生成注册代码到这个jar包里
                    INSERT_BYTE_CODE_CLASS_FILE = dest
                } else if (name.startsWith(TransConstans.PACKAGE_OF_GENERATE_APPDELEGATE_FILE)) {
                    //找到我们注解自动生成的类
                    InputStream inputStream = jarFiles.getInputStream(element)
                    scanAptGenerateClass(inputStream)
                    inputStream.close()
                }

                //扫描所有的标记了@AppComponent注解的类
                Logger.i("before scanAppComponentClass -> " + name)
                //有些jar包中可能包含元信息类似于 META-INF/这样的文件夹需要过滤掉这种文件
                //apt注解工程的 jar包可能扫描就不全都是 class文件 需要 注意
                if (name.endsWith(TransConstans.CLAZZ)) {
                    Logger.i("after scanAppComponentClass -> " + name)
                    //扫描标记了@AppComponent注解的类
                    InputStream scanAppComponentStream = jarFiles.getInputStream(element)
                    scanAppComponentClass(scanAppComponentStream, dest)
                    scanAppComponentStream.close()
                }

            }
            jarFiles.close()
        }
    }

    /**
     * 通过ASM扫描指定符合条件的类
     * @param inputStream
     * @param list
     */
    static void scanAptGenerateClass(InputStream inputStream) {
        ClassReader classReader = new ClassReader(inputStream)
//        ClassWriter classWriter = new ClassWriter(classReader, ClassWriter.COMPUTE_MAXS)
//        classReader.accept(new ScanClassVisitor(Opcodes.ASM5), ClassReader.EXPAND_FRAMES)
        classReader.accept(new ScanClassVisitor(Opcodes.ASM5), 0)
        inputStream.close()
    }

    static void scanAptGenerateClass(File file) {
        scanAptGenerateClass(new FileInputStream(file))
    }


    static void scanAppComponentClass(InputStream inputStream, File target) {
        ClassReader reader = new ClassReader(inputStream)
//        ClassWriter classWriter = new ClassWriter(reader, ClassWriter.COMPUTE_MAXS)
//        reader.accept(new ScanAppComponentClassVisitor(Opcodes.ASM5), ClassReader.EXPAND_FRAMES)
        reader.accept(new ScanAppComponentClassVisitor(Opcodes.ASM5, target), 0)
        inputStream.close()
    }

    static void scanAppComponentClass(File file, File target) {
        scanAppComponentClass(new FileInputStream(file), target)
    }

}
