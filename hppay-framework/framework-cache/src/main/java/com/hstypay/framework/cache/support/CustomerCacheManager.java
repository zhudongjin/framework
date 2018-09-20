/**
 * 
 */
package com.hstypay.framework.cache.support;

import com.hstypay.framework.cache.CustomSpringCache;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.util.Collection;

/**
 * @author Exception
 *
 */
public class CustomerCacheManager extends SimpleCacheManager implements ApplicationContextAware {
	private ApplicationContext applicationContext;
	
	public ApplicationContext getApplicationContext() {
		return applicationContext;
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}
	
	/**
	 * 收集需要attach到spring cache中的bean
	 * @param applicationContext
	 */
	protected void collectCacheClient(ApplicationContext applicationContext) {
		if (applicationContext == null)
			return;
		
		Collection<CustomSpringCache> cacheList = BeanFactoryUtils.beansOfTypeIncludingAncestors(
				getApplicationContext(), CustomSpringCache.class).values();
		
		for (CustomSpringCache cache:cacheList) {
			if (lookupCache(cache.getName())!=null)
				continue;
			
			if (cache.isAttach2SpringCache())
				addCache(cache);
		}
	}
	
	@Override
	public void afterPropertiesSet() {
		super.afterPropertiesSet();
		
		collectCacheClient(this.applicationContext);
	}
	
	/**
	@Override
	protected Collection<? extends Cache> loadCaches() {
		Collection<? extends Cache> caches = super.loadCaches();
		Set<Cache> cacheList = new HashSet<Cache>();
		for (Cache cache : caches) {
			//如果是基于CustomSpringCache的实现，则检查是cache是否可用
			//将载入可用的cache
			if (cache instanceof CustomSpringCache) {
				if (((CustomSpringCache)cache).isEnable()){
					cacheList.add(cache);
				}
			} else {
				cacheList.add(cache);
			}
		}
		
		return cacheList;
	}
	*/

}
