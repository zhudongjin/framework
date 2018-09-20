/**
 * 
 */
package com.hstypay.framework.session;

import java.lang.reflect.Method;

/**
 * session当前运行环境的接口，主要是用于定义从不同运行平台
 * 获取session key和authid。因为不同运行平台从http上下文中
 * 获取cookie的操作不同。例如对spring mvc、struts、play不
 * 同平台获取cookie的方式不同，不同平台继承该接口实现即可
 * 
 * @author Exception
 *
 */
public interface SessionEnvContext {

	/**
	 * 从当前运行的上下文信息中获取SessionKey与SesionUserId
	 * 
	 * @return String[],第一个元素是SessionKey,第二个元素是AuthId
	 */
	String[] getSessionKeyAndAuthId(Object target, Method method, Object[] args, String sessionKeyName, String sessionAuthIdName);

}
