package com.jlpay.asm;

import com.jlpay.appdelegate.util.Logger;
import com.jlpay.appdelegate.util.TransConstans;

import org.apache.commons.lang3.StringUtils;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import java.util.Arrays;

public class HackClassVisitor extends ClassVisitor {

    HackClassVisitor(int api, ClassVisitor classVisitor) {
        super(api, classVisitor);
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
        MethodVisitor mv = super.visitMethod(access, name, descriptor, signature, exceptions);
        Logger.i("HackClassVisitor  visitMethod access -> " + access);
        Logger.i("HackClassVisitor  visitMethod name -> " + name);
        Logger.i("HackClassVisitor  visitMethod descriptor -> " + descriptor);
        Logger.i("HackClassVisitor  visitMethod signature -> " + signature);
        Logger.i("HackClassVisitor  visitMethod exceptions -> " + Arrays.toString(exceptions));
        if (StringUtils.equals(TransConstans.HACK_INIT_METHOD,name)) {
            mv = new AppDelegateMethodVisitor(Opcodes.ASM5, mv, access, name, descriptor);
        }
        return mv;
    }
}