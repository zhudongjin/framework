package com.hstypay.framework.session.exception;

import com.hstypay.sandbox.error.ErrorCode;
import com.hstypay.sandbox.error.ErrorCodeType;

public enum SessionErrorCodes implements ErrorCodeType {

    SESSION_EXCEPTION("session.exception", "系统异常"),
    SESSION_CHK_FAIL("session.check.fail", "获取session失败"),
    SESSION_RESULT_INVALID("session.result.invalid", "检查session失败"),
    NO_SESSION_VALIDATOR("session.no.validator", "获取session校验器失败"),
    NULL_SESSION_KEY("session.key.null", "session key为空"),
    NULL_POINTER("session.null.pointer", "空指针异常"),
    SAVE_SESSION("session.save.fail", "保存session失败");

    private final String code;

    private final String message;

    SessionErrorCodes(final String code, final String message) {
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
