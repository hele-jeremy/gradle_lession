package com.jlpay.asm;


import com.jlpay.appdelegate.util.Logger;
import com.jlpay.appdelegate.util.TransConstans;
import com.jlpay.appdelegate.util.TransformUtil;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.ClassVisitor;

import java.util.Arrays;

public class ScanClassVisitor extends ClassVisitor {


    public ScanClassVisitor(int api) {
        super(api);
    }

    /**
     * 访问类上的注解
     * @param descriptor
     * @param visible
     * @return
     */
    @Override
    public AnnotationVisitor visitAnnotation(String descriptor, boolean visible) {
        Logger.i("scan visitAnnotation :  " + descriptor + " " + visible);
        return super.visitAnnotation(descriptor, visible);
    }

    /**
     * 通过该方法获取到扫描到的每一个类的信息
     *
     * @param version
     * @param access
     * @param name
     * @param signature
     * @param superName
     * @param interfaces
     */
    @Override
    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
        super.visit(version, access, name, signature, superName, interfaces);
        Logger.i("ScanClassVisitor  visit : " + version + " " + access + " " + name + " " + signature + " " + superName + " " + Arrays.toString(interfaces));
        if (StringUtils.isNotEmpty(TransConstans.SCAN_CLASS_MATCH_INTERFACE) && ArrayUtils.isNotEmpty(interfaces)) {
            for (int j = 0; j < interfaces.length; j++) {
                if (StringUtils.equals(TransConstans.SCAN_CLASS_MATCH_INTERFACE, interfaces[j])) {
                    if (!TransformUtil.SCAN_APT_GENERATE_CLASS_LIST.contains(name)) {
                        TransformUtil.SCAN_APT_GENERATE_CLASS_LIST.add(name);
                    }
                }
            }
        }

        Logger.i("ScanClassVisitor  visit : " + version + " " + access + " " + name + " " + signature + " " + superName + " " + TransformUtil.SCAN_APT_GENERATE_CLASS_LIST);
    }
}
