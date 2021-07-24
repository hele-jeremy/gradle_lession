package com.jlpay.appdelegate

import com.android.build.api.transform.*
import com.android.utils.FileUtils
import com.google.common.collect.Sets
import com.jlpay.appdelegate.util.Logger
import com.jlpay.appdelegate.util.RegisterCodeGenerator
import com.jlpay.appdelegate.util.TransConstans
import com.jlpay.appdelegate.util.TransformUtil
import groovy.io.FileType
import org.apache.commons.codec.digest.DigestUtils
import org.gradle.api.Project

class AppDelegateTransform extends Transform {

    Project mProject

    AppDelegateTransform(Project project) {
        this.mProject = project
    }

    /**
     * transform任务名称
     * @return
     */
    @Override
    String getName() {
        return TransConstans.TRANSFORM_NAME
    }

    /**
     * 接收处理的文件类型RESOURCE CLASSES
     * 只处理.class文件
     * @return
     */
    @Override
    Set<QualifiedContent.ContentType> getInputTypes() {
//        return TransformManager.CONTENT_CLASS
        return Collections.singleton(QualifiedContent.DefaultContentType.CLASSES)
    }

    /**
     * 检索范围
     * @return
     */
    @Override
    Set<? super QualifiedContent.Scope> getScopes() {
        //检索整个工程
//        return TransformManager.SCOPE_FULL_PROJECT

        return Sets.immutableEnumSet(
                QualifiedContent.Scope.PROJECT,   //当前工程模块
                QualifiedContent.Scope.SUB_PROJECTS, //其他模块
                QualifiedContent.Scope.EXTERNAL_LIBRARIES //依赖的第三方外部库
        )
    }

    /**
     * 是否支持增量编译
     * @return
     */
    @Override
    boolean isIncremental() {
        return false
    }


    /**
     * 执行过滤.class文件的操作
     * directory目录格式
     * jar包格式
     * @param transformInvocation
     * @throws TransformException* @throws InterruptedException* @throws IOException
     */
    @Override
    void transform(TransformInvocation transformInvocation) throws TransformException, InterruptedException, IOException {
//        super.transform(transformInvocation)

        def appLifecycleDelegateList = []
        Logger.i("start scacn appdegate info ------- ")
        //拿到所有的class文件
        Collection<TransformInput> transformInputs = transformInvocation.inputs
        //转换后的输出
        def outputProvider = transformInvocation.outputProvider
//        if (outputProvider != null) {
//            outputProvider.deleteAll()
//        }

        transformInputs.each { TransformInput transformInput ->

            //遍历所有的jar文件
            transformInput.jarInputs.each { JarInput jarInput ->

                if (jarInput.file.exists()) {
                    def jarName = jarInput.name
                    def jarAbsolutePath = jarInput.file.getAbsolutePath()
                    Logger.i("find jar---: " + jarName)
                    Logger.i("find jar---: " + jarAbsolutePath)
                    //重命名jar名称
                    def hexName = DigestUtils.md5Hex(jarAbsolutePath)
                    if (jarName.endsWith(".jar")) {
                        jarName = jarName.substring(0, jarName.length() - 4)
                    }

                    def jarFile = jarInput.file
                    //获取输出重命名后的jar包的路径
                    File destFile = outputProvider.getContentLocation(jarName + "_" + hexName, jarInput.contentTypes, jarInput.scopes, Format.JAR)

                    //过滤我们需要的指定的类
                    if (TransformUtil.shouldProcessPreDexJar(jarAbsolutePath)) {
                        List<String> classNameList = TransformUtil.scanJar(jarFile, destFile)
                        if (classNameList != null) {
                            appLifecycleDelegateList.addAll(classNameList)
                        }
                    }

                    Logger.i("jar appLifecycleDelegateList : " + appLifecycleDelegateList)
                    //拷贝输出
                    FileUtils.copyFile(jarFile, destFile)

                }
            }

            //遍历所有的class文件夹
            transformInput.directoryInputs.each { DirectoryInput directoryInput ->
                File dir = directoryInput.file

                if (dir) {
                    dir.traverse(type: FileType.FILES, nameFilter: ~/.*\.class/) { File file ->
                        Logger.i("find class---: " + file.name)
                        Logger.i("find class---: " + file.path)

                    }
                } else {
                    Logger.i("find class---**: " + dir.name)
                    Logger.i("find class---**: " + dir.path)
                }

                if (directoryInput.file.isDirectory()) {
                    directoryInput.file.eachFileRecurse { File file ->
                        if (TransformUtil.isTargetClass(file)) {
                            appLifecycleDelegateList.add(file.name)
                        }
                    }
                }

                Logger.i("directory appLifecycleDelegateList : " + appLifecycleDelegateList)

                def dest = outputProvider.getContentLocation(directoryInput.name, directoryInput.contentTypes, directoryInput.scopes, Format.DIRECTORY)
                FileUtils.copyDirectory(directoryInput.file, dest)
            }


        }

        Logger.i("total appLifecycleDelegateList : " + appLifecycleDelegateList)

        if (TransformUtil.INSERT_BYTE_CODE_CLASS_FILE) {
            TransformUtil.TRANSFORM_CLASS_NAME_LIST.each { String className ->
                Logger.i("each className : " + className)

                RegisterCodeGenerator.insertInitCode(appLifecycleDelegateList)


            }
        }


        Logger.i("end scacn appdegate info ------- ")


    }
}
