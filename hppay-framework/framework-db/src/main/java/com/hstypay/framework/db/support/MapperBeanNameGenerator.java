package com.hstypay.framework.db.support;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.AnnotationBeanNameGenerator;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;

import java.beans.Introspector;

/**
 * mybatis根据接口生成DAO时的bean名字生成
 * @author Tinnfy Lee
 *
 */
public class MapperBeanNameGenerator extends AnnotationBeanNameGenerator {
    //前缀
    protected String prefix;
    //后缀
    protected String suffix;

    public MapperBeanNameGenerator() {
        // TODO Auto-generated constructor stub
    }

    @Override
    protected String buildDefaultBeanName(BeanDefinition definition) {
        String shortClassName = ClassUtils.getShortName(definition.getBeanClassName());

        StringBuilder sb = new StringBuilder();
        //拼接前缀
        if (StringUtils.hasText(prefix))
            sb.append(prefix);

        sb.append(shortClassName);

        //拼接后缀
        if (StringUtils.hasText(suffix))
            sb.append(suffix);

        //首字母小写,如果是连着两个大写，则不将首 字母转小写
        return Introspector.decapitalize(sb.toString());
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public String getSuffix() {
        return suffix;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }
}
