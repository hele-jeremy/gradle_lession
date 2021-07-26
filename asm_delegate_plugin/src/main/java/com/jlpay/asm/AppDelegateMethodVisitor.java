package com.jlpay.asm;

import com.jlpay.appdelegate.util.Logger;
import com.jlpay.appdelegate.util.TransConstans;
import com.jlpay.appdelegate.util.TransformUtil;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.commons.AdviceAdapter;

import java.util.ArrayList;

public class AppDelegateMethodVisitor extends AdviceAdapter {


    protected AppDelegateMethodVisitor(int api, MethodVisitor methodVisitor, int access, String name, String descriptor) {
        super(api, methodVisitor, access, name, descriptor);
    }

    @Override
    public void visitCode() {
        super.visitCode();
        Logger.i("AppDelegateMethodVisitor visitCode ---");
    }

    @Override
    protected void onMethodEnter() {
        super.onMethodEnter();
        Logger.i("AppDelegateMethodVisitor onMethodEnter ---");
        ArrayList<String> list = TransformUtil.SCAN_APT_GENERATE_CLASS_LIST;
        for (String aptClassName : list) {
            Logger.i("onMethodEnter aptClassName -> " + aptClassName);
            mv.visitTypeInsn(Opcodes.NEW, aptClassName);
            mv.visitInsn(Opcodes.DUP);
            mv.visitMethodInsn(Opcodes.INVOKESPECIAL, aptClassName, "<init>", "()V", false);
            mv.visitVarInsn(Opcodes.ALOAD, 0);
            mv.visitFieldInsn(Opcodes.GETFIELD, TransConstans.INSERT_BYTE_CODE_CLASS_PATH, "mAppDelegateMetas", "Ljava/util/TreeSet;");
            mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, aptClassName, "collect", "(Ljava/util/TreeSet;)V", false);
            mv.visitInsn(Opcodes.POP);

        }


    }

    @Override
    protected void onMethodExit(int opcode) {
        super.onMethodExit(opcode);
        Logger.i("AppDelegateMethodVisitor onMethodExit ---");
    }
}