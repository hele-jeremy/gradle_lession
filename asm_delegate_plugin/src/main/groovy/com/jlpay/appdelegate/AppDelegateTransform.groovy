package com.jlpay.appdelegate

import com.android.build.api.transform.DirectoryInput
import com.android.build.api.transform.JarInput
import com.android.build.api.transform.QualifiedContent
import com.android.build.api.transform.Transform
import com.android.build.api.transform.TransformException
import com.android.build.api.transform.TransformInput
import com.android.build.api.transform.TransformInvocation
import com.google.common.collect.Sets
import groovy.io.FileType
import org.gradle.api.Project

public class AppDelegateTransform extends Transform {

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
        return "AppDelegateTransform"
    }

    /**
     * 接收处理的文件类型RESOURCE CLASSES
     * 只处理.class文件
     * @return
     */
    @Override
    Set<QualifiedContent.ContentType> getInputTypes() {
        return Collections.singleton(QualifiedContent.DefaultContentType.CLASSES)
    }

    /**
     * 检索范围
     * @return
     */
    @Override
    Set<? super QualifiedContent.Scope> getScopes() {
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
        return true
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

        //保存包含AppLifecycleDelegate类的jar包路径的数组
        def AppLifecycleDelegateStus = []
        //保存包含@AppComponent @ModuleComponent注解修饰的类的jar包路径
        def specificAnnotatoinStubs = []

        System.out.println("start*************************打印class名称*******************************************")
        //拿到所有的class文件
        Collection<TransformInput> transformInputs = transformInvocation.inputs
        transformInputs.each { TransformInput transformInput ->

            //遍历所有的class文件夹
            transformInput.directoryInputs.each { DirectoryInput directoryInput ->
                File dir = directoryInput.file

                if (dir) {
                    3
                    dir.traverse(type: FileType.FILES, nameFilter: ~/.*\.class/) { File file ->
                        System.out.println("find class---: " + file.name)
                        System.out.println("find class---: " + file.path)

                    }
                } else {
                    System.out.println("find class---**: " + dir.name)
                    System.out.println("find class---**: " + dir.path)
                }



            }


            //遍历所有的jar文件
            transformInput.jarInputs.each { JarInput jarInput ->
                if(!jarInput.file.exists()){return }

                System.out.println("find jar---: " + jarInput.name)
                System.out.println("find jar---: " + jarInput.file.getAbsolutePath())
            }


        }

        System.out.println("end*************************打印class名称*******************************************")


    }
}
