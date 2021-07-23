package com.jlpay.appdelegate_apt;

import com.jlpay.appdelegate_apt.util.Constants;
import com.jlpay.appdelegate_apt.util.Logger;

import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;

public abstract class BaseProcessor extends AbstractProcessor {

    //日志打印
    protected Logger mLogger;
    //文件操作
    protected Filer mFiler;
    //类型
    protected Types mTypes;
    //节点解析
    protected Elements mElementUitls;
    //各个组件名
    protected String mComponentName;

    /**
     * 注解处理器初始化
     *
     * @param processingEnvironment
     */
    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);
        mLogger = new Logger(processingEnvironment.getMessager());
        mFiler = processingEnvironment.getFiler();
        mTypes = processingEnvironment.getTypeUtils();
        mElementUitls = processingEnvironment.getElementUtils();

        //获取用户配置的组件名
        Map<String, String> options = processingEnvironment.getOptions();
        if (MapUtils.isNotEmpty(options)) {
            mComponentName = options.get(Constants.KEY_COMPONENT_NAME);
        }

        if (StringUtils.isNotEmpty(mComponentName)) {
            mComponentName.replaceAll("[^0-9a-zA-Z_]+", "");
            mLogger.info("configuration componentname is [ " + mComponentName + " ]");
        } else {
            throw new IllegalArgumentException("please set " + Constants.KEY_COMPONENT_NAME + " in your module build.gradle android{}");
        }
    }


    /**
     * 注解处理器支持接受的参数
     *
     * @return
     */
    @Override
    public Set<String> getSupportedOptions() {
        return new HashSet<String>() {{
            add(Constants.KEY_COMPONENT_NAME);
        }};
    }

    /**
     * 支持的java源文件的版本
     *
     * @return
     */
    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
//        return SourceVersion.RELEASE_8;
    }
}
