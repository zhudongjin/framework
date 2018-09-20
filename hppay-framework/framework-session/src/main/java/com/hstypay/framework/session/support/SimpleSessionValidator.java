package com.hstypay.framework.session.support;

import com.hstypay.framework.session.Session;
import com.hstypay.framework.session.SessionEnvContext;
import com.hstypay.framework.session.SessionValidator;
import com.hstypay.framework.session.exception.SessionErrorCodes;
import com.hstypay.framework.session.exception.SessionException;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.lang.reflect.Method;

@Component
public class SimpleSessionValidator implements SessionValidator {
	@Resource
	private SessionEnvContext envContext;

	private static final Log logger = LogFactory
			.getLog(SimpleSessionValidator.class);

	@Override
	public SessionValidateResult validateSession(Object target, Method method,
			Object[] args, String sessionKeyName, String sessionAuthIdName)
			throws Exception {

		// 检查注解的上下文参数
		//target == null || method == null || 
		if (sessionKeyName == null
				|| sessionAuthIdName == null) {
			logger.error("session注解拦截上下文参数为空");
			throw new SessionException(SessionErrorCodes.NULL_POINTER.toErrorCode());
		}

		if (envContext == null) {
			logger.error("session environment context is null");
			throw new SessionException(SessionErrorCodes.NULL_POINTER.toErrorCode());
		}

		boolean checkResult = false;
		String errMsg = "";

		do {
			// 获取客户端的session key和authid
			String[] sessionArr = envContext.getSessionKeyAndAuthId(target,
					method, args, sessionKeyName, sessionAuthIdName);

			if (sessionArr == null 
				|| sessionArr.length != 2
				|| sessionArr[0] == null
				|| sessionArr[1] == null) {
				checkResult = false;
				errMsg = "can not get session key from run environment context";
				logger.error(errMsg);
				break;
			}

			String sessionKey = sessionArr[0];
			String sessionAuthId = sessionArr[1];

			// 获取缓存服务中的session内容
			Session session = SessionContext.loadSession(sessionKey);
			if (session == null) {
				checkResult = false;
				errMsg = "Can not get session for id " + sessionKey;
				logger.error(errMsg);
				break;
			}
			
			//检查authid是否相同
			Object authId = session
					.getAttribute(Session.SESSION_AUTHID_ATTR_NAME);
			if (StringUtils.isBlank((String) authId)
					|| StringUtils.isBlank(sessionAuthId)
					|| !sessionAuthId.equals(authId)) {

				logger.debug("authId check failed,Server session authId="
						+ authId + ",client authId=" + sessionAuthId);

				checkResult = false;
				errMsg = "session authid check fail, session authid is null or not eq";
				logger.error(errMsg);
				break;
			}
			
			//检查session state是否有效(1有效)
			Object sessionState = session
					.getAttribute(Session.SESSION_STATE_ATTR_NAME);
			if (sessionState == null
					|| !sessionState.equals(Session.SESSION_VALID_STATE)) {
				checkResult = false;
				errMsg = "session state check fail, session state is null or not eq 1";
				logger.error(errMsg);
				break;
			}
			
			//前面的检查走完了，此处说明检查通过
			//延长session有效时间
			session.autoDelayExpire();
			checkResult = true;
			
		} while (false);

		return new SessionValidateResult(checkResult, errMsg, true);
	}

}
