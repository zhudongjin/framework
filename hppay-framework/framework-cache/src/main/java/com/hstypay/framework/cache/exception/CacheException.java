package com.hstypay.framework.cache.exception;

import com.hstypay.sandbox.error.ErrorCode;
import com.hstypay.sandbox.exception.BusinessException;

/**
 * @author Vincent
 * @version 1.0 2017-06-27 19:30
 */
public class CacheException extends BusinessException {

    private static final long serialVersionUID = 1L;

    public CacheException() {
        super();
    }

    public CacheException(Throwable cause) {
        super(cause);
    }

    public CacheException(ErrorCode errorCode) {
        super(errorCode);
    }

    public CacheException(ErrorCode errorCode, Throwable cause) {
        super(errorCode, cause);
    }

    public CacheException(String code, String message, Throwable cause) {
        super(code, message, cause);
    }
}
