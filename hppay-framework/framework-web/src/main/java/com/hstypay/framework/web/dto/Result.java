package com.hstypay.framework.web.dto;

import com.hstypay.sandbox.constant.Constant;
import com.hstypay.sandbox.error.ErrorCode;
import com.hstypay.sandbox.exception.CommonErrorCodes;
import com.hstypay.sandbox.support.LogHelper;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import org.slf4j.MDC;

import java.io.Serializable;
/**
 * 返回结果
 *
 * @author vincent
 * @version 1.0 2016年12月15日 下午3:07:55
 */
@XStreamAlias(Constant.CONSTANT_XML_ROOT_ELE)
public class Result implements Serializable {

    private static final long serialVersionUID = 1L;

    private String logId = MDC.get(LogHelper.KEY_REQUEST_ID);

    private boolean status = true;

    private ErrorCode error;

    private Object data;

    public Result() {

    }

    public Result(Object obj) {
        if(obj instanceof ErrorCode){
            this.setError((ErrorCode) obj);
        } else {
            this.data = obj;
        }
    }

    public ErrorCode getError() {
        return error;
    }

    public void setError(ErrorCode error) {
        this.error = error;
        if (error != null) {
            status = CommonErrorCodes.SUCCESS.getCode() == error.getCode();
        }
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getLogId() {
        return logId;
    }

    public void setLogId(String logId) {
        this.logId = logId;
    }

    public static Result ok() {
        return new Result();
    }

    public static Result ok(Object data) {
        return new Result(data);
    }

    public static Result fail(String message) {
        CommonErrorCodes error = CommonErrorCodes.SERVER_BUSY;
        String code = error.getCode();
        return failure(code, message);
    }

    public static Result failure(ErrorCode errorCode) {
        return new Result(errorCode);
    }

    public static Result failure(String code, String message) {
        return failure(ErrorCode.create(code, message));
    }
}
