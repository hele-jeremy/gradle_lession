package com.jlpay.asm;

import com.jlpay.appdelegate.util.Logger;

import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.ClassVisitor;

public class ScanAppComponentClassVisitor extends ClassVisitor {


    public ScanAppComponentClassVisitor(int api) {
        super(api);
    }

    @Override
    public AnnotationVisitor visitAnnotation(String descriptor, boolean visible) {
        Logger.i("ScanAppComponentClassVisitor  visitAnnotation  :  " + descriptor + " " + visible);
        return super.visitAnnotation(descriptor, visible);
    }
}
