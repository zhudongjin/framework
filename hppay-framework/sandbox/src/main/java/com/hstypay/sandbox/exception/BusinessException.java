package com.hstypay.sandbox.exception;

import com.hstypay.sandbox.error.ErrorCode;
import com.hstypay.sandbox.error.IError;

/**
 * 业务异常
 *
 * @author Vincent
 * @version 1.0 2017-06-27 10:03
 */
public class BusinessException extends RuntimeException implements IError {

    private static final long serialVersionUID = 1L;

    // 错误信息
    private ErrorCode errorCode;

    public BusinessException() {
        this.errorCode = ErrorCode.error();
    }

    public BusinessException(Throwable cause) {
        super(cause);
        this.errorCode = ErrorCode.error();
    }

    public BusinessException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    public BusinessException(ErrorCode errorCode, Throwable cause) {
        super(errorCode.getMessage(), cause);
        this.errorCode = errorCode;
    }

    public BusinessException(String code, String message, Throwable cause) {
        super(message, cause);
        this.errorCode = ErrorCode.create(code, message);
    }

    @Override
    public ErrorCode getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(ErrorCode errorCode) {
        this.errorCode = errorCode;
    }
}
