package com.hstypay.framework.session.aspect;

import com.hstypay.framework.session.Session;
import com.hstypay.framework.session.SessionInterceptor;
import com.hstypay.framework.session.SessionValidator;
import com.hstypay.framework.session.annotation.CheckSession;
import com.hstypay.framework.session.exception.SessionErrorCodes;
import com.hstypay.framework.session.exception.SessionException;
import com.hstypay.framework.session.support.SessionContext;
import com.hstypay.framework.core.support.SpringApplicationContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Map;

/**
 * session注解的拦截处理
 * 
 * @author Tinffy
 *
 */
@Component
@Aspect
public class CheckSessionAspect implements Ordered {
	private int order = -10000;
	// session验证器，注入方式初始化属性
	@Resource
	private SessionValidator sessionValidator;

	// session处理拦截器
	private SessionInterceptor sessionInterceptor;
	private boolean isInterceptorGetted = false;

	private static final Log logger = LogFactory.getLog(CheckSessionAspect.class);

	/**
	 * 切点
	 */
	@Pointcut("@within(com.hstypay.framework.session.annotation.CheckSession)||@annotation(com.hstypay.framework.session.annotation.CheckSession)")
	public void checkSessioPointCut() {
		logger.debug("[CheckSession]Definition check session point cut.");
	};

	@Around("checkSessioPointCut()")
	public Object checkSessionMethod(ProceedingJoinPoint joinPoint) throws Throwable {
		// 检查session
		SessionValidator.SessionValidateResult result = checkSession(joinPoint);
		if (result == null)
			throw new SessionException(SessionErrorCodes.SESSION_RESULT_INVALID.toErrorCode());

		logger.debug("session check result,result=" + result.getCheckResult() + ",throwexp="
				+ result.isThrowExp());

		// 根据检查结果来判断是抛异常还是返回
		if (result.getCheckResult() == false) {
			if (result.isThrowExp()) {
				Object resObj = result.getResultObject();
				if (resObj == null)
					throw new SessionException(SessionErrorCodes.SESSION_CHK_FAIL.toErrorCode());

				// 如果需要抛异常时检查返回的结果是否为一个异常类 ，是则直接抛出
				// 不是则抛出SessionCheckException
				if (resObj instanceof Throwable)
					throw (Throwable) resObj;
				else {
					// session检查失败，打印日志，不将错误信息抛出以防止错误信息中带有系统信息被人获取
					logger.info("session check fail:" + result.getResultObject().toString());
					throw new SessionException(SessionErrorCodes.SESSION_CHK_FAIL.toErrorCode());
				}
			} else {
				return result.getResultObject();
			}
		}

		// 调用拦截器的befor处理
		doBeforInterceptor();

		// 调用原函数
		return joinPoint.proceed();
	}

	private SessionValidator.SessionValidateResult checkSession(JoinPoint joinPoint) throws Throwable {
		// 获取切点的函数信息
		MethodSignature ms = null;
		try {
			ms = (MethodSignature) joinPoint.getSignature();
		} catch (Exception e) {
			logger.error("MethodSignature类型转换错误", e);
			throw e;
		}

		// 获取访法上的CheckSession注解,如果没有则直接返回
		// 不应该出现走到此处但方法上没有注解
		CheckSession chkSessionAnn = ms.getMethod().getAnnotation(CheckSession.class);

		// 方法上找到不注解则从类上查找注解
		if (chkSessionAnn == null)
			chkSessionAnn = ms.getMethod().getDeclaringClass().getAnnotation(CheckSession.class);

		if (chkSessionAnn == null)
			return null;

		// session验证器为空直接抛异常 ，无法验证session
		if (null == sessionValidator) {
			logger.error("session validator is null");
			throw new SessionException(SessionErrorCodes.NO_SESSION_VALIDATOR.toErrorCode());
		}

		// 检查session
		SessionValidator.SessionValidateResult result = sessionValidator.validateSession(joinPoint.getTarget(),
				ms.getMethod(), joinPoint.getArgs(), chkSessionAnn.key(), chkSessionAnn.authid());

		return result;
	}
	
	/**
	 * 获取session的拦截处理器
	 * @return
	 */
	private SessionInterceptor getSessionInterceptor() {
		//如果已经获取。直接返回
		if (isInterceptorGetted)
			return sessionInterceptor;
		
		//获取bean上下文
		ApplicationContext ctx = SpringApplicationContext.getApplicationContext();
		if (ctx == null)
			return null;

		Map<String, SessionInterceptor> interceptors = BeanFactoryUtils
				.beansOfTypeIncludingAncestors(ctx, SessionInterceptor.class);

		// 找order优先级最高的拦截器
		for (Map.Entry<String, SessionInterceptor> entry : interceptors.entrySet()) {
			if (sessionInterceptor == null || entry.getValue().getOrder() < sessionInterceptor.getOrder())
				sessionInterceptor = entry.getValue();
		}

		isInterceptorGetted = true;

		return sessionInterceptor;
	}
	
	/**
	 * session检查OK后，调用拦截方法前的处理
	 */
	private void doBeforInterceptor() {
		try {
			// 获取当前session
			Session session = SessionContext.getSession();
			if (session == null)
				return;

			// 获取session的拦截器
			SessionInterceptor interceptor = getSessionInterceptor();
			if (interceptor == null)
				return;

			interceptor.before(session);

			logger.debug("doBeforInterceptor ok");
		} catch (Throwable e) {
			logger.error("doBeforInterceptor fail", e);
		}
	}
	
	/**
	 * session检测完成后释放前的处理
	 */
	private void doAfterInterceptor() {
		try {
			// 获取当前session
			Session session = SessionContext.getSession();
			if (session == null)
				return;

			// 获取session的拦截器
			SessionInterceptor interceptor = getSessionInterceptor();
			if (interceptor == null)
				return;

			interceptor.after(session);

			logger.debug("doAfterInterceptor ok");
		} catch (Throwable e) {
			logger.error("doAfterInterceptor fail", e);
		}
	}

	@After("checkSessioPointCut()")
	public void after(JoinPoint joinPoint) {
		// 在session检查函数处理完后释放上下文中当前的session信息
		try {
			// 调用拦截器的after处理
			doAfterInterceptor();

			SessionContext.releaseSession();
			logger.debug("release current session after check session");
		} catch (Throwable e) {
			logger.error("释放上文件中的session出错", e);
		}
	}

	@Override
	public int getOrder() {
		// order为负，优先级高于普通的aop处理
		return order;
	}

}
