package com.jlpay.asm;

import com.jlpay.appdelegate.util.Logger;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;

public class HackAppComponentClassVisitor extends ClassVisitor {

    private boolean isAttachBaseContextMethodDefined;
    private boolean isOnCreateMethodDefined;
    private boolean isOnTrimMemoryMethodDefined;
    private boolean isOnLowMemoryMethodDefined;
    private boolean isOnTerminateMethodDefined;
    private boolean isOnConfigurationChangedMethodDefined;

    public HackAppComponentClassVisitor(int api, ClassVisitor classVisitor) {
        super(api, classVisitor);
    }


    @Override
    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
        Logger.i("HackAppComponentClassVisitor visit -> " + name);
        super.visit(version, access, name, signature, superName, interfaces);
    }


    @Override
    public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
        Logger.i("HackAppComponentClassVisitor visitMethod -> " + access + " " + name + " " + descriptor);
        MethodVisitor methodVisitor = super.visitMethod(access, name, descriptor, signature, exceptions);
        String methodDefine = name + " " + descriptor;
        switch (methodDefine) {
            case "attachBaseContext (Landroid/content/Context;)V":
                isAttachBaseContextMethodDefined = true;
                return new AppComponentMethodVisitor(methodVisitor,name, descriptor);
            case "onCreate ()V":
                isOnCreateMethodDefined = true;
                return new AppComponentMethodVisitor(methodVisitor,name, descriptor);
            case "onTrimMemory (I)V":
                isOnTrimMemoryMethodDefined = true;
                return new AppComponentMethodVisitor(methodVisitor,name, descriptor);
            case "onConfigurationChanged (Landroid/content/res/Configuration;)V":
                isOnConfigurationChangedMethodDefined = true;
                return new AppComponentMethodVisitor(methodVisitor,name, descriptor);
            case "onLowMemory ()V":
                isOnLowMemoryMethodDefined = true;
                return new AppComponentMethodVisitor(methodVisitor,name, descriptor);
            case "onTerminate ()V":
                isOnTerminateMethodDefined = true;
                return new AppComponentMethodVisitor(methodVisitor,name, descriptor);
        }

        return methodVisitor;
    }

    @Override
    public void visitEnd() {
        super.visitEnd();
        if (!isAttachBaseContextMethodDefined) {
            definedMethod(4,"attachBaseContext","(Landroid/content/Context;)V");
        }
        if (!isOnCreateMethodDefined) {
            definedMethod(1,"onCreate","()V");
        }

        if (!isOnTrimMemoryMethodDefined) {
            definedMethod(1,"onTrimMemory","(I)V");
        }
        if (!isOnLowMemoryMethodDefined) {
            definedMethod(1,"onLowMemory","()V");
        }
        if (!isOnConfigurationChangedMethodDefined) {
            definedMethod(1,"onConfigurationChanged","(Landroid/content/res/Configuration;)V");
        }
        if (!isOnTerminateMethodDefined) {
            definedMethod(1,"onTerminate","()V");
        }
    }

    /**
     * 没有哪个方法就添加哪个方法
     * @param modifier
     * @param methodName
     * @param desc
     */
    private void definedMethod(int modifier, String methodName, String desc) {

    }
}
