package com.jlpay.appdelegate.util

import com.jlpay.asm.ScanAppComponentClassVisitor
import com.jlpay.asm.ScanClassVisitor
import org.apache.commons.lang3.StringUtils
import org.objectweb.asm.ClassReader
import org.objectweb.asm.ClassWriter
import org.objectweb.asm.Opcodes

import java.util.jar.JarEntry
import java.util.jar.JarFile

class TransformUtil {
    //要插桩子节码的文件
    static File INSERT_BYTE_CODE_CLASS_FILE
    public static ArrayList<String> SCAN_APT_GENERATE_CLASS_LIST = new ArrayList<>()

    /**
     * 过滤掉不需要扫描的jar包
     * @param path
     * @return
     */
    static boolean shouldProcessPreDexJar(String path) {
        return !path.contains("androidx.") && !path.contains("/android/m2repository")
    }

    static boolean shouldProcessClass(String path) {
        return StringUtils.isNotEmpty(path) && path.startsWith(TransConstans.PACKAGE_OF_GENERATE_APPDELEGATE_FILE)
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
                } else {
                    //扫描标记了@AppComponent注解的类
                    InputStream scanAppComponentStream = jarFiles.getInputStream(element)
                    scanAppComponentClass(scanAppComponentStream)
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


    static void scanAppComponentClass(InputStream inputStream) {
        ClassReader reader = new ClassReader(inputStream)
//        ClassWriter classWriter = new ClassWriter(reader, ClassWriter.COMPUTE_MAXS)
//        reader.accept(new ScanAppComponentClassVisitor(Opcodes.ASM5), ClassReader.EXPAND_FRAMES)
        reader.accept(new ScanAppComponentClassVisitor(Opcodes.ASM5), 0)
        inputStream.close()
    }

}
