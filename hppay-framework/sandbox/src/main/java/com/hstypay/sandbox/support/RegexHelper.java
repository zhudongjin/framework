package com.hstypay.sandbox.support;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Vincent
 * @version 1.0 2017-07-05 12:23
 */
public class RegexHelper {

    /**
     * email
     */
    public static final String REG_EMAIL = "\\w[-\\w.+]*@([A-Za-z0-9][-A-Za-z0-9]+\\.)+[A-Za-z]{2,14}";
    /**
     * 身份证
     */
    public static final String REG_IDCARD = "[1-9]\\d{13,16}[a-zA-Z0-9]{1}";
    /**
     * 手机号码
     */
    public static final String REG_MOBILE = "(\\+\\d+)?1[34578]\\d{9}$";
    /**
     * 简单手机号码
     */
    public static final String REG_MOBILE_SIMPLE = "^1\\d{10}$";
    /**
     * 银行手机号码
     */
    public static final String REG_MOBILE_BANK = "^((13[0-9])|(15[^4,\\D])|(18[0-9])|(14[579])|(17[^249\\D]))\\d{8}$";
    /**
     * 香港手机号码
     */
    public static final String REG_MOBILE_HK = "^(5|6|8|9)\\d{7}$";
    /**
     * 数字字符串
     */
    public static final String REG_NUMBER = "^[0-9]*$";
    /**
     * 固定电话
     */
    public static final String REG_PHONE = "((\\d{1,4}([-]\\d{1,8}){1,2})|(\\d{7,12}))";
    /**
     * 正整数和负整数
     */
    public static final String REG_DIGIT = "\\\\-?[1-9]\\\\d+";
    /**
     * 整数和浮点数（正负整数和正负浮点数）
     */
    public static final String REG_DECIMALS = "^[-+]?[0-9]*\\.?[0-9]+$";
    /**
     * 空白字符
     */
    public static final String REG_BLANK_SPACE = "\\\\s+";
    /**
     * 中文字符
     */
    public static final String REG_CHINESE = "^[\\u4E00-\\u9FA5]+$";
    /**
     * 日期（年月日）
     */
    public static final String REG_BIRTHDAY = "[1-9]{4}([-./])\\\\d{1,2}\\\\1\\\\d{1,2}";
    /**
     * URL地址
     */
    public static final String REG_URL = "^((https|http|ftp|rtsp|mms)?:\\/\\/)[^\\s]+";
    /**
     * 获取网址 URL 的一级域名
     */
    public static final String REG_URL_DOMAIN = "(?<=http://|\\\\.)[^.]*?\\\\.(com|cn|net|org|biz|info|cc|tv)";
    /**
     * 中国邮政编码
     */
    public static final String REG_POST_CODE = "[1-9]\\\\d{5}";
    /**
     * 匹配IP地址
     */
    public static final String REG_IP = "[1-9](\\\\d{1,2})?\\\\.(0|([1-9](\\\\d{1,2})?))\\\\.(0|([1-9](\\\\d{1,2})?))\\\\.(0|([1-9](\\\\d{1,2})?))";
    /**
     * 匹配营业执照注册号
     */
    public static final String LICENSE_CODE = "[A-Z0-9]{15}$|[A-Z0-9]{18}$";
    
    /**
     * 由数字和26个英文字母组成的字符串
     */
    public static final String REG_LETTER_NUMBERS = "^[A-Za-z0-9]+$";
    
    /**
     * 由中文、数字和26个英文字母组成的字符串
     */
    public static final String REG_CHINESE_LETTER_NUMBERS = "^[\\u4E00-\\u9FA5_A-Za-z0-9]+$";
    
    /**
     * 验证正则
     *
     * @param regex 正则
     * @param str   验证的字符串
     * @return true/false
     */
    public static boolean match(String regex, String str) {
        return Pattern.matches(regex, str);
    }

    /**
     * 验证Email
     *
     * @param email email地址，格式：zhangsan@zuidaima.com，zhangsan@xxx.com.cn，xxx代表邮件服务商
     * @return 验证成功返回true，验证失败返回false
     */
    public static boolean checkEmail(String email) {
        return Pattern.matches(REG_EMAIL, email);
    }
    
    public static void main(String[] args) {
		String email = "zhongjian@163.com";
		System.out.println(checkEmail(email));
	}

    /**
     * 验证身份证号码
     *
     * @param idCard 居民身份证号码15位或18位，最后一位可能是数字或字母
     * @return 验证成功返回true，验证失败返回false
     */
    public static boolean checkIdCard(String idCard) {
        return Pattern.matches(REG_IDCARD, idCard);
    }

    /**
     * 验证手机号码（支持国际格式，+86135xxxx...（中国内地），+00852137xxxx...（中国香港））
     *
     * @param mobile 移动、联通、电信运营商的号码段
     *               <p>移动的号段：134(0-8)、135、136、137、138、139、147（预计用于TD上网卡）
     *               、150、151、152、157（TD专用）、158、159、187（未启用）、188（TD专用）</p>
     *               <p>联通的号段：130、131、132、155、156（世界风专用）、185（未启用）、186（3g）</p>
     *               <p>电信的号段：133、153、180（未启用）、189</p>
     * @return 验证成功返回true，验证失败返回false
     */
    public static boolean checkMobile(String mobile) {
        return Pattern.matches(REG_MOBILE, mobile);
    }

    /**
     * 简单手机号验证 1开头的11位数字
     *
     * @param mobile 手机号码
     * @return 验证成功返回true，验证失败返回false
     */
    public static boolean checkSimpleMobile(String mobile) {
        return Pattern.matches(REG_MOBILE_SIMPLE, mobile);
    }
    
    /**
     * 银行手机号验证 13、15[0-3][5-9]、18、14[579]、17[135678]开头
     *
     * @param mobile 手机号码
     * @return 验证成功返回true，验证失败返回false
     */
    public static boolean checkBankMobile(String mobile) {
        return Pattern.matches(REG_MOBILE_BANK, mobile);
    }
    
    /**
     * 香港手机号码8位数，5|6|8|9开头+7位任意数
     *
     * @param mobile 手机号码
     * @return 验证成功返回true，验证失败返回false
     */
    public static boolean checkHKPhone(String mobile) {
        return Pattern.matches(REG_MOBILE_HK, mobile);
    }

    /**
     * 验证固定电话号码
     *
     * @param phone 电话号码，格式：国家（地区）电话代码 + 区号（城市代码） + 电话号码，如：+8602085588447
     *              <p><b>国家（地区） 代码 ：</b>标识电话号码的国家（地区）的标准国家（地区）代码。它包含从 0 到 9 的一位或多位数字，
     *              数字之后是空格分隔的国家（地区）代码。</p>
     *              <p><b>区号（城市代码）：</b>这可能包含一个或多个从 0 到 9 的数字，地区或城市代码放在圆括号——
     *              对不使用地区或城市代码的国家（地区），则省略该组件。</p>
     *              <p><b>电话号码：</b>这包含从 0 到 9 的一个或多个数字 </p>
     * @return 验证成功返回true，验证失败返回false
     */
    public static boolean checkPhone(String phone) {
        return Pattern.matches(REG_PHONE, phone);
    }

    /**
     * 验证数字
     *
     * @param number 一位或多位0-9之间的数字
     * @return 验证成功返回true，验证失败返回false
     */
    public static boolean checkNumber(String number) {
        return Pattern.matches(REG_NUMBER, number);
    }

    /**
     * 验证整数（正整数和负整数）
     *
     * @param digit 一位或多位0-9之间的整数
     * @return 验证成功返回true，验证失败返回false
     */
    public static boolean checkDigit(String digit) {
        return Pattern.matches(REG_DIGIT, digit);
    }

    /**
     * 验证整数和浮点数（正负整数和正负浮点数）
     *
     * @param decimals 一位或多位0-9之间的浮点数，如：1.23，233.30
     * @return 验证成功返回true，验证失败返回false
     */
    public static boolean checkDecimals(String decimals) {
        return Pattern.matches(REG_DECIMALS, decimals);
    }

    /**
     * 验证空白字符
     *
     * @param blankSpace 空白字符，包括：空格、\t、\n、\r、\f、\x0B
     * @return 验证成功返回true，验证失败返回false
     */
    public static boolean checkBlankSpace(String blankSpace) {
        return Pattern.matches(REG_BLANK_SPACE, blankSpace);
    }

    /**
     * 验证中文
     *
     * @param chinese 中文字符
     * @return 验证成功返回true，验证失败返回false
     */
    public static boolean checkChinese(String chinese) {
        return Pattern.matches(REG_CHINESE, chinese);
    }

    /**
     * 验证日期（年月日）
     *
     * @param birthday 日期，格式：1992-09-03，或1992.09.03
     * @return 验证成功返回true，验证失败返回false
     */
    public static boolean checkBirthday(String birthday) {
        return Pattern.matches(REG_BIRTHDAY, birthday);
    }

    /**
     * 验证URL地址
     *
     * @param url 格式：http://blog.csdn.net:80/xyang81/article/details/7705960? 或 http://www.csdn.net:80
     * @return 验证成功返回true，验证失败返回false
     */
    public static boolean checkURL(String url) {
        return Pattern.matches(REG_URL, url);
    }

    /**
     * <pre>
     * 获取网址 URL 的一级域名
     * http://www.zuidaima.com/share/1550463379442688.htm ->> zuidaima.com
     * </pre>
     *
     * @param url 访问地址
     * @return 域名
     */
    public static String getDomain(String url) {
        Pattern p = Pattern.compile(REG_URL_DOMAIN, Pattern.CASE_INSENSITIVE);
        // 获取完整的域名
        // Pattern p=Pattern.compile("[^//]*?\\.(com|cn|net|org|biz|info|cc|tv)", Pattern.CASE_INSENSITIVE);
        Matcher matcher = p.matcher(url);
        matcher.find();
        return matcher.group();
    }

    /**
     * 匹配中国邮政编码
     *
     * @param postcode 邮政编码
     * @return 验证成功返回true，验证失败返回false
     */
    public static boolean checkPostcode(String postcode) {
        return Pattern.matches(REG_POST_CODE, postcode);
    }

    /**
     * 匹配IP地址(简单匹配，格式，如：192.168.1.1，127.0.0.1，没有匹配IP段的大小)
     *
     * @param ipAddress IPv4标准地址
     * @return 验证成功返回true，验证失败返回false
     */
    public static boolean checkIpAddress(String ipAddress) {
        return Pattern.matches(REG_IP, ipAddress);
    }
    
    /**
     * 匹配营业执照注册号
     * @param licenseCode 营业执照注册号
     * @return 验证成功返回true,是失败返回false
     */
    public static boolean checkLicenseCode(String licenseCode) {
    	return Pattern.matches(LICENSE_CODE, licenseCode);
    }
    
    /**
     * 匹配 只能是英文和数字的组合
     * @param str
     * @return 验证成功返回true,是失败返回false
     */
    public static boolean checkLetterNum(String str) {
        return Pattern.matches(REG_LETTER_NUMBERS, str);
    }
    
    /**
     * 匹配 只能是中文、英文和数字的组合
     * @param str
     * @return 验证成功返回true,是失败返回false
     */
    public static boolean checkChineseLetterNum(String str) {
        return Pattern.matches(REG_CHINESE_LETTER_NUMBERS, str);
    }
}
