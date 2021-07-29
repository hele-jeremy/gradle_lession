package com.jlpay.appdelegate.util

import com.jlpay.asm.HackAppComponentClassVisitor
import com.jlpay.asm.HackClassVisitor
import org.apache.commons.io.IOUtils
import org.apache.commons.lang3.StringUtils
import org.objectweb.asm.ClassReader
import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.ClassWriter
import org.objectweb.asm.Opcodes

import java.util.jar.JarEntry
import java.util.jar.JarFile
import java.util.jar.JarOutputStream
import java.util.zip.ZipEntry

class RegisterCodeGenerator {

    /**
     * 执行字节码代码插入
     */
    static void insertInitCode() {
        RegisterCodeGenerator generator = new RegisterCodeGenerator()
        File targetFile = TransformUtil.INSERT_BYTE_CODE_CLASS_FILE
        Logger.i("insertInitCode targetFile -> " + targetFile)
        if (targetFile != null && targetFile.exists() && targetFile.name.endsWith(".jar")) {
            generator.insertInitCodeToJarFile(targetFile)
        }
    }

    static void insertAppComponentInitCode() {
        RegisterCodeGenerator generator = new RegisterCodeGenerator()
        generator.insertAppInitCodeToFileOrJarFile()
    }

    /**
     * 插入代码到 @Appcomponent注解标记的类中
     */
    private void insertAppInitCodeToFileOrJarFile() {
        Set<Map.Entry<File, List<String>>> entries = TransformUtil.SCAN_APPCOMPONENT_MARK_CLASS.entrySet()
        for (Map.Entry<File, List<String>> entry : entries) {
            File file = entry.key
            List<String> classNames = entry.value
            String tartFilePath = file.absolutePath

            if (file != null && file.exists() && file.isDirectory()) {
                Logger.i("insertAppInitCodeToFileOrJarFile file -> " + tartFilePath)
                for (String className : classNames) {
                    File hackFile = new File(file, File.separator + className + TransConstans.CLAZZ)
                    Logger.i("insertAppInitCodeToFileOrJarFile hackFile -> " + hackFile.absolutePath)
                    //是class文件类型
                    hackAppComponentInitCodeInFile(hackFile)
                }
            } else {
                for (String className : classNames) {
                    insertAppComponentInitCodeToJarFile(file, className)
                }

            }
        }
    }


    private void insertAppComponentInitCodeToJarFile(File jarFile, String className) {
        if (jarFile != null && jarFile.exists() && jarFile.isFile() && StringUtils.isNotEmpty(className)) {
            Logger.i("insertInitCodeIntoJarFile jarFile.name -> " + jarFile.name)
            def optJar = new File(jarFile.getParent(), jarFile.name + ".opt")
            if (optJar.exists())
                optJar.delete()
            Logger.i("insertInitCodeIntoJarFile optJar -> " + optJar)
            def file = new JarFile(jarFile)
            def entries = file.entries()
            JarOutputStream jarOutputStream = new JarOutputStream(new FileOutputStream(optJar))
            while (entries != null && entries.hasMoreElements()) {
                JarEntry jarEntry = entries.nextElement()
                def entryName = jarEntry.getName()
                Logger.i("insertInitCodeIntoJarFile entryName -> " + entryName)
                def zipEntry = new ZipEntry(entryName)
                def inputStream = file.getInputStream(zipEntry)
                jarOutputStream.putNextEntry(zipEntry)
                //找到要插入代码的目标类
                if (StringUtils.equals(className + TransConstans.CLAZZ, entryName)) {
                    Logger.i("Insert initAppcomponent code to class -> " + entryName)
                    //ASM执行字节码的插桩
                    def bytes = referHackAppComponentWhenInit(inputStream)
                    //将插桩后的class重写写入jar包中
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


    /**
     * 在class文件中插入代码
     * @param classFile
     */
    private void hackAppComponentInitCodeInFile(File classFile) {
        if (classFile != null && classFile.exists() && classFile.isFile()) {
            File tempFile = new File(classFile.getParent(), classFile.name + ".temp")
            if (tempFile.exists()) {
                tempFile.delete()
            }

            FileInputStream inputStream = new FileInputStream(classFile)
            def bytes = referHackAppComponentWhenInit(inputStream)
            FileOutputStream outputStream = new FileOutputStream(tempFile)
            outputStream.write(bytes)
            outputStream.flush()
            inputStream.close()
            outputStream.close()
            if (classFile.exists()) {
                classFile.delete()
            }
            tempFile.renameTo(classFile)
        }
    }

    private byte[] referHackAppComponentWhenInit(InputStream inputStream) {
        ClassReader cr = new ClassReader(inputStream)
        ClassWriter cw = new ClassWriter(cr, ClassWriter.COMPUTE_MAXS)
        ClassVisitor cv = new HackAppComponentClassVisitor(Opcodes.ASM5, cw)
        cr.accept(cv, ClassReader.EXPAND_FRAMES)
        return cw.toByteArray()
    }

    /***
     * 插入代码到jar包中
     * @param jarFile
     */
    private void insertInitCodeToJarFile(File jarFile) {
        if (jarFile != null && jarFile.exists() && jarFile.isFile()) {
            Logger.i("insertInitCodeIntoJarFile jarFile.name -> " + jarFile.name)
            def optJar = new File(jarFile.getParent(), jarFile.name + ".opt")
            if (optJar.exists())
                optJar.delete()
            Logger.i("insertInitCodeIntoJarFile optJar -> " + optJar)
            def file = new JarFile(jarFile)
            def entries = file.entries()
            JarOutputStream jarOutputStream = new JarOutputStream(new FileOutputStream(optJar))
            while (entries != null && entries.hasMoreElements()) {
                JarEntry jarEntry = entries.nextElement()
                def entryName = jarEntry.getName()
                Logger.i("insertInitCodeIntoJarFile entryName -> " + entryName)
                def zipEntry = new ZipEntry(entryName)
                def inputStream = file.getInputStream(zipEntry)
                jarOutputStream.putNextEntry(zipEntry)
                //找到要插入代码的目标类
                if (StringUtils.equals(TransConstans.INSERT_BYTE_CODE_CLASS_FILE_NAME, entryName)) {
                    Logger.i("Insert init code to class -> " + entryName)
                    //ASM执行字节码的插桩
                    def bytes = referHackWhenInit(inputStream)
                    //将插桩后的class重写写入jar包中
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
        ClassWriter cw = new ClassWriter(cr, ClassWriter.COMPUTE_MAXS)
        ClassVisitor cv = new HackClassVisitor(Opcodes.ASM5, cw)
        cr.accept(cv, ClassReader.EXPAND_FRAMES)
        return cw.toByteArray()
    }

}
