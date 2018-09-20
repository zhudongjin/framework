/**
 * 
 */
package com.hstypay.framework.session;

/**
 * SessionID生成器接口
 * 
 * @author Exception
 *
 */
public interface SessionKeyGenerator {
	
	/**
	 * 生产一个Session ID
	 * @return
	 */
    String generateKey(String authId);
}
