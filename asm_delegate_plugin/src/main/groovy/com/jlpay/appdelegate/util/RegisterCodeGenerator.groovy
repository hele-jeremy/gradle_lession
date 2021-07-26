package com.jlpay.appdelegate.util

import org.apache.commons.io.IOUtils
import org.objectweb.asm.ClassReader
import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.ClassWriter
import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.commons.AdviceAdapter
import org.objectweb.asm.Opcodes

import java.util.jar.JarEntry
import java.util.jar.JarFile
import java.util.jar.JarOutputStream
import java.util.zip.ZipEntry

class RegisterCodeGenerator {

    private List<String> classNameList

    RegisterCodeGenerator(List<String> classNameList) {
        this.classNameList = classNameList
    }
/**
 * 执行代码插入
 * @param classNameList
 */
    static void insertInitCode(List<String> classNameList) {
        if (classNameList != null && !classNameList.isEmpty()) {
            RegisterCodeGenerator generator = new RegisterCodeGenerator(classNameList)
            File targetFile = TransformUtil.INSERT_BYTE_CODE_CLASS_FILE

            if (targetFile != null && targetFile.name.endsWith(".jar")) {
                generator.insertInitCodeToJarFile(targetFile)
            }
        }
    }

    /***
     * 插入代码到jar包中
     * @param jarFile
     */
    private void insertInitCodeToJarFile(File jarFile) {
        if (jarFile) {
            def optJar = new File(jarFile.getParent(), jarFile.name + ".opt")
            if (optJar.exists())
                optJar.delete()

            def file = new JarFile(jarFile)
            def entries = file.entries()
            JarOutputStream jarOutputStream = new JarOutputStream(new FileOutputStream(optJar))
            while (entries != null && entries.hasMoreElements()) {
                JarEntry jarEntry = entries.nextElement()
                def entryName = jarEntry.getName()
                def zipEntry = new ZipEntry(entryName)
                def inputStream = file.getInputStream(zipEntry)
                jarOutputStream.putNextEntry(zipEntry)
                if (TransConstans.INSERT_BYTE_CODE_CLASS_FILE_NAME == entryName) {
                    Logger.i("Insert init code to class -> " + entryName)

                    def bytes = referHackWhenInit(inputStream)
                    jarOutputStream.write(bytes)
                } else {
                    jarOutputStream.write(IOUtils.toByteArray(inputStream))
                }
                inputStream.close()
                jarOutputStream.closeEntry()
            }

            jarOutputStream.close()
            file.close()
            if (jarFile.exists()) {
                jarFile.delete()
            }

            optJar.renameTo(jarFile)

        }
    }


    //执行字节码插入的操作
    private byte[] referHackWhenInit(InputStream inputStream) {
        ClassReader cr = new ClassReader(inputStream)
        ClassWriter cw = new ClassWriter(cr, 0)
        ClassVisitor cv = new HackClassVisitor(Opcodes.ASM5, cw)
        cr.accept(cv, ClassReader.EXPAND_FRAMES)
        return cw.toByteArray()
    }

    class HackClassVisitor extends ClassVisitor {

        HackClassVisitor(int api, ClassVisitor classVisitor) {
            super(api, classVisitor)
        }

        @Override
        MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
            MethodVisitor mv = super.visitMethod(access, name, descriptor, signature, exceptions)
            Logger.i("HackClassVisitor  visitMethod name -> " + name)
            if (TransConstans.HACK_INIT_METHOD == name) {
                mv = new AppDelegateMethodVisitor(Opcodes.ASM5, mv, access, name, descriptor)
            }
            return mv
        }
    }


    class AppDelegateMethodVisitor extends AdviceAdapter {


        protected AppDelegateMethodVisitor(int api, MethodVisitor methodVisitor, int access, String name, String descriptor) {
            super(api, methodVisitor, access, name, descriptor)
        }

        @Override
        void visitCode() {
            super.visitCode()
            Logger.i("AppDelegateMethodVisitor visitCode ---")

        }

        @Override
        protected void onMethodEnter() {
            super.onMethodEnter()
            Logger.i("AppDelegateMethodVisitor onMethodEnter ---")


        }

        @Override
        protected void onMethodExit(int opcode) {
            super.onMethodExit(opcode)
            Logger.i("AppDelegateMethodVisitor onMethodExit ---")
        }
    }
}
