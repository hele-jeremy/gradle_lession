package com.jlpay.appdelegate.util

import com.jlpay.asm.HackClassVisitor
import org.apache.commons.collections4.CollectionUtils
import org.apache.commons.io.IOUtils
import org.objectweb.asm.ClassReader
import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.ClassWriter
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
     * 执行字节码代码插入
     * @param classNameList
     */
    static void insertInitCode(List<String> classNameList) {
        if (CollectionUtils.isNotEmpty(classNameList)) {
            RegisterCodeGenerator generator = new RegisterCodeGenerator(classNameList)
            File targetFile = TransformUtil.INSERT_BYTE_CODE_CLASS_FILE
            Logger.i("insertInitCode targetFile -> " + targetFile)
            if (targetFile != null && targetFile.exists() && targetFile.name.endsWith(".jar")) {
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
                if (TransConstans.INSERT_BYTE_CODE_CLASS_FILE_NAME == entryName) {
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
