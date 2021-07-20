package com.jlpay.appdelegate

import com.android.build.api.transform.DirectoryInput
import com.android.build.api.transform.QualifiedContent
import com.android.build.api.transform.Transform
import com.android.build.api.transform.TransformException
import com.android.build.api.transform.TransformInput
import com.android.build.api.transform.TransformInvocation
import com.google.common.collect.Sets

public class AppDelegateTransform extends Transform {


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
        super.transform(transformInvocation)

        //拿到所有的class文件
        /*Collection<TransformInput> transformInputs = transformInvocation.inputs
        transformInputs.each { TransformInput transformInput ->

            transformInput.directoryInputs.each { DirectoryInput directoryInput ->
                File dir = directoryInput.file

                if (dir) {
                    dir.traverse(type: FileType.FILES, nameFilter: ~/.*\.class/) { File file ->
                        System.out.println("find class---: " + file.name)
                        System.out.println("find class---: " + file.path)

                    }
                } else {
                    System.out.println("find class---**: " + dir.name)
                    System.out.println("find class---**: " + dir.path)
                }

            }
        }*/




    }
}
