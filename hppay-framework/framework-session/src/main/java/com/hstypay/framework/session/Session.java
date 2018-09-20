/**
 * 
 */
package com.hstypay.framework.session;

import java.util.Set;

/**
 * 
 * Session接口
 * 
 * @author Exception
 * 
 */
public interface Session {
	
	//存缓存是的属性Key
	String SESSION_AUTHID_ATTR_NAME = "session_authid";
	String SESSION_STATE_ATTR_NAME = "session_state";
	String SESSION_TIMESTAMP = "session_timestamp";
	String SESSION_EXPIRE_TIME = "session_expiretime";
	int SESSION_DELAY_EXPIRE_RATE = 70;//70表示70%
	int DEFAULT_EXPIRE_TIME = 24*60*60;//默认过期时间
	
	
	//Cookie里的属性Key
	String SESSION_KEY = "SKEY";
	String SESSION_AUTHID_KEY = "SAUTHID";

	//session状态的有效值
	String SESSION_VALID_STATE = "1";
	
	//默认获取对象中的属性
	String SESSION_KEY_ARG_NAME = "sessionKey";
	String SESSION_AUTH_ID_ARG_NAME = "sessionAuthId";
	
	
	/**
	 * 获取Session的Key
	 * 
	 * @return
	 */
	String getKey();
	
	/**
	 * 载入保存的session内容,如果返回false表示缓存中没有，当无法正常访问缓存时会抛出异常
	 * @return
	 * @throws Exception
	 */
	boolean load() throws Exception;
	
	/**
	 * 保存Session，将当前session中的所有属性写回到session缓存中
	 * 此操作应只在session创建时使用。session获取和检查处不应调用
	 * 当业务确实需要在获取session后又重新写回时请慎重考虑是否能过
	 * 其他方式共享数据。
	 * 
	 * @param expired 过期时间,单位秒
	 * @return
	 */
	void save(int expired) throws RuntimeException;

	/**
	 * 验长过期时间，仅将cache的过期时间更新，cache中的内容不变
	 * 无内容重写动作
	 * @param expired
	 */
	boolean delayExpire(int expired);
	
	/**
	 * 自动验长过期时间，达到需要更新时间点时将cache中的写入时间更新，将整
	 * 个内容重新写回到cache。因此有内容重写动作
	 * @return
	 */
	boolean autoDelayExpire();
	
	/**
	 * 根据ID删除Session
	 */
	void delete();
	
	/**
	 * 获取属性值
	 * @param attributeName
	 * @return
	 */
	Object getAttribute(String attributeName);

	/**
	 * 获取属性名称
	 * @return
	 */
	Set<String> getAttributeNames();

	/**
	 * 设置属性
	 * @param attributeName
	 * @param attributeValue
	 */
	void setAttribute(String attributeName, Object attributeValue);

	/**
	 * 移除属性
	 * @param attributeName
	 */
	Object removeAttribute(String attributeName);
}
