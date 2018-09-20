package com.hstypay.framework.cache.exception;

import com.hstypay.sandbox.error.ErrorCode;
import com.hstypay.sandbox.error.ErrorCodeType;

/**
 * 公共异常信息
 *
 * @author Vincent
 * @version 1.0 2017-06-27 09:45
 */
public enum CacheErrorCodes implements ErrorCodeType {

    CACHE_EXCEPTION("cache.exception", "缓存系统出现异常"),
    CACHE_DISABLE("cache.disable", "缓存不可用"),
    GET_VALUE_FAIL("cache.get.value.fail", "获取cache失败"),
    VALUE_TYPE_INVALID("cache.type.invalid", "cache数据的类型错误"),
    TOUCH_VALUE_FAIL("cache.touch.fail", "cache touch操作失败"),//
    DELETE_KEY_FAIL("cache.delete.fail", "cache delete操作失败"),
    FLUSHALL_FAIL("cache.flushall.fail", "cache flush操作失败"),
    PUT_FAIL("cache.put.fail", "cache 存储操作失败"),
    CONFIG_INCORRECT("cache.config.incorrect", "cache 配置不正确"),
    INIT_CACHE("cache.init.fail", "初始化cache失败");;

    private final String code;

    private final String message;

    CacheErrorCodes(final String code, final String message) {
        this.code = code;
        this.message = message;
    }

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public ErrorCode toErrorCode(Object... args) {
        return null;
    }
}
