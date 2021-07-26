package com.jlpay.appdelegate

import com.android.build.api.transform.*
import com.android.utils.FileUtils
import com.google.common.collect.Sets
import com.jlpay.appdelegate.util.Logger
import com.jlpay.appdelegate.util.TransConstans
import com.jlpay.appdelegate.util.TransformUtil
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

        Logger.i("start scacn appdegate info ------- ")
        boolean isLeftSlash  = File.separator == "/"
        //拿到所有的class文件包含当前工程和直接以源码依赖的工程的class(目录文件夹的形式出现） 依赖的第三方库(jar包形式出现)
        Collection<TransformInput> transformInputs = transformInvocation.inputs
        //转换后的输出
        def outputProvider = transformInvocation.outputProvider
        if (outputProvider != null && !isIncremental()) {
            outputProvider.deleteAll()
        }

        transformInputs.each { TransformInput transformInput ->

            //遍历所有的jar文件
            transformInput.jarInputs.each { JarInput jarInput ->

                if (jarInput.file.exists()) {
                    def jarName = jarInput.name
                    def jarAbsolutePath = jarInput.file.getAbsolutePath()
                    Logger.i("find jar name---: " + jarName)
                    Logger.i("find jar path---: " + jarAbsolutePath)
                    //重命名jar名称
                    def hexName = DigestUtils.md5Hex(jarAbsolutePath)
                    if (jarName.endsWith(".jar")) {
                        jarName = jarName.substring(0, jarName.length() - 4)
                    }

                    def jarFile = jarInput.file
                    //获取输出重命名后的jar包的目录路径
                    File destFile = outputProvider.getContentLocation(jarName + "_" + hexName, jarInput.contentTypes, jarInput.scopes, Format.JAR)

                    //过滤我们需要的指定的类
                    if (TransformUtil.shouldProcessPreDexJar(jarAbsolutePath)) {
                        TransformUtil.scanJar(jarFile, destFile)
                    }

                    Logger.i("jar appLifecycleDelegateList : " + TransformUtil.SCAN_APT_GENERATE_CLASS_LIST)
                    //拷贝输出
                    FileUtils.copyFile(jarFile, destFile)
                }
            }

            //遍历所有的class文件夹
            transformInput.directoryInputs.each { DirectoryInput directoryInput ->
//                File dir = directoryInput.file

//                if (dir) {
//                    dir.traverse(type: FileType.FILES, nameFilter: ~/.*\.class/) { File file ->
//                        Logger.i("dir find class---: " + file.name)
//                        Logger.i("dir find class---: " + file.path)
//                    }
//                } else {
//                    Logger.i("dir find class---**: " + dir.name)
//                    Logger.i("dir find class---**: " + dir.path)
//                }

                //获取class文件夹的输出目录
                def dest = outputProvider.getContentLocation(directoryInput.name, directoryInput.contentTypes, directoryInput.scopes, Format.DIRECTORY)
                def dirAbsolutePath = directoryInput.file.absolutePath
                Logger.i("dirAbsolutePath before -> " + dirAbsolutePath)
                if (!dirAbsolutePath.endsWith(File.separator)) {
                    //合法的文件夹路径
                    dirAbsolutePath = dirAbsolutePath + File.separator
                }
                Logger.i("dirAbsolutePath after -> " + dirAbsolutePath)
                Logger.i("dirAbsolutePath after -> " + "\\\\")
                //遍历文件夹获取每个class文件
                directoryInput.file.eachFileRecurse { File file ->3
                    def path = file.absolutePath.replace(dirAbsolutePath,'')
                    Logger.i("eachFileRecurse path = " + path + " isLeftSlash = " + isLeftSlash)
                    if(!isLeftSlash){
                        //https://blog.wpjam.com/m/regrex-4-backslash/
                        //编译源码时候进行编译平台的适配
                        path =path.replaceAll("\\\\","/")
                    }


                }


                FileUtils.copyDirectory(directoryInput.file, dest)
            }


        }

        Logger.i("total appLifecycleDelegateList : " + TransformUtil.SCAN_APT_GENERATE_CLASS_LIST)

        if (TransformUtil.INSERT_BYTE_CODE_CLASS_FILE) {
            TransformUtil.TRANSFORM_CLASS_NAME_LIST.each { String className ->
                Logger.i("each className : " + className)

//                RegisterCodeGenerator.insertInitCode(appLifecycleDelegateList)


            }
        }


        Logger.i("end scacn appdegate info ------- ")


    }
}
