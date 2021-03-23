package com.jeremy.plugin

import com.android.build.api.transform.*
import com.android.build.gradle.internal.pipeline.TransformManager
import com.jeremy.asm.LifeCycleClassVisitor
import groovy.io.FileType
import org.apache.commons.io.FileUtils
import org.objectweb.asm.ClassReader
import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.ClassWriter

public class LifeCycleTransform extends Transform {


    //设置我们自定义的 Transform 对应的 Task 名称。
    // Gradle 在编译的时候，会将这个名称显示在控制台上。比如：Task :app:transformClassesWithXXXForDebug。
    @Override
    String getName() {
        return "LifeCycleTransform"
    }

    //在项目中会有各种各样格式的文件，
    // 通过 getInputType 可以设置 LifeCycleTransform 接收的文件类型，
    // 此方法返回的类型是 Set<QualifiedContent.ContentType> 集合
    //CLASSES：代表只检索 .class 文件；
    //RESOURCES：代表检索 java 标准资源文件
    @Override
    Set<QualifiedContent.ContentType> getInputTypes() {
        return TransformManager.CONTENT_CLASS
    }

    //规定自定义 Transform 检索的范围
    @Override
    Set<? super QualifiedContent.Scope> getScopes() {
//        return TransformManager.SCOPE_FULL_PROJECT
        return TransformManager.PROJECT_ONLY
    }

    // 表示当前 Transform 是否支持增量编译
    @Override
    boolean isIncremental() {
        return false
    }

    //在 自定义Transform 中最重要的方法就是 transform()。在这个方法中，可以获取到两个数据的流向。
    //inputs：inputs 中是传过来的输入流，其中有两种格式，一种是 jar 包格式，一种是 directory（目录格式）。
    //outputProvider：outputProvider 获取到输出目录，最后将修改的文件复制到输出目录，这一步必须做，否则编译会报错。
    @Override
    void transform(TransformInvocation transformInvocation)
            throws TransformException, InterruptedException, IOException {
//        super.transform(transformInvocation)

        println("*************************打印class名称*******************************************")


//        //拿到所有的class文件
//        Collection<TransformInput> transformInputs = transformInvocation.inputs
//        transformInputs.each { TransformInput transformInput ->
//
//            transformInput.directoryInputs.each { DirectoryInput directoryInput ->
//                File dir = directoryInput.file
//
//                if (dir) {
//                    dir.traverse(type: FileType.FILES, nameFilter: ~/.*\.class/) { File file ->
//                        System.out.println("find class: " + file.name)
//
//                    }
//                }
//
//            }
//        }

        //拿到所有的.class文件
        Collection<TransformInput> transformInputs = transformInvocation.inputs
        TransformOutputProvider transformOutputProvider = transformInvocation.outputProvider
        if (transformOutputProvider != null) {
            transformOutputProvider.deleteAll()
        }

        transformInputs.each { TransformInput transformInput ->
            transformInput.directoryInputs.each { DirectoryInput directoryInput ->

                File dir = directoryInput.file
                if (dir) {
                    dir.traverse(type: FileType.FILES, nameFilter: ~/.*\.class/) { File file ->
                        System.out.println("find class: " + file.name)
                        //对class文件进行读取
                        ClassReader classReader = new ClassReader(file.bytes)
                        //对class文件的写入
                        ClassWriter classWriter = new ClassWriter(classReader, ClassWriter.COMPUTE_MAXS)
                        //访问class文件的响应的内容,解析到某一个结构就会通知到ClassVisitor的相应方法
                        ClassVisitor classVisitor = new LifeCycleClassVisitor(classWriter)
                        //依次调用ClassVisitor的各个方法
                        classReader.accept(classVisitor, ClassReader.EXPAND_FRAMES)
                        //toByteArray方法会将最终修改的字节码以 byte数组形式返回
                        byte[] bytes = classWriter.toByteArray()
                        //通过文件流写入方式覆盖原先的内容,实现class文件的改写
                        FileOutputStream fileOutputStream = new FileOutputStream(file.path)
                        fileOutputStream.write(bytes)
                        fileOutputStream.close()

                    }
                }

                //处理完输入文件后把输出传给下一个文件
                def dest = transformOutputProvider.getContentLocation(directoryInput.name, directoryInput.contentTypes, directoryInput.scopes, Format.DIRECTORY)
                FileUtils.copyDirectory(directoryInput.file, dest)

            }

            //gradle 3.6.0以上R类不会转为.class文件而会转成jar，因此在Transform实现中需要单独拷贝
            transformInput.jarInputs.each { JarInput jarInput ->
                File file = jarInput.file
                System.out.println("jarinput : " + file.name)
                def dest = transformOutputProvider.getContentLocation(jarInput.name, jarInput.contentTypes, jarInput.scopes, Format.JAR)
                FileUtils.copyFile(file, dest)
            }
        }


        println("*************************打印class名称*******************************************")


    }
}