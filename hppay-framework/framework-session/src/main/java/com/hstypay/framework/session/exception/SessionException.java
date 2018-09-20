/**
 * 
 */
package com.hstypay.framework.session.exception;

import com.hstypay.sandbox.error.ErrorCode;
import com.hstypay.sandbox.exception.BusinessException;

/**
 * 整个session处理过程的异常类
 * 
 * @author Exception
 *
 */
public class SessionException extends BusinessException {

    private static final long serialVersionUID = 1L;

    public SessionException() {
        super();
    }

    public SessionException(Throwable cause) {
        super(cause);
    }

    public SessionException(ErrorCode errorCode) {
        super(errorCode);
    }

    public SessionException(ErrorCode errorCode, Throwable cause) {
        super(errorCode, cause);
    }

    public SessionException(String code, String message, Throwable cause) {
        super(code, message, cause);
    }
}
