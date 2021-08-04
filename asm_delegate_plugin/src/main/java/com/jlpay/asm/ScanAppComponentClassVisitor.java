package com.jlpay.asm;

import com.jlpay.appdelegate.util.Logger;
import com.jlpay.appdelegate.util.TransConstans;
import com.jlpay.appdelegate.util.TransformUtil;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ModuleVisitor;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ScanAppComponentClassVisitor extends ClassVisitor {

    private File mTargetFile;
    private String className;

    public ScanAppComponentClassVisitor(int api, File targetFile) {
        super(api);
        mTargetFile = targetFile;
    }

    /**
     * jdk9编译产物中有 module-info.class 以及在kotlin的项目中（kotlin-standlib)的包中也有module-info.class
     * 使用ASM5访问module-info.class的时候会报错，最低需要ASM6的支持因此暂且在此处返回null,表示不想访问module信息
     * https://blog.csdn.net/haohaounique/article/details/94780353
     * https://www.coder.work/article/7086537
     * @param name
     * @param access
     * @param version
     * @return
     */
    @Override
    public ModuleVisitor visitModule(String name, int access, String version) {
        Logger.i("ScanAppComponentClassVisitor visitModule :  " + name + " " + access + " " + version);
//        return super.visitModule(name, access, version);
        return null;
    }

    @Override
    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
        Logger.i("ScanAppComponentClassVisitor visit :  " + name + " " + Arrays.toString(interfaces) + " " + superName + "  " + version);
        // TODO: 2021/7/28 判断@Appcomponent注解标记的类是否是android.app.Application的子类
        className = name;
        super.visit(version, access, name, signature, superName, interfaces);
    }

    @Override
    public AnnotationVisitor visitAnnotation(String descriptor, boolean visible) {
        Logger.i("ScanAppComponentClassVisitor visitAnnotation :  " + descriptor + " " + visible);
        if (StringUtils.isNotEmpty(descriptor) && StringUtils.equals(TransConstans.SCAN_APPCOMPONENT_PATH, descriptor)) {
            Logger.i("ScanAppComponentClassVisitor visitAnnotation--》 :  " + descriptor + " " + visible);
            Logger.i("ScanAppComponentClassVisitor visitAnnotation--》 :  " + className + " " + mTargetFile.getAbsolutePath());
            if (!TransformUtil.SCAN_APPCOMPONENT_MARK_CLASS.containsKey(mTargetFile)) {
                TransformUtil.SCAN_APPCOMPONENT_MARK_CLASS.put(mTargetFile, new ArrayList<String>() {{
                    add(className);
                }});
            } else {
                List<String> classLists = TransformUtil.SCAN_APPCOMPONENT_MARK_CLASS.get(mTargetFile);
                if (CollectionUtils.isNotEmpty(classLists) && !classLists.contains(className)) {
                    classLists.add(className);
                }
            }
        }
        return super.visitAnnotation(descriptor, visible);
    }
}
