package com.hstypay.framework.web.controller;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import com.hstypay.sandbox.exception.BusinessException;
import com.hstypay.sandbox.exception.CommonErrorCodes;

/**
 * @author Vincent
 * @version 1.0 2017-06-27 23:27
 */
public abstract class BaseController {

    protected Logger logger = LoggerFactory.getLogger(this.getClass());

    @Resource
    protected HttpServletRequest request;

    @Resource
    protected HttpServletResponse response;

    protected void redirectToUrl(String url) {
        try {
            response.sendRedirect(url);
        } catch (IOException e) {
            logger.error("redirect error to " + url, e);
            throw new BusinessException(CommonErrorCodes.INVALID_REDIRECT_URL.toErrorCode(), e);
        }
    }
    
    /**
	 * 校验上传的文件
	 * @param accessory
	 * @return true 校验通过  false 校验失败
	 */
	protected boolean validUploadedImg(MultipartFile accessory, int maxSize) {
		List<String> validContentTypes = Arrays.asList(new String[]{"png", "jpg", "jpeg", "gif", "bmp"});
		
		String contentType = accessory.getContentType();
		String validType = "";
		long size = accessory.getSize();
		
		for (String type : validContentTypes) {
			if (contentType.indexOf(type) >= 0) {
				validType = type;
				break;
			}
		}
		if (validType.equals("") || size > maxSize) {
			return false;
		}
		return true;
	}


}
