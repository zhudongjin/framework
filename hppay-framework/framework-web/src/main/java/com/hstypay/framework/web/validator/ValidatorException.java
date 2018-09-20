package com.hstypay.framework.web.validator;

import com.hstypay.sandbox.error.ErrorCode;
import com.hstypay.sandbox.error.IError;

public class ValidatorException extends RuntimeException implements IError{

	/**
	 * 
	 */
	private static final long serialVersionUID = 592154068188373411L;
	
	// 错误信息
    private ErrorCode errorCode;
    
    public ValidatorException() {
        this.errorCode = ErrorCode.error();
    }

    public ValidatorException(Throwable cause) {
        super(cause);
        this.errorCode = ErrorCode.error();
    }

    public ValidatorException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    public ValidatorException(ErrorCode errorCode, Throwable cause) {
        super(errorCode.getMessage(), cause);
        this.errorCode = errorCode;
    }

    public ValidatorException(String code, String message, Throwable cause) {
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
