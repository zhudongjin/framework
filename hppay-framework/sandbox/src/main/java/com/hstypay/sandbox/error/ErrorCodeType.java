package com.hstypay.sandbox.error;

/**
 * 异常接口
 *
 * @author Vincent
 * @version 1.0 2017-06-27 09:43
 */
public interface ErrorCodeType {

    String getCode();

    String getMessage();

    ErrorCode toErrorCode(Object... args);
}
