package com.hstypay.framework.web.view.excell;

import com.hstypay.framework.web.support.WebUtils;
import com.hstypay.sandbox.support.type.FilePathInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.core.Ordered;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.view.AbstractCachingViewResolver;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

public class ExcelViewResolver extends AbstractCachingViewResolver implements Ordered {
    protected final Logger logger = LoggerFactory.getLogger(getClass());

    // json支持的后缀
    protected Set<String> suffixes = new LinkedHashSet<String>(Arrays.asList("xls", "xlsx"));

    protected View view = null;

    protected int order = 2;

    protected Class<?> viewClass = ExcelView.class;

    protected String prefix = "";

    protected String suffix = "";

    // excel模板的本地路径
    protected String templatePath = null;

    protected String templateParameter = "template";

    //查找不到模板是否抛异常
    protected boolean noTemplateThrowExp = true;

    private List<String> exceptionViews;

    public void setOrder(int order) {
        this.order = order;
    }

    @Override
    public int getOrder() {
        return order;
    }

    public Class<?> getViewClass() {
        return viewClass;
    }

    public void setViewClass(Class<?> viewClass) {
        this.viewClass = viewClass;
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

    public String getTemplatePath() {
        return templatePath;
    }

    public void setTemplatePath(String templatePath) {
        this.templatePath = templatePath;
    }

    public String getTemplateParameter() {
        return templateParameter;
    }

    public void setTemplateParameter(String templateParameter) {
        this.templateParameter = templateParameter;
    }

    public List<String> getExceptionViews() {
        return exceptionViews;
    }

    public void setExceptionViews(List<String> exceptionViews) {
        this.exceptionViews = exceptionViews;
    }

    /**
     * 获取View的模板文件路径,如果templateParameter的参数有设定值，则取参数中设定的名称作为
     * excel模板文件名，否则使用uri作为模板文件名
     * @param viewName
     * @return
     */
    protected String getResponseViewName(String viewName) {
        HttpServletRequest request = WebUtils.getRequest();
        String tempalteView = null;

        // 判断是否是异常请求
        boolean expFlag = false;
        if (exceptionViews != null && exceptionViews.size() > 0) {
            String requestURI = request.getRequestURI();
            String suffix = StringUtils.getFilenameExtension(requestURI);
            for (String exceptionView : exceptionViews) {
                if (viewName.equalsIgnoreCase(exceptionView) || viewName.equalsIgnoreCase(exceptionView + "." + suffix)) {
                    expFlag = true;
                }
            }
        }

        //尝试从templateParameter中指定的参数获取模板视图名
        if (!expFlag && org.apache.commons.lang3.StringUtils.isNoneBlank(templateParameter)) {
            tempalteView = request.getParameter(templateParameter);
        }

        //如果templateParameter为空，则使用uri作为模板视图名
        if (org.apache.commons.lang3.StringUtils.isBlank(tempalteView)) {
            tempalteView = viewName;
        } else {
            /**
             * 如果用templateParameter作为模板视图，则需要将uri中最后部分替换为参数指定的名称
             * 例如/demotest/list_test.xls?template=trade，则需要替换uri生成/demotest/trade.xls
             * 作为视图
             */
            FilePathInfo pathInfo = new FilePathInfo(viewName);
            StringBuilder sb = new StringBuilder(64);
            if (pathInfo.getPath() != null) {
                sb.append(pathInfo.getPath());
                if (!pathInfo.getPath().endsWith("/"))
                    sb.append("/");
            }

            if (pathInfo.getFileName() != null)
                sb.append(tempalteView);

            if (pathInfo.getFileExtension() != null) {
                sb.append(".");
                sb.append(pathInfo.getFileExtension());
            }

            tempalteView = sb.toString();
        }

        return tempalteView;
    }

    @Override
    public View resolveViewName(String viewName, Locale locale) throws Exception {
        HttpServletRequest request = WebUtils.getRequest();
        String requestURI = request.getRequestURI();
        String suffix = StringUtils.getFilenameExtension(requestURI);

        int separatorIndex = viewName.indexOf(".");

        // 判断viewName后缀是否为支持的后缀
        if (suffix == null || separatorIndex == -1 || !suffixes.contains(suffix)) {
            return null;
        }
        String viewNamePrefix = viewName.substring(0, separatorIndex);

        viewName = viewNamePrefix + "." + suffix;
        viewName = getResponseViewName(viewName);
        return super.resolveViewName(viewName, locale);
    }

    @Override
    protected View loadView(String viewName, Locale locale) throws Exception {
        String suffix = StringUtils.getFilenameExtension(viewName);
        // 判断viewName后缀是否为支持的后缀
        if (suffix != null && suffixes.contains(suffix)) {
            return buildView(viewName);
        }

        return null;
    }

    /**
     * 创建View
     * @param viewName
     * @return
     * @throws Exception
     */
    protected ExcelView buildView(String viewName) throws Exception {
        String templateFile = getPrefix() + viewName + getSuffix();

        ExcelView view = (ExcelView) BeanUtils.instantiateClass(getViewClass());
        view.setTemplatePath(this.templatePath);
        view.setTempalteFile(templateFile);
        view.setApplicationContext(getApplicationContext());

        return view;
    }
}