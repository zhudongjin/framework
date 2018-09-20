/**
 * 
 */
package com.hstypay.framework.session.support;

import com.hstypay.framework.session.Session;
import com.hstypay.framework.session.exception.SessionErrorCodes;
import com.hstypay.framework.session.exception.SessionException;
import com.hstypay.framework.cache.CustomSpringCache;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.cache.Cache.ValueWrapper;
import org.springframework.util.Assert;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * 
 * @author Exception
 *
 */
public class SimpleSession implements Session {

	private static final Log logger = LogFactory.getLog(SimpleSession.class);
	private static String DEFAULT_CODEING = "utf-8";
	// session的key
	private String key;
	// private String authId;

	// session属性
	private Map<String, Object> sessionAttrs = new HashMap<String, Object>();

	// session的缓存访问客户端
	private CustomSpringCache sessionCacheClient;

	// Constructor
	public SimpleSession(String key, String authId, CustomSpringCache sessionCacheClient) {
		Assert.notNull(key, "session key must be not null");
		Assert.notNull(authId, "session auth id must be not null");
		Assert.notNull(sessionCacheClient, "sessionCacheClient must be not null");

		this.key = key;
		this.sessionCacheClient = sessionCacheClient;

		setAttribute(Session.SESSION_AUTHID_ATTR_NAME, authId);
		setAttribute(Session.SESSION_STATE_ATTR_NAME, Session.SESSION_VALID_STATE);
	}

	public SimpleSession(String key, CustomSpringCache customSpringCache) {
		Assert.notNull(key, "session key must be not null");
		Assert.notNull(customSpringCache, "sessionCacheClient must be not null");

		this.key = key;
		this.sessionCacheClient = customSpringCache;
		setAttribute(Session.SESSION_STATE_ATTR_NAME, Session.SESSION_VALID_STATE);
	}

	@Override
	public void save(int expired) throws RuntimeException {
		try {
			Assert.isTrue(expired > 0, "session expired must be greater than 0");

			// 将写入session的当前时间和过期时长写入session中
			Long timestamp = System.currentTimeMillis() / 1000;
			setAttribute(Session.SESSION_TIMESTAMP, timestamp);
			setAttribute(Session.SESSION_EXPIRE_TIME, new Integer(expired));

			StringBuilder cacheContent = new StringBuilder(128);
			boolean first = true;
			// 拼接Session的内容
			for (Map.Entry<String, Object> entry : this.sessionAttrs.entrySet()) {
				String value = entry.getValue().toString();
				if (first) {
					first = false;
				} else {
					cacheContent.append("&");
				}

				// 对value进行urlencode
				cacheContent.append(entry.getKey()).append("=").append(encode(value));
			}

			logger.info("save session success. key=" + key);

			sessionCacheClient.put(key, cacheContent.toString(), expired);
		} catch (Throwable t) {
			logger.error("save session fail:", t);
			throw new SessionException(SessionErrorCodes.SAVE_SESSION.toErrorCode(), t);
		}
	}

	@Override
	public boolean delayExpire(int expired) {
		try {
			Assert.isTrue(expired > 0, "session expired must be greater than 0");
			boolean res = sessionCacheClient.touch(key, expired);

			logger.debug(key + " delay session expire result=" + res);

			if (res == false)
				logger.error("delay session expire result is false, key=" + key);

			return res;
		} catch (Throwable t) {
			logger.error("delay session expire failed, key=" + key, t);
		}

		return false;
	}

	@Override
	public boolean autoDelayExpire() {
		try {
			// 获取session中保存的时间戳
			String strTimestamp = (String) getAttribute(Session.SESSION_TIMESTAMP);
			Long sessionTimestamp = 0L;
			if (!StringUtils.isBlank(strTimestamp))
				sessionTimestamp = Long.valueOf(strTimestamp);

			// 获取session中的过期时长
			String strExpire = (String) getAttribute(Session.SESSION_EXPIRE_TIME);
			Integer expire = 0;
			if (!StringUtils.isBlank(strExpire))
				expire = Integer.valueOf(strExpire);

			// 计算重写过期时间的时间点，session写入时间+时长*比例
			Long dueTime = sessionTimestamp + (expire * Session.SESSION_DELAY_EXPIRE_RATE) / 100;
			// 获取当前时间
			Long curTimestamp = System.currentTimeMillis() / 1000;

			// 如果当前时间小于需要重写的时间则直接返回
			if (curTimestamp < dueTime)
				return true;

			// 如果session中写入的过期时长为空或0，则使用默认时长，否则使用原来的过期时长
			if (expire <= 0)
				expire = Session.DEFAULT_EXPIRE_TIME;

			// 将内容重新写入session
			save(expire);

			return true;
		} catch (Throwable t) {
			logger.error("auto delay session expire failed, key=" + key, t);
		}

		return false;
	}

	@Override
	public boolean load() throws Exception {
		// 从缓存服务获取内容
		ValueWrapper valueWrapper = sessionCacheClient.get(key);

		if (valueWrapper == null) {
			logger.error("session data by key(" + key + ") is null");
			return false;
		}

		String sessionContent = (String) valueWrapper.get();
		if (StringUtils.isBlank(sessionContent)) {
			logger.error("session data by key(" + key + ") is null or empty");
			return false;
		}

		logger.debug("session[" + key + "]:" + sessionContent);

		// 以k=v&k=v的格式解析获取的缓存内容
		String[] contentArr = sessionContent.split("&");
		for (String str : contentArr) {
			String[] keyValueArr = str.split("=");
			if (keyValueArr.length == 2 && !StringUtils.isBlank(keyValueArr[0]))
				setAttribute(keyValueArr[0], decode(keyValueArr[1]));
		}

		return true;
	}

	@Override
	public void delete() {
		sessionCacheClient.evict(key);
	}

	@Override
	public Object getAttribute(String attributeName) {
		return sessionAttrs.get(attributeName);
	}

	@Override
	public Set<String> getAttributeNames() {
		return sessionAttrs.keySet();
	}

	@Override
	public void setAttribute(String attributeName, Object attributeValue) {
		Assert.notNull(attributeName, "session attribute name must be not null");
		Assert.notNull(attributeValue, "session attribute value must be not null");

		sessionAttrs.put(attributeName, attributeValue);
	}

	@Override
	public Object removeAttribute(String attributeName) {
		return sessionAttrs.remove(attributeName);
	}

	public String getKey() {
		return key;
	}

	// url编码
	private static String encode(String value) throws Exception {
		return URLEncoder.encode(value, DEFAULT_CODEING);
	}

	// url解码
	private static String decode(String value) throws Exception {
		return URLDecoder.decode(value, DEFAULT_CODEING);
	}

	public static void main(String[] args) {
		try {
			System.out.println(encode("%3Daaaa=&如果"));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
