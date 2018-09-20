/**
 *
 */
package com.hstypay.framework.session.support;

import com.hstypay.framework.session.SessionEnvContext;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * session当前运行环境spring mvc实现
 * <p>
 * 用于从当前运行的上下文信息中获取session的key和authid
 *
 * @author Exception
 */
@Component
public class SessionEnviromentContext implements SessionEnvContext {

    private static final Log logger = LogFactory.getLog(SessionEnviromentContext.class);

    @Override
    public String[] getSessionKeyAndAuthId(Object target, Method method,
                                           Object[] args, String sessionKeyName, String sessionAuthIdName) {
        Assert.notNull(sessionKeyName, "session key name is null");
        Assert.notNull(sessionAuthIdName, "session key name is null");

        String[] keyAndAuthId = null;

        /**
         * 获取httprequest对象
         * 需要在web.xml中增加一个监听
         * <listener>
         * <listener-class>    
         *   org.springframework.web.context.request.RequestContextListener    
         * </listener-class>    
         * </listener>
         */
        ServletRequestAttributes reqAttr = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes());
        if (reqAttr != null) {
            HttpServletRequest request = reqAttr.getRequest();
            if (request == null) {
                logger.debug("HttpServletRequest is null");
                return null;
            }

            keyAndAuthId = getKeyAndAuthIdFromHttpRequest(request,
                    sessionKeyName, sessionAuthIdName);
        } else {
            logger.debug("ServletRequestAttributes is null");
        }

        return keyAndAuthId;
    }

    /**
     * 从HttpServletRequest中获取cookie信息
     *
     * @param request
     * @param keyName
     * @param authIdName
     * @return
     */
    private String[] getKeyAndAuthIdFromHttpRequest(HttpServletRequest request,
                                                    String keyName, String authIdName) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            logger.debug("Cookie is null");
            return null;
        }

        String keyValue = null;
        String authIdValue = null;
        //从cookie中拿取session key和authid
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals(keyName))
                keyValue = cookie.getValue();

            if (cookie.getName().equals(authIdName))
                authIdValue = cookie.getValue();
        }

        if (keyValue != null && authIdValue != null) {
            return new String[]{keyValue, authIdValue};
        } else {
            logger.debug("session key or authid is null,key=" + keyValue + ",authid" + authIdValue);
            return null;
        }
    }

    @SuppressWarnings("unused")
    public static void main(String[] args) {
        class Ta {
            private String listid;
            private String spid;

            public Ta(String a1, String a2) {
                listid = a1;
                spid = a2;
            }
        }

        Map<String, String> ss = new HashMap<String, String>();
        ss.put("aa", "dd");
        ss.put("123", "dred");
        System.out.println("test");

        System.out.println(ToStringBuilder.reflectionToString(ss,
                ToStringStyle.SIMPLE_STYLE));

        Ta a = new Ta("2224", "ddd");
        System.out.println(ToStringBuilder.reflectionToString(a,
                ToStringStyle.SIMPLE_STYLE));
    }
}
