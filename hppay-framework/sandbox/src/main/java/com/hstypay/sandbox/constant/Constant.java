package com.hstypay.sandbox.constant;

/**
 * 公共全局常量
 *
 * @author Vincent
 * @version 1.0 2017-06-27 10:34
 */
public interface Constant {

    /** xml格式数据返回的最外层节点名称 */
    String CONSTANT_XML_ROOT_ELE = "result";

    /** 异常页面存储key */
    String EXCEPTIONS_PAGE_VIEW = "EXCEPTIONS_PAGE_VIEW";
    /** 是否在抛异常的时候返回http错误状态 */
    String EXCEPTIONS_STATUS = "EXCEPTION_STATUS";

    /** / **/
    String MARK_SLASH = "/";

    /** = **/
    String MARK_EQUAL = "=";

    /** - **/
    String MARK_HYPHEN = "-";

    /** _ **/
    String MARK_DASH = "_";

    /** & **/
    String MARK_AND = "&";

    /** , **/
    String MARK_COMMA = ",";

    /** . **/
    String MARK_POINT = ".";

    /** ? **/
    String MARK_QUESTION = "?";

    /** : **/
    String MARK_COLON = ":";

    /** | **/
    String MARK_VERTICAL = "|";

    /** ; **/
    String MARK_SEMICOLON = ";";

    /** EMPTY **/
    String EMPTY = "";

    /** UTF-8 **/
    String UTF_8 = "UTF-8";

    /** 文件夹分隔符 **/
    String FOLDER_SEPARATOR = "/";
}
