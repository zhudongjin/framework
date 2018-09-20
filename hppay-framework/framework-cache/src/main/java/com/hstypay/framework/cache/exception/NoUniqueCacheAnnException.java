package com.hstypay.framework.cache.exception;

import com.hstypay.sandbox.exception.BusinessException;

/**
 * @author Vincent
 * @version 1.0 2017-06-27 22:58
 */
public class NoUniqueCacheAnnException extends BusinessException {

    private static final long serialVersionUID = 1L;

    public NoUniqueCacheAnnException() {
        super();
    }

    public NoUniqueCacheAnnException(String message) {
        super(CacheErrorCodes.CACHE_EXCEPTION.getCode(), message, null);
    }

    public NoUniqueCacheAnnException(String message, Throwable cause) {
        super(CacheErrorCodes.CACHE_EXCEPTION.getCode(), message, cause);
    }

    public NoUniqueCacheAnnException(Throwable cause) {
        super(cause);
    }
}
