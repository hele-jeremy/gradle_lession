package com.jlpay.asm;

import com.jlpay.appdelegate.util.Logger;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

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


    /**
     * @param access
     * @param name
     * @param descriptor
     * @param signature
     * @param exceptions
     * @return
     */
    @Override
    public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
        Logger.i("HackAppComponentClassVisitor visitMethod -> " + access + " " + name + " " + descriptor);
        MethodVisitor methodVisitor = super.visitMethod(access, name, descriptor, signature, exceptions);
        String methodDefine = name + " " + descriptor;
        switch (methodDefine) {
            case "attachBaseContext (Landroid/content/Context;)V":
                isAttachBaseContextMethodDefined = true;
                return new AppComponentMethodVisitor(methodVisitor, name, descriptor, true, false);
            case "onCreate ()V":
                isOnCreateMethodDefined = true;
                return new AppComponentMethodVisitor(methodVisitor, name, descriptor, false, false);
            case "onTrimMemory (I)V":
                isOnTrimMemoryMethodDefined = true;
                return new AppComponentMethodVisitor(methodVisitor, name, descriptor, false, true);
            case "onConfigurationChanged (Landroid/content/res/Configuration;)V":
                isOnConfigurationChangedMethodDefined = true;
                return new AppComponentMethodVisitor(methodVisitor, name, descriptor, true, false);
            case "onLowMemory ()V":
                isOnLowMemoryMethodDefined = true;
                return new AppComponentMethodVisitor(methodVisitor, name, descriptor, false, false);
            case "onTerminate ()V":
                isOnTerminateMethodDefined = true;
                return new AppComponentMethodVisitor(methodVisitor, name, descriptor, false, false);
        }

        return methodVisitor;
    }

    /**
     * 在最后如果没有指定的方法就生成指定的方法并插入代码
     */
    @Override
    public void visitEnd() {

        if (!isAttachBaseContextMethodDefined) {
            definedMethod(4, "attachBaseContext", "(Landroid/content/Context;)V", true, false);
        }
        if (!isOnCreateMethodDefined) {
            definedMethod(1, "onCreate", "()V", false, false);
        }

        if (!isOnTrimMemoryMethodDefined) {
            definedMethod(1, "onTrimMemory", "(I)V", false, true);
        }
        if (!isOnLowMemoryMethodDefined) {
            definedMethod(1, "onLowMemory", "()V", false, false);
        }
        if (!isOnConfigurationChangedMethodDefined) {
            definedMethod(1, "onConfigurationChanged", "(Landroid/content/res/Configuration;)V", true, false);
        }
        if (!isOnTerminateMethodDefined) {
            definedMethod(1, "onTerminate", "()V", false, false);
        }
        super.visitEnd();
    }

    /**
     * 没有哪个方法就添加哪个方法
     * https://blog.csdn.net/aesop_wubo/article/details/48948211
     *
     * @param modifier
     * @param methodName
     * @param desc
     */
    private void definedMethod(int modifier, String methodName, String desc, boolean aload, boolean iload) {
        //添加指定的方法
        MethodVisitor mv = this.visitMethod(modifier, methodName, desc, null, null);
        mv.visitCode();  //如果要生成方法的代码，需要先以visitCode开头，访问结束需要调用visitEnd方法
        mv.visitVarInsn(Opcodes.ALOAD, 0);   //将该类的引用对象入栈
        if (aload) {
            mv.visitVarInsn(Opcodes.ALOAD, 1);
        }

        if (iload) {
            mv.visitVarInsn(Opcodes.ILOAD, 1);
        }
        //添加super访问父类的方法
        mv.visitMethodInsn(Opcodes.INVOKESPECIAL, "android/app/Application", methodName, desc, false);
        //return结束方法
        mv.visitInsn(Opcodes.RETURN);
        mv.visitEnd();
    }
}
