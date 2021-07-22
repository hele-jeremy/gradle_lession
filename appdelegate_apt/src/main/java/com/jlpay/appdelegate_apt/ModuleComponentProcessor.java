package com.jlpay.appdelegate_apt;

import com.google.auto.service.AutoService;
import com.jlpay.appdelegate_apt.util.Constants;
import com.jlpay.delegate.anontation.ModuleComponent;

import org.apache.commons.collections4.CollectionUtils;

import java.util.Iterator;
import java.util.Set;

import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;


@AutoService(Processor.class) //帮助我们自动注册该注解处理器
@SupportedAnnotationTypes({Constants.ANNOTATION_MODULECOMPONENT})
public class ModuleComponentProcessor extends BaseProcessor {


    /**
     * 初始化
     * @param processingEnvironment
     */
    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);

        mLogger.info("init----:  "+this.getClass().getSimpleName());

    }


    /**
     * 处理我们指定的注解
     * @param annotations
     * @param roundEnv
     * @return
     */
    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        if(CollectionUtils.isNotEmpty(annotations)){
            //获取所有标记了@ModuleComponent注解的类的集合
            Set<? extends Element> moduleComponentElements = roundEnv.getElementsAnnotatedWith(ModuleComponent.class);

            try{

                Iterator<? extends TypeElement> iterator = annotations.iterator();
                while (iterator.hasNext()) {
                    TypeElement next = iterator.next();
                    mLogger.info("annotations - " + next + " class : " + roundEnv.getElementsAnnotatedWith(next));
                }

                parseModuleComponents(moduleComponentElements);

            }catch (Exception e){
                mLogger.error(e);
            }

            return true;
        }

        return false;
    }

    private void parseModuleComponents(Set<? extends Element> moduleComponentElements) {
        if(CollectionUtils.isNotEmpty(moduleComponentElements)){




        }
    }


}