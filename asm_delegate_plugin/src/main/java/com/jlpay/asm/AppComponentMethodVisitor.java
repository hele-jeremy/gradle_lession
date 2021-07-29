package com.jlpay.asm;

import com.jlpay.appdelegate.util.Logger;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

public class AppComponentMethodVisitor extends MethodVisitor {

    private String methodName;//调用的方法名
    private String methodDesc;//调用方法的子节码描述
    private boolean aload;
    private boolean iload;


    public AppComponentMethodVisitor(MethodVisitor methodVisitor, String name, String descriptor, boolean aload, boolean iload) {
        super(Opcodes.ASM5, methodVisitor);
        this.methodName = name;
        this.methodDesc = descriptor;
        this.aload = aload;
        this.iload = iload;
    }

    /**
     * 将AppLifecycleDelegate类中分发Application生命周期的方法 分发到各个实现了IAppLifecycleDelegate接口的子类中
     * <p>
     * ASM是一个Java字节码操控框架。它能被用来动态生成类或者增强既有类的功能。ASM可以直接产生二进制class文件，也可以在类被加载入Java虚拟机之前动态改变类行为。
     * ASM实现可以是接口，也是可以类。
     * Java class被存储在严格格式定义的`.class`文件里，这些类文件拥有足够的元数据来解析类中的所有元素：类名称、方法、属性以及Java字节码（指令）。ASM从类文件中读入信息后，能够改变类行为，分析类信息，甚至能够根据用户要求生成新类。
     * ## JVM执行class的指令
     * 在Java中每一个方法在执行的时候JVM都会为其分配一个"帧"，帧是用来存储方法中计算所需要的所有数据。其中第0个元素就是`this`，如果方法有参数传入会排在它的后面。
     * ### ALOAD_0
     * 这个指令是`LOAD`系列指令中的一个，它的意思表示装载当前第0个元素到堆栈中。代码上相当于"this"。而这个数据元素的类型是一个引用类型。这些指令包含`ALOAD`，`ILOAD`，`LLOAD`，`FLOAD`，`DLOAD`。区分它们的作用就是针对不用数据类型而准备的`LOAD`指令，此外还有专门负责处理数组的指令`SALOAD`。
     * ### Invokespecial
     * 这个指令是调用系列指令中的一个。其目的是调用对象类的方法。后面需要给上父类的方法完整签名。"#8"的意思是 `.class`文件常量表中第8个元素。值为`java/lang/Object."<init>":()V`。结合`ALOAD_0`。这两个指令可以翻译为`super()`。其含义是调用自己的父类构造方法。
     * ### GETSTATIC
     * 这个指令是`GET`系列指令中的一个其作用是获取静态字段内容到堆栈中。这一系列指令包括了：`GETFIELD`、`GETSTATIC`。它们分别用于获取动态字段和静态字段。
     * ### IDC
     * 这个指令的功能是从常量表中装载一个数据到堆栈中。
     * ### Invokevirtual
     * 是一种调用指令，这个指令区别与`invokespecial`的是它是根据引用调用对象类的方法。这里有一篇文章专门讲解这两个指令："http://wensiqun.iteye.com/blog/1125503"。
     * ### RETURN
     * 这也是一系列指令中的一个，其目的是方法调用完毕返回，可用的其他指令有：`IRETURN`，`DRETURN`，`ARETURN`等，用于表示不同类型参数的返回
     *
     * @param opcode
     */
    @Override
    public void visitInsn(int opcode) {
        /**
         * (Opcodes.ALOAD,1)指令表示从局部变量表加载一个reference类型值到操作数栈,对于非静态方法的局部变量表中的第一个即下表为0的位置的值为该方法所在类类的引用即this
         *      在我们需要调用的方法中只有attachBaseContext和onConfigurationchange方法需要用到参数并且都是局部变量表中的第一个位置的参数
         * （Opcodes.ILOAD,1)加载局部变量表中的位置下标为1的数据类型为int类型的变量
         *      在我们要调用的方法中有onTrimMemory方法需要使用到int类型的参数
         */

        //通过判断Opcodes的return码来保证插入的子节码在return之前生效
        switch (opcode) {
            case Opcodes.IRETURN:
            case Opcodes.ARETURN:
            case Opcodes.RETURN:
            case Opcodes.FRETURN:
            case Opcodes.DRETURN:
            case Opcodes.LRETURN:
                Logger.i("AppComponentMethodVisitor visitInsn -> " + opcode);
                //调用AppLifecycleDelegate中的get()方法
                mv.visitMethodInsn(Opcodes.INVOKESTATIC, "com/jeremy/appdelegate/AppLifecycleDelegate", "get", "()Lcom/jeremy/appdelegate/AppLifecycleDelegate;", false);
                if (aload) {
                    mv.visitVarInsn(Opcodes.ALOAD, 1);
                }
                if (iload) {
                    mv.visitVarInsn(Opcodes.ILOAD, 1);
                }
                //调用AppLifecycleDelegate中的各个分发生命周期方法
                mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "com/jeremy/appdelegate/AppLifecycleDelegate", methodName, methodDesc, false);
                break;
        }

        super.visitInsn(opcode);
    }
}
