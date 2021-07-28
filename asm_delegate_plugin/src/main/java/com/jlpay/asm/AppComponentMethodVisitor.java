package com.jlpay.asm;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

public class AppComponentMethodVisitor extends MethodVisitor {


    public AppComponentMethodVisitor(MethodVisitor methodVisitor, String name, String descriptor) {
        super(Opcodes.ASM5,methodVisitor);
    }

    @Override
    public void visitInsn(int opcode) {
        mv.visitMethodInsn(Opcodes.INVOKESTATIC, "com/jeremy/appdelegate/AppLifecycleDelegate", "get", "()Lcom/jeremy/appdelegate/AppLifecycleDelegate;", false);
        mv.visitVarInsn(Opcodes.ALOAD, 1);
        mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "com/jeremy/appdelegate/AppLifecycleDelegate", "attachBaseContext", "(Landroid/content/Context;)V", false);
        super.visitInsn(opcode);
    }
}
