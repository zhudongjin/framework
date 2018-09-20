package com.hstypay.sandbox.exception;

import com.hstypay.sandbox.error.ErrorCode;

/**
 * 无效数据异常，接收到此异常后，直接返回null对象
 * Created by Lenovo on 2017/7/19.
 */
public class InvalidDataException extends BusinessException {
    // 错误信息
    private ErrorCode errorCode;

    public InvalidDataException() {
        this.errorCode = ErrorCode.error();
    }

    public InvalidDataException(Throwable cause) {
        super(cause);
        this.errorCode = ErrorCode.error();
    }

    public InvalidDataException(ErrorCode errorCode) {
        super(errorCode);
        this.errorCode = errorCode;
    }

    public InvalidDataException(ErrorCode errorCode, Throwable cause) {
        super(errorCode, cause);
        this.errorCode = errorCode;
    }

    public InvalidDataException(String code, String message, Throwable cause) {
        super(code, message, cause);
        this.errorCode = ErrorCode.create(code, message);
    }
}
