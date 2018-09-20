package com.hstypay.framework.web.validator;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.hstypay.framework.web.validator.annotation.Valid;

/** 请求参数拦截
 * 
 * @author zh
 *
 */
public class ValidatorHandlerInterceptor extends HandlerInterceptorAdapter {

	/** 请求到达Controller之前拦截
	 * 
	 */
	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {
		if (handler instanceof HandlerMethod) {
			HandlerMethod handlerMethod = (HandlerMethod)handler;
			Method method = handlerMethod.getMethod();
			if (method.isAnnotationPresent(RequestMapping.class)) {
				RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
				RequestMethod[] requestMethods = requestMapping.method();
		    	for (RequestMethod requestMethod : requestMethods) {
		    		if (RequestMethod.POST == requestMethod) {
		    			return validator(request, method);
					}
				}
			}
		}
		return super.preHandle(request, response, handler);
	}
	
	public boolean validator(HttpServletRequest request, Method method) {
		Annotation[][] annontations = method.getParameterAnnotations();
    	if (annontations != null) {
    		for (Annotation[] annotation : annontations) {
        		for (Annotation annot : annotation) {
        			if (annot instanceof Valid) {
        				Class<?>[] clazzs = method.getParameterTypes();
        				for (Class<?> clazz : clazzs) {
        					//加入校验
        					AutoVaildator autoVaildator = new RequestAutoValidator(request, clazz);
        					autoVaildator.validator();
						}
    				}
    			}
    		}
		}
    	return true;
	}

}
