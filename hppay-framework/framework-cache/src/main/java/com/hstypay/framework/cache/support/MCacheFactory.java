package com.hstypay.framework.cache.support;

import com.hstypay.framework.cache.CacheClientFactory;

/**
 * ClassName: MCacheFactory 
 * Function: MCacheFactory 
 * date: 2017年7月25日 下午2:36:34
 *
 * @author hansong
 */
public class MCacheFactory {
	
	private static CacheClient client;
	
	public static CacheClient getFactory(){
		if(client == null){
			client = createClient();
		}
		return client;
	}
	private static CacheClient createClient(){
		return CacheClientFactory.createClient("classpath:cache.properties", "default");
	}
}
