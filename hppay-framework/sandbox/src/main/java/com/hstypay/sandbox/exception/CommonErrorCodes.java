package com.hstypay.sandbox.exception;

import com.hstypay.sandbox.error.ErrorCode;
import com.hstypay.sandbox.error.ErrorCodeType;

/**
 * 公共异常信息
 *
 * @author Vincent
 * @version 1.0 2017-06-27 09:45
 */
public enum CommonErrorCodes implements ErrorCodeType {

    SUCCESS("success", null),

    INTERNAL_ERROR("internal error","内部错误"),
    SERVER_BUSY("common.server.busy", "服务器繁忙,请稍后再试"),
    PARAMS_ERROR("common.params.error", "参数错误"),
    OPERATION_ERROR("common.operation.error", "操作失败，请稍后重试"),

    FILE_UPLOAD_ERROR("common.file.upload.error", "文件上传失败"),
    FILE_UPLOAD_LIMIT_SIZE("common.file.upload.limit.size", "上传文件大小不能超过{0}"),
    FILE_UPLOAD_LIMIT("common.file.upload.limit", "上传文件大小超过限制"),
    FILE_NOT_EXIST("common.file.not.exist", "文件不存在"),
    NOT_LOGIN("common.not.login", "请先去登录"),
    LOGIN_FAILED("common.login.failed", "登录失败，请联系管理员"),
    SIGN_ERROR("common.sign.error", "签名失败"),
    VIEW_ERROR("common.view.error", "视图渲染失败"),
    VALID_PARAMS_NOT_NULL("common.valid.params.not.null", "{0}不能为空"),
    VERIFY_CODE_ERROR("common.verify.code.error", "短信验证码不正确"),
    VERIFY_CODE_SENDED("common.verify.code.sended", "短信验证码已经发送，请稍后再试"),
    CAPTCHA_CODE_ERROR("common.captcha.code.error", "验证码不正确"),

    OBJECT_NOT_FOUND("common.obj.not.found", "没有获取到数据"),
    OBJECT_COPY_ERROR("common.obj.copy.error", "数据获取失败"),
    OBJECT_CREATE_ERROR("common.obj.create.error", "创建对象错误"),

    HTTP_CLIENT_PROTOCOL_ERROR("common.http.client.protocol.error", "HTTP协议错误"),
    HTTP_API_SIGN_ERROR("common.http.api.sign.error", "签名验证失败"),

    IO_EXCEPTION("common.io.error", "IO异常"),
    AUTH_EXCEPTION("common.auth.error", "您无权访问该资源，请联系管理员"),
    CLIENT_EXCEPTION("common.client.error", "客户端不匹配"),
    INVALID_URL("common.invalid.url", "无效的访问链接"),
    INVALID_REDIRECT_URL("common.invalid.redirect.url", "重定向失败"),
    THIRD_SERVER_EXCEPTION("e.common.third.server.error", "第三方服务器异常"),
    THIRD_SERVER_FAIL("common.third.server.fail", "第三方处理失败"),
    
    TASK_STATUS("common.task.status","系统调度任务状态未启用"),
    TASK_RUN_STATUS("common.task.runStatus","系统调入任务运行状态正在运行"),
    EXPORT_DATA_TOO_BIG("export.data.too.big", "导出数据太大,请调整查询条件减少导出数据"),
    START_TIME_NOT_NULL("start.time.not.null", "开始时间不能为空"),
    END_TIME_NOT_NULL("end.time.not.null", "结束时间不能为空"),
    MAX_EXPORT_INTERVAL_LIMIT("max.export.interval.limit", "导出时间间隔不能超过{0}"),
    ;

    private final String code;

    private final String message;

    CommonErrorCodes(final String code, final String message) {
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
        return new ErrorCode(this.getCode(), this.getMessage(), args);
    }
}
