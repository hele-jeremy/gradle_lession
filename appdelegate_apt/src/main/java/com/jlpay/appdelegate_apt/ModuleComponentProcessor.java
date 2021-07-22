package com.jlpay.appdelegate_apt;

import com.google.auto.service.AutoService;
import com.jlpay.appdelegate_apt.util.Constants;

import org.apache.commons.collections4.CollectionUtils;

import java.util.Iterator;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.lang.model.element.TypeElement;
import javax.xml.bind.annotation.XmlAccessOrder;


@AutoService(Process.class) //帮助我们自动注册该注解处理器
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
     * @param set
     * @param roundEnvironment
     * @return
     */
    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        if(CollectionUtils.isNotEmpty(annotations)){

            Iterator<? extends TypeElement> iterator = annotations.iterator();
            while (iterator.hasNext()) {
                TypeElement next = iterator.next();
                mLogger.info("annotations - " + next.getSimpleName() + " class : " + roundEnv.getElementsAnnotatedWith(next));
            }

            return true;
        }

        return false;
    }




}