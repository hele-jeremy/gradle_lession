package com.jlpay.appdelegate_apt;

import com.google.auto.service.AutoService;
import com.jlpay.appdelegate_apt.util.Constants;
import com.jlpay.delegate.anontation.ModuleComponent;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;

import org.apache.commons.collections4.CollectionUtils;

import java.util.Iterator;
import java.util.Set;

import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;


@AutoService(Processor.class) //帮助我们自动注册该注解处理器
@SupportedAnnotationTypes({Constants.ANNOTATION_MODULECOMPONENT})
public class ModuleComponentProcessor extends BaseProcessor {

    private TypeMirror mIAppLifecycleDelegate;

    /**
     * 初始化
     *
     * @param processingEnvironment
     */
    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);
        //获取IAppLifecycleDelegate接口的TypeMirror
        mIAppLifecycleDelegate = mElementUitls.getTypeElement(Constants.IAPPLIFECYCLEDELEGATE).asType();
    }


    /**
     * 处理我们指定的注解
     *
     * @param annotations 包含了所有的注解的集合
     * @param roundEnv    对应注解修饰的类  属性 方法的数据信息
     * @return
     */
    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        if (CollectionUtils.isNotEmpty(annotations)) {
            //获取所有标记了@ModuleComponent注解的节点集合
            Set<? extends Element> moduleComponentElements = roundEnv.getElementsAnnotatedWith(ModuleComponent.class);

            try {

                Iterator<? extends TypeElement> iterator = annotations.iterator();
                while (iterator.hasNext()) {
                    TypeElement next = iterator.next();
                    mLogger.info("annotations - " + next + " class : " + roundEnv.getElementsAnnotatedWith(next));
                }
                parseModuleComponents(moduleComponentElements);

            } catch (Exception e) {
                mLogger.error(e);
            }

            return true;
        }

        return false;
    }

    /**
     * 处理对应的注解信息
     *
     * @param moduleComponentElements
     */
    private void parseModuleComponents(Set<? extends Element> moduleComponentElements) {
        if (CollectionUtils.isNotEmpty(moduleComponentElements)) {
            mLogger.info("parseModuleComponents....");

            TypeElement lifeCycleDelegateElement = mElementUitls.getTypeElement(Constants.IAPPLIFECYCLEDELEGATE);
            TypeMirror delegateMetaElement = mElementUitls.getTypeElement(Constants.DELEGATEMETA).asType();
            mLogger.info("lifeCycleDelegateElement - " + lifeCycleDelegateElement);
            mLogger.info("delegateMetaElement - " + delegateMetaElement);

            //构建方法参数类型
            //TreeSet<DelegateMeta> delegateMetaList
            ClassName treeSet = ClassName.get("java.util", "TreeSet");
            TypeName delegateMeta = ClassName.get(delegateMetaElement);
            ParameterizedTypeName param = ParameterizedTypeName.get(treeSet, delegateMeta);

            //构建方法参数名称
            ParameterSpec delegateMetaList = ParameterSpec.builder(param, "delegateMetaList").build();

            //构建方法collect
            MethodSpec.Builder collectMethod = MethodSpec.methodBuilder("collect")
                    .addAnnotation(Override.class)
                    .addModifiers(Modifier.PUBLIC)
                    .addParameter(delegateMetaList);

            //找出@ModuleComponent标记的类并生成java文件
            for (Element element : moduleComponentElements) {
                TypeMirror tm = element.asType();

                if(!mTypes.isSubtype(tm,mIAppLifecycleDelegate)){
                    throw new RuntimeException("@ModuleComponent 标记的类必须实现IAppLifecycleDelegate接口...!");
                }

                // TODO: 2021/7/23 DelegateMeta 的依赖问题
//                DelegateMeta dm;
            }


        }
    }


}