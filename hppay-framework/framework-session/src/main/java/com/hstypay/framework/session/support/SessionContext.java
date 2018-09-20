/**
 *
 */
package com.hstypay.framework.session.support;

import com.hstypay.framework.session.Session;
import com.hstypay.framework.session.SessionKeyGenerator;
import com.hstypay.framework.session.exception.SessionErrorCodes;
import com.hstypay.framework.session.exception.SessionException;
import com.hstypay.framework.cache.CustomSpringCache;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import javax.annotation.Resource;

/**
 * session上文操作类
 *
 * @author Tinffy
 */
@Component
public class SessionContext {
    @SuppressWarnings("unused")
    private static final Log logger = LogFactory.getLog(SessionContext.class);

    // session缓存的访问客户端
    private static CustomSpringCache sessionCacheClient;
    // session key生成器
    private static SessionKeyGenerator sessionKeyGenerator = new SimpleGenerator();
    // session的上下文存储
    private static ThreadLocal<Session> sessionContextHolder = new ThreadLocal<Session>();

    // 注入缓存的访问客户端实例
    @Resource(name = "sessionCacheClient")
    public void setSessionCacheClient(CustomSpringCache sessionCacheClient) {
        SessionContext.sessionCacheClient = sessionCacheClient;
    }

    // session key生成器注入实例
    @Resource
    public void setSessionKeyGenerator(SessionKeyGenerator sessionKeyGenerator) {
        SessionContext.sessionKeyGenerator = sessionKeyGenerator;
    }

    /**
     * 根据session key用client查询出缓存中的session内容
     *
     * @param key
     * @return
     * @throws Exception
     */
    public static Session loadSession(String key) throws Exception {
        //检查参数是否为null
        Assert.notNull(key, "session key must not be null");
        Assert.notNull(sessionCacheClient,
                "session cache client must not be null");
        //检查session client是否可用
        Assert.isTrue(
                sessionCacheClient.isEnable(),
                "session cache client is not open. please check cache.properties exist or 'session.memcached.open=true'");
        //从缓存中查询出内空，并解析放入到session的属性中
        SimpleSession session = new SimpleSession(key, sessionCacheClient);
        if (session.load()) {
            sessionContextHolder.set(session);
            //logger.debug("load session success");
            return session;
        }

        //session载入失败返回null
        return null;
    }

    /**
     * 从上下文中获取当前session
     * 只有loadSession或creatSession后当前上下文才会有session信息
     *
     * @return
     */
    public static Session getSession() {
        Session session = (Session) sessionContextHolder.get();
        return session;
    }

    /**
     * 创建session,并将session保存到当前上下文中，
     * 此操作并未将session的内容写入到缓存服务器中，
     * 写入缓存服务器需要显示调用Session的save方法
     *
     * @param authId
     * @return
     */
    public static Session creatSession(String authId) {
        Assert.notNull(authId, "session auth id must not be null");
        Assert.notNull(sessionKeyGenerator,
                "sessionKeyGenerator must not be null");
        //检查session client是否可用
        Assert.notNull(sessionCacheClient, "session cache client is null");
        Assert.isTrue(
                sessionCacheClient.isEnable(),
                "session cache client is not open. please check cache.properties exist or 'session.memcached.open=true'");

        //生成session key
        String key = sessionKeyGenerator.generateKey(authId);
        //生成为空则认为失败
        if (StringUtils.isBlank(key))
            throw new SessionException(SessionErrorCodes.NULL_SESSION_KEY.toErrorCode());

        //实例化一个session并写入到当前上下文中
        SimpleSession session = new SimpleSession(key, authId, sessionCacheClient);
        sessionContextHolder.set(session);

        return session;
    }

    /**
     * 清除当前上下文中的session
     */
    public static void releaseSession() {
        sessionContextHolder.remove();
    }
}
