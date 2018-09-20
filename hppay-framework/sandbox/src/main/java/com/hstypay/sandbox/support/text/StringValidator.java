package com.hstypay.sandbox.support.text;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;


/**
 * 系统统一的非法字符校验器
 * @author wanfan
 *
 */
public class StringValidator {

	/**
	 * 非法的字符列表
	 */
	private static String[] invalidStrs = new String[]{"\\|", ",", "'", "`"};
	
	private static String pattern = "";
	static {
		pattern += "(";
		for (String invalidStr : invalidStrs){
			pattern += invalidStr;
			pattern += "|";
		}
		pattern = pattern.substring(0, pattern.length() -1);
		pattern += ")";
	}
	
	private static Pattern regx = Pattern.compile(pattern);
	
	
	/**
	 * 获取错误提示信息
	 * @return
	 */
	public static String getErrorInfo(){
		StringBuilder sb = new StringBuilder();
		sb.append("可能含有非法字符：");
		for (String invalidStr : invalidStrs) {
			sb.append(invalidStr);
		}
		// 去掉转义符
		return sb.toString().replace("\\", "");
	}
	
	
	
	
	/**
	 * 是否包含非法的字符或者字符串
	 * @param content
	 * @return
	 */
	public static boolean isValid(String content) {
		Matcher m = regx.matcher(content);
		if (m.find( )) {
			return false;
		}
		return true;
	}
	
	/**
	 * 判断某个字符串 是否是 数字或字母
	 * @param content
	 * @return
	 */
	public static boolean isNumOrLetter(String content){
		if(StringUtils.isBlank(content)){
			return false;
		}
		return content.matches("[0-9A-Za-z]*");
	}
}
