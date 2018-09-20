package com.hstypay.framework.cache.support;

import org.springframework.aop.framework.AopProxyUtils;
import org.springframework.aop.support.AopUtils;
import org.springframework.cache.Cache;
import org.springframework.context.expression.AnnotatedElementKey;
import org.springframework.context.expression.CachedExpressionEvaluator;
import org.springframework.context.expression.MethodBasedEvaluationContext;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.EvaluationException;
import org.springframework.expression.Expression;
import org.springframework.util.Assert;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


/**
 * 复制spring 4.1.6 的org.springframework.cache.interceptor.ExpressionEvaluator相关实现代码
 * 由 于spring的cache的key计算支持SpEL的相关处理代码为私有类
 * 
 * @author Tinffy Lee
 *
 */
public class CacheKeyExpEvaluator extends CachedExpressionEvaluator {

	/**
	 * Indicate that there is no result variable.
	 */
	public static final Object NO_RESULT = new Object();

	/**
	 * Indicate that the result variable cannot be used at all.
	 */
	public static final Object RESULT_UNAVAILABLE = new Object();

	/**
	 * The name of the variable holding the result object.
	 */
	public static final String RESULT_VARIABLE = "result";

	// shared param discoverer since it caches data internally
	private final ParameterNameDiscoverer paramNameDiscoverer = new DefaultParameterNameDiscoverer();

	private final Map<ExpressionKey, Expression> keyCache = new ConcurrentHashMap<ExpressionKey, Expression>(64);

	private final Map<ExpressionKey, Expression> conditionCache = new ConcurrentHashMap<ExpressionKey, Expression>(64);

	private final Map<ExpressionKey, Expression> unlessCache = new ConcurrentHashMap<ExpressionKey, Expression>(64);

	private final Map<AnnotatedElementKey, Method> targetMethodCache =
			new ConcurrentHashMap<AnnotatedElementKey, Method>(64);


	/**
	 * Create an {@link EvaluationContext} without a return value.
	 * @see #createEvaluationContext(Collection, Method, Object[], Object, Class, Object)
	 */
	public EvaluationContext createEvaluationContext(Collection<? extends Cache> caches,
			Method method, Object[] args, Object target, Class<?> targetClass) {

		return createEvaluationContext(caches, method, args, target, targetClass, NO_RESULT);
	}

	/**
	 * Create an {@link EvaluationContext}.
	 * @param caches the current caches
	 * @param method the method
	 * @param args the method arguments
	 * @param target the target object
	 * @param targetClass the target class
	 * @param result the return value (can be {@code null}) or
	 * {@link #NO_RESULT} if there is no return at this time
	 * @return the evaluation context
	 */
	public EvaluationContext createEvaluationContext(Collection<? extends Cache> caches,
			Method method, Object[] args, Object target, Class<?> targetClass, Object result) {

		CacheExpressionRootObject rootObject = new CacheExpressionRootObject(caches,
				method, args, target, targetClass);
		Method targetMethod = getTargetMethod(targetClass, method);
		CacheEvaluationContext evaluationContext = new CacheEvaluationContext(rootObject,
				targetMethod, args, this.paramNameDiscoverer);
		if (result == RESULT_UNAVAILABLE) {
			evaluationContext.addUnavailableVariable(RESULT_VARIABLE);
		}
		else if (result != NO_RESULT) {
			evaluationContext.setVariable(RESULT_VARIABLE, result);
		}
		return evaluationContext;
	}

	public Object key(String keyExpression, AnnotatedElementKey methodKey, EvaluationContext evalContext) {
		return getExpression(this.keyCache, methodKey, keyExpression).getValue(evalContext);
	}
	
	public Object key(String keyExpression, Collection<? extends Cache> caches,
			Method method, Object[] args, Object target) {
		Class<?> targetClass = getTargetClass(target);
		AnnotatedElementKey methodKey = new AnnotatedElementKey(method, targetClass);
		EvaluationContext evalContext = createEvaluationContext(caches, method, args, target, targetClass, NO_RESULT);
		
		return key(keyExpression, methodKey, evalContext);
	}

	public boolean condition(String conditionExpression, AnnotatedElementKey methodKey, EvaluationContext evalContext) {
		return getExpression(this.conditionCache, methodKey, conditionExpression).getValue(evalContext, boolean.class);
	}

	public boolean unless(String unlessExpression, AnnotatedElementKey methodKey, EvaluationContext evalContext) {
		return getExpression(this.unlessCache, methodKey, unlessExpression).getValue(evalContext, boolean.class);
	}
	
	private Class<?> getTargetClass(Object target) {
		Class<?> targetClass = AopProxyUtils.ultimateTargetClass(target);
		if (targetClass == null && target != null) {
			targetClass = target.getClass();
		}
		return targetClass;
	}

	/**
	 * Clear all caches.
	 */
	void clear() {
		this.keyCache.clear();
		this.conditionCache.clear();
		this.unlessCache.clear();
		this.targetMethodCache.clear();
	}

	private Method getTargetMethod(Class<?> targetClass, Method method) {
		AnnotatedElementKey methodKey = new AnnotatedElementKey(method, targetClass);
		Method targetMethod = this.targetMethodCache.get(methodKey);
		if (targetMethod == null) {
			targetMethod = AopUtils.getMostSpecificMethod(method, targetClass);
			if (targetMethod == null) {
				targetMethod = method;
			}
			this.targetMethodCache.put(methodKey, targetMethod);
		}
		return targetMethod;
	}
	
	class CacheExpressionRootObject {

		private final Collection<? extends Cache> caches;

		private final Method method;

		private final Object[] args;

		private final Object target;

		private final Class<?> targetClass;


		public CacheExpressionRootObject(
				Collection<? extends Cache> caches, Method method, Object[] args, Object target, Class<?> targetClass) {

			Assert.notNull(method, "Method is required");
			Assert.notNull(targetClass, "targetClass is required");
			this.method = method;
			this.target = target;
			this.targetClass = targetClass;
			this.args = args;
			this.caches = caches;
		}


		public Collection<? extends Cache> getCaches() {
			return this.caches;
		}

		public Method getMethod() {
			return this.method;
		}

		public String getMethodName() {
			return this.method.getName();
		}

		public Object[] getArgs() {
			return this.args;
		}

		public Object getTarget() {
			return this.target;
		}

		public Class<?> getTargetClass() {
			return this.targetClass;
		}

	}
	
	class CacheEvaluationContext extends MethodBasedEvaluationContext {

		private final List<String> unavailableVariables;

		CacheEvaluationContext(Object rootObject, Method method, Object[] args,
				ParameterNameDiscoverer paramDiscoverer) {

			super(rootObject, method, args, paramDiscoverer);
			this.unavailableVariables = new ArrayList<String>();
		}

		/**
		 * Add the specified variable name as unavailable for that context. Any expression trying
		 * to access this variable should lead to an exception.
		 * <p>This permits the validation of expressions that could potentially a variable even
		 * when such variable isn't available yet. Any expression trying to use that variable should
		 * therefore fail to evaluate.
		 */
		public void addUnavailableVariable(String name) {
			this.unavailableVariables.add(name);
		}


		/**
		 * Load the param information only when needed.
		 */
		@Override
		public Object lookupVariable(String name) {
			if (this.unavailableVariables.contains(name)) {
				throw new VariableNotAvailableException(name);
			}
			return super.lookupVariable(name);
		}

	}
	
	@SuppressWarnings("serial")
	class VariableNotAvailableException extends EvaluationException {

		private final String name;

		public VariableNotAvailableException(String name) {
			super("Variable '" + name + "' is not available");
			this.name = name;
		}


		public String getName() {
			return name;
		}
	}
}