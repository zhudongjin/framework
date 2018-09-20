package com.hstypay.framework.session;

import java.lang.reflect.Method;

/**
 * session验证器接口
 * @author Tinffy
 *
 */
public interface SessionValidator {
	
	/**
	 * 验证session操作
	 * @param target
	 * @param method
	 * @param args
	 * @param sessionKeyName
	 * @param sessionAuthIdName
	 * @return
	 * @throws Exception
	 */
	SessionValidateResult validateSession(Object target, Method method, Object[] args, String sessionKeyName, String sessionAuthIdName) throws Exception;
	
	//session验证返回结果
	class SessionValidateResult {
		private boolean checkResult;//session检查是否通过
		private Object resultObject;//检查返回的结果,检查不通过时通过该对象返回信息
		private boolean throwExp;//检查不通过时是否直接抛异常出去

		public boolean isThrowExp() {
			return throwExp;
		}

		public boolean getCheckResult() {
			return checkResult;
		}

		public Object getResultObject() {
			return resultObject;
		}

		public SessionValidateResult(boolean checkResult, Object resultObject, boolean bThrowExp) {
			this.checkResult = checkResult;
			this.resultObject = resultObject;
			this.throwExp = bThrowExp;
		}
	}
}
