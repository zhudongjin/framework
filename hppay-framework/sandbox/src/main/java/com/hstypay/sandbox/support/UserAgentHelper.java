package com.hstypay.sandbox.support;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * 浏览器版本识别
 *
 * @author tangbaodong
 * @version 1.0 2017-07-01 10:46
 */
public class UserAgentHelper {

    protected static Logger logger = LoggerFactory.getLogger(UserAgentHelper.class);

    /**
     * 获取客户端浏览器类型、编码下载文件名
     *
     * @param userAgent 浏览器agent
     * @param fileName  文件名
     * @return 文件头字符串
     */
    public static String encodeFileName(String userAgent, String fileName) {
        String rtn = "";
        try {
            String new_filename = URLEncoder.encode(fileName, "UTF8");
            // 如果没有UA，则默认使用IE的方式进行编码，因为毕竟IE还是占多数的
            rtn = "filename=\"" + new_filename + "\"";
            if (userAgent != null) {
                userAgent = userAgent.toLowerCase();
                // IE浏览器，只能采用URLEncoder编码
                if (userAgent.contains("msie")) {
                    rtn = "filename=\"" + new_filename + "\"";
                }
                // Opera浏览器只能采用filename*
                else if (userAgent.contains("opera")) {
                    rtn = "filename*=UTF-8''" + new_filename;
                }
                // Safari浏览器，只能采用ISO编码的中文输出
                else if (userAgent.contains("safari")) {
                    rtn = "filename=\"" + new String(fileName.getBytes("UTF-8"), "ISO8859-1") + "\"";
                }
                // Chrome浏览器，只能采用MimeUtility编码或ISO编码的中文输出
                else if (userAgent.contains("applewebkit")) {
                    rtn = "filename=\"" + new String(fileName.getBytes("UTF-8"), "ISO8859-1") + "\"";
                }
                // FireFox浏览器，可以使用MimeUtility或filename*或ISO编码的中文输出
                else if (userAgent.contains("mozilla")) {
                    rtn = "filename*=UTF-8''" + new_filename;
                }
            }
        } catch (UnsupportedEncodingException e) {
            logger.error("", e);
        }
        return rtn;
    }
}
