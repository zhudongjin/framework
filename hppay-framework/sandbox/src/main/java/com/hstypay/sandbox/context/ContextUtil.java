package com.hstypay.sandbox.context;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 上下文工具
 *
 * @author vincent
 * @version 1.0 2016年12月15日 下午1:58:24
 */
public class ContextUtil {

    private static final Logger logger = LoggerFactory.getLogger(ContextUtil.class);

    protected static ThreadLocal<Context> contextThreadLocal = new ThreadLocal<Context>();

    /**
     * 设置当前的Context
     *
     * @param context
     */
    public static void setContext(Context context) {
        contextThreadLocal.set(context);
    }

    /**
     * 取得当前的Context，如果不存在，默认建立一个AppContext
     *
     * @return
     */
    public static Context getContext() {
        Context ctx = contextThreadLocal.get();
        if (ctx == null) {
            if (logger.isDebugEnabled()) {
                logger.debug("当前线程不存在Context，开始创建默认的SimpleContext");
            }
            ctx = new SimpleContext();
            contextThreadLocal.set(ctx);
        }
        return ctx;
    }

    public static void removeContext() {
        contextThreadLocal.get().release();
        contextThreadLocal.remove();
    }

    /**
     * 判断当前是否有Context
     *
     * @return
     */
    public static boolean hasContext() {
        return (contextThreadLocal.get() != null);
    }
}