package com.hstypay.framework.cache.aspect;

import com.hstypay.framework.cache.CustomSpringCache;
import com.hstypay.framework.cache.annotation.CacheAttribute;
import com.hstypay.framework.cache.exception.NoUniqueCacheAnnException;
import com.hstypay.framework.cache.support.CacheKeyExpEvaluator;
import com.hstypay.framework.core.support.SpringApplicationContext;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Collection;

/**
 * cache附加属性的拦截处理
 *
 * @author Tinffy
 */
@Component
@Aspect
public class CacheAttributeAspect implements Ordered {

    private static final Logger logger = LoggerFactory.getLogger(CacheAttributeAspect.class);

    private static final String DEFAULT_CACHE_MANAGER_BEAN_NAME = "cacheManager";
    private static final String DEFAULT_CACHE_KEYGENERATOR_BEAN_NAME = "keyGenerator";

    private static boolean isLoadDefault = false;
    //默认的key生成器
    private static KeyGenerator defKeyGen = null;
    //默认的cache manager
    private static CacheManager defCacheMgr = null;

    //spel key生成对象
    private CacheKeyExpEvaluator evaluator = new CacheKeyExpEvaluator();

    /**
     * 切点,匹配所有CacheAttribute注解的方法
     */
    @Pointcut("@annotation(com.hstypay.framework.cache.annotation.CacheAttribute)")
    public void expiredCachePointCut() {
        logger.debug("[CacheAttribute]Definition expired cache point cut.");
    }

    /**
     * 切点，匹配所有com.hstypay.framework.cache.annotation.Cacheable前缀的注解方法
     */
    @Pointcut("execution(@com.hstypay.framework.cache.annotation.Cacheable* * *(..))")
    public void combinedCachePointCut() {

    }

    /**
     * Cacheable前的cache过期时间预处理
     *
     * @param joinPoint
     */
    @Before("expiredCachePointCut()||combinedCachePointCut()")
    public void before(JoinPoint joinPoint) {
        try {
            Signature sig = joinPoint.getSignature();
            MethodSignature methodSig = null;
            if (sig instanceof MethodSignature)
                methodSig = (MethodSignature) sig;

            if (methodSig == null)
                return;

            // 获取CacheAttribute和Cacheable注解信息
            Annotation[] anns = getCacheAnnotation(joinPoint);
            if (anns == null || anns.length != 2) {
                logger.error("method not have CacheAttribute Annotation or Cacheable* combined Annotation");
                return;
            }

            Cacheable cacheable = (Cacheable) anns[0];
            CacheAttribute cacheAttribute = (CacheAttribute) anns[1];

            if (cacheAttribute == null || cacheable == null) {
                logger.error("method not have CacheAttribute and Cacheable Annotation");
                return;
            }

            // 获取注解设定的过期时间
            int expired = cacheAttribute.expire();
            // 载入默认的cacheManager和keyGenerator
            loadDefaultCacheConf();

            // 如果Cacheable指定了keyGenerator则使用指定的，否则使用默认的
            KeyGenerator keyGenerator;
            if (!StringUtils.isBlank(cacheable.keyGenerator())) {
                keyGenerator = SpringApplicationContext.getBean(cacheable.keyGenerator(), KeyGenerator.class);
            } else {
                keyGenerator = defKeyGen;
            }

            // 如果Cacheable指定了cacheManager则使用的指定的，否则使用默认的
            CacheManager cacheManager;
            if (!StringUtils.isBlank(cacheable.cacheManager())) {
                cacheManager = SpringApplicationContext.getBean(cacheable.cacheManager(), CacheManager.class);
            } else {
                cacheManager = defCacheMgr;
            }

            if (cacheManager == null || keyGenerator == null) {
                logger.error("cacheManager or keyGenerator is null");
                return;
            }

            // 获取cache的名称列表
            Collection<Cache> caches = this.getCaches(cacheable.value(), cacheManager);

            // 生成key
            Object cacheKey;
            if (!StringUtils.isBlank(cacheable.key())) {
                // 如果Cacheable指定了key，则按spring规则生成key
                cacheKey = evaluator.key(cacheable.key(), caches, methodSig.getMethod(), joinPoint.getArgs(), joinPoint.getTarget());
            } else {
                // 未指定则使用keyGenerator生成key
                cacheKey = keyGenerator.generate(joinPoint.getTarget(), methodSig.getMethod(), joinPoint.getArgs());
            }

            // 对使用的cache预先设定key的过期时间
            for (Cache cache : caches) {
                if (cache instanceof CustomSpringCache) {
                    CustomSpringCache acCache = (CustomSpringCache) cache;
                    acCache.putKeyExpired((String) cacheKey, expired);
                }
            }
        } catch (Throwable e) {
            logger.error("CacheAttribute set cache key expired failed.", e);
        }
    }

    /**
     * 扫描方法上的注解，获取Cacheable和CacheAttribute的注解信息
     * 为支持组合注解，扫描非Cacheable和CacheAttribute的注解类型上的注解
     *
     * @param joinPoint
     * @return
     */
    private Annotation[] getCacheAnnotation(JoinPoint joinPoint) {
        Cacheable cacheable = null;
        CacheAttribute cacheAttr = null;

        Signature sig = joinPoint.getSignature();
        MethodSignature methodSig = null;
        if (sig instanceof MethodSignature)
            methodSig = (MethodSignature) sig;

        if (methodSig == null)
            return null;

        //获取方法上所有的注解
        Annotation[] methodAnns = methodSig.getMethod().getAnnotations();
        for (Annotation ann : methodAnns) {
            Cacheable cacheAnn = null;
            CacheAttribute cacheAttrAnn = null;

            if (ann instanceof Cacheable)
                cacheAnn = (Cacheable) ann;
            else if (ann instanceof CacheAttribute)
                cacheAttrAnn = (CacheAttribute) ann;
            else {
                //注解是非Cacheable和CacheAttribute的注解
                //则扫描注解上的注解是否有Cacheable或CacheAttribute
                Annotation[] metaAnns = getMetaAnnotation(ann);
                if (metaAnns != null) {
                    cacheAnn = (Cacheable) metaAnns[0];
                    cacheAttrAnn = (CacheAttribute) metaAnns[1];
                }
            }

            //检查同一方法上是否有多个Cacheable注解
            if (cacheable != null && cacheAnn != null) {
                throw new NoUniqueCacheAnnException("not unique Cacheable annotation");
            } else {
                //如果注解或组合注解上有Cacheable，则作为当前方法的Cacheable注解
                if (cacheAnn != null)
                    cacheable = cacheAnn;
            }

            //检查同一方法上是否有多个CacheAttribute注解
            if (cacheAttr != null && cacheAttrAnn != null) {
                throw new NoUniqueCacheAnnException("not unique CacheAttribute annotation");
            } else {
                //如果注解或组合注解上有CacheAttribute，则作为当前方法的CacheAttribute注解
                if (cacheAttrAnn != null)
                    cacheAttr = cacheAttrAnn;
            }
        }

        if (cacheable == null && cacheAttr == null)
            return null;
        else
            return new Annotation[]{cacheable, cacheAttr};
    }

    /**
     * 扫描组合注解
     *
     * @param ann
     * @return
     */
    private Annotation[] getMetaAnnotation(Annotation ann) {
        Cacheable cacheable;
        CacheAttribute cacheAttr;

        cacheable = ann.annotationType().getAnnotation(Cacheable.class);
        cacheAttr = ann.annotationType().getAnnotation(CacheAttribute.class);

        if (cacheable == null && cacheAttr == null)
            return null;
        else
            return new Annotation[]{cacheable, cacheAttr};
    }

    // 从cacheManager中获取Cacheable指定的key
    private Collection<Cache> getCaches(String[] cacheNames,
                                        CacheManager cacheManager) {
        Collection<Cache> caches = new ArrayList<Cache>(1);
        for (String cacheName : cacheNames) {
            Cache cache = cacheManager.getCache(cacheName);
            if (cache != null) {
                caches.add(cache);
            }
        }

        return caches;
    }

    /**
     * 载入默认的CacheManager和KeyGenerator配置
     */
    private static void loadDefaultCacheConf() {
        if (isLoadDefault) {
            return;
        }
        try {
            defCacheMgr = SpringApplicationContext.getBean(DEFAULT_CACHE_MANAGER_BEAN_NAME, CacheManager.class);
            defKeyGen = SpringApplicationContext.getBean(DEFAULT_CACHE_KEYGENERATOR_BEAN_NAME, KeyGenerator.class);
        } catch (Throwable t) {
            logger.error("get default cacheManager and keyGenerator failed", t);
        }

        //载入失败也标记已载入，载入失败不需要每次都重新载入，因此重新载入也是失败
        isLoadDefault = true;
    }

    @Override
    public int getOrder() {
        return -1;
    }

}
