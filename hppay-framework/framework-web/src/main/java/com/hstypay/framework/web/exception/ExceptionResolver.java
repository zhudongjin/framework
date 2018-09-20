package com.hstypay.framework.web.exception;

import com.hstypay.framework.core.configuration.AppConfiguration;
import com.hstypay.framework.web.constant.WebConstant;
import com.hstypay.framework.web.dto.Result;
import com.hstypay.framework.web.support.WebUtils;
import com.hstypay.sandbox.constant.Constant;
import com.hstypay.sandbox.error.ErrorCode;
import com.hstypay.sandbox.error.IError;
import com.hstypay.sandbox.exception.BusinessException;
import com.hstypay.sandbox.exception.CommonErrorCodes;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author Vincent
 * @version 1.0 2017-06-27 20:43
 */
public class ExceptionResolver implements HandlerExceptionResolver {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExceptionResolver.class);

    private static final String EXCEPTION_VIEW = "exceptions/exception";

    private String exceptionView = EXCEPTION_VIEW;
    private boolean trueError = false;
    private boolean loginRedirect = false;

    private String getFileMB(long byteFile) {
        if (byteFile == 0)
            return "0MB";
        long mb = 1024 * 1024;
        return "" + byteFile / mb + "MB";
    }

    private IError adapterException(Exception ex) {
        IError exception;
        if (ex instanceof IError) {
            exception = (IError) ex;
        } else if (ex instanceof MaxUploadSizeExceededException) {
            MaxUploadSizeExceededException orgExp = (MaxUploadSizeExceededException) ex;
            long size = orgExp.getMaxUploadSize();
            String cnt = getFileMB(size);
            exception = new BusinessException(CommonErrorCodes.FILE_UPLOAD_LIMIT_SIZE.toErrorCode(cnt), ex);
        } else {
            if (StringUtils.isNotBlank(ex.getMessage())) {
                exception = new BusinessException(ErrorCode.create(CommonErrorCodes.SERVER_BUSY.getCode(), ex.getMessage()), ex);
            } else {
                exception = new BusinessException(ex);
            }
        }
        return exception;
    }

    private String getViewName(HttpServletRequest request) {
        Object _exceptionView = request.getAttribute(Constant.EXCEPTIONS_PAGE_VIEW);
        if (_exceptionView == null || StringUtils.isBlank(_exceptionView.toString())) {
            _exceptionView = StringUtils.isBlank(this.exceptionView) ? exceptionView : EXCEPTION_VIEW;
        }
        return _exceptionView.toString();
    }

    private boolean returnHttpErrorStatus(HttpServletRequest request) {
        boolean _trueError = trueError;
        Object statusFlag = request.getAttribute(Constant.EXCEPTIONS_STATUS);
        if (statusFlag != null && StringUtils.isNotBlank(statusFlag.toString()) && ("true".equalsIgnoreCase(statusFlag.toString()) || "false".equalsIgnoreCase(statusFlag.toString()))) {
            _trueError = "true".equalsIgnoreCase(statusFlag.toString());
        }
        return _trueError;
    }

    @Override
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        IError exception = this.adapterException(ex);
        ErrorCode errorCode = exception.getErrorCode();
        if (StringUtils.isBlank(errorCode.getMessage()) || CommonErrorCodes.NOT_LOGIN.getCode().equalsIgnoreCase(errorCode.getCode())) {
            LOGGER.warn(WebUtils.getRequestLog(request, errorCode.toString()));
        } else {
            LOGGER.error(WebUtils.getRequestLog(request, errorCode.toString()), ex);
        }

        if (returnHttpErrorStatus(request)) response.setStatus(HttpStatus.BAD_REQUEST.value());

        if (this.loginRedirect && CommonErrorCodes.NOT_LOGIN.getCode().equalsIgnoreCase(errorCode.getCode())) {
            String loginUrl = AppConfiguration.getInstance().get(WebConstant.CONF_KEY_LOGIN_URL, "/login");
            try {
                response.sendRedirect(loginUrl);
                return new ModelAndView();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        ModelAndView mv = new ModelAndView();
        mv.setViewName(this.getViewName(request));
        mv.addObject(Result.failure(errorCode));
        return mv;
    }

    public void setExceptionView(String exceptionView) {
        this.exceptionView = exceptionView;
    }

    public void setTrueError(boolean trueError) {
        this.trueError = trueError;
    }

    public void setLoginRedirect(boolean loginRedirect) {
        this.loginRedirect = loginRedirect;
    }
}
