package com.hstypay.sandbox.support.xml;

import org.apache.commons.lang3.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.ByteArrayInputStream;
import java.io.StringReader;
import java.lang.reflect.Method;
import java.util.*;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * XML的工具方法
 *
 * @author 尹有松
 * @version 1.0 2017-07-01 10:48
 */
public class XmlUtils {

	private static final Logger logger = Logger.getLogger(XmlUtils.class.getName());

    /**
     * 转XMLmap
     *
     * @param xmlBytes xml字节
     * @param charset  编码
     * @return map对象
     * @throws Exception 异常
     */
    public static Map<String, String> toMap(byte[] xmlBytes, String charset) throws Exception {
		SAXReader reader = new SAXReader(false);
		reader.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
		reader.setFeature("http://xml.org/sax/features/external-general-entities", false);
		reader.setFeature("http://xml.org/sax/features/external-parameter-entities", false);
		reader.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
		reader.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);

        InputSource source = new InputSource(new ByteArrayInputStream(xmlBytes));
        source.setEncoding(charset);
        Document doc = reader.read(source);
        return XmlUtils.toMap(doc.getRootElement());
    }

    /**
     * 转MAP
     *
     * @param element xml节点
     * @return map对象
     */
    @SuppressWarnings("unchecked")
    public static Map<String, String> toMap(Element element) {
        Map<String, String> rest = new HashMap<String, String>();
        List<Element> els = element.elements();
        for (Element el : els) {
            rest.put(el.getName().toLowerCase(), el.getText());
        }
        return rest;

    }

    /**
     * map转xml
     *
     * @param params map对象
     * @return xml字符串
     */
    public static String toXml(Map<String, String> params) {
        return toXml(params, "xml");
    }

    /**
     * map转xml 带标签
     *
     * @param params map对象
     * @param tag    标签
     * @return xml字符串
     */
    public static String toXml(Map<String, String> params, String tag) {
        StringBuilder buf = new StringBuilder();
        List<String> keys = new ArrayList<String>(params.keySet());
        Collections.sort(keys);
        buf.append("<").append(tag).append(">");
        for (String key : keys) {
        	if(StringUtils.isNotBlank(params.get(key))){
        		 buf.append("<").append(key).append(">");
                 buf.append("<![CDATA[").append(params.get(key)).append("]]>");
                 buf.append("</").append(key).append(">\n");
        	}
        }
        buf.append("</").append(tag).append(">");
        return buf.toString();
    }

    /**
     * xml 转Map
     *
     * @param xml xml字符串
     * @return map对象
     */
    public static Map<String, String> parserXml(String xml) {
        Map<String, String> map = new LinkedHashMap<String, String>();
        if (StringUtils.isNotBlank(xml)) {
            try {
				Document document = null;
				SAXReader reader = new SAXReader();
				reader.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
				reader.setFeature("http://xml.org/sax/features/external-general-entities", false);
				reader.setFeature("http://xml.org/sax/features/external-parameter-entities", false);
				reader.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
				reader.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);

				String encoding = getEncoding(xml);
				InputSource source = new InputSource(new StringReader(xml));
				source.setEncoding(encoding);
				document = reader.read(source);
				if(document.getXMLEncoding() == null) {
					document.setXMLEncoding(encoding);
				}

                Element employees = document.getRootElement();
                for (Iterator i = employees.elementIterator(); i.hasNext(); ) {
                    Element employee = (Element) i.next();
                    map.put(employee.getName(), employee.getText());
                }
            } catch (DocumentException ignored) {
				logger.warning("DocumentException " + ignored.getMessage());
            } catch (SAXException e) {
				e.printStackTrace();
				logger.warning("SAXException " + e.getMessage());
			}
		}
        return map;
    }

	private static String getEncoding(String text) {
		String result = null;
		String xml = text.trim();
		if(xml.startsWith("<?xml")) {
			int end = xml.indexOf("?>");
			String sub = xml.substring(0, end);
			StringTokenizer tokens = new StringTokenizer(sub, " =\"'");

			while(tokens.hasMoreTokens()) {
				String token = tokens.nextToken();
				if("encoding".equals(token)) {
					if(tokens.hasMoreTokens()) {
						result = tokens.nextToken();
					}
					break;
				}
			}
		}

		return result;
	}

    /**
     * java对象转xml
     *
     * @param obj 对象
     * @return xml字符串
     */
    public static String toXml(Object obj) {
        StringBuilder buf = new StringBuilder();
        buf.append("<xml>");
        try {
            Class type = obj.getClass();
            BeanInfo beanInfo = Introspector.getBeanInfo(type);

            PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
            for (PropertyDescriptor descriptor : propertyDescriptors) {
                String propertyName = descriptor.getName();
                if (!propertyName.equals("class")) {
                    Method readMethod = descriptor.getReadMethod();
                    Object result = readMethod.invoke(obj);
                    if (result != null) {
                        buf.append("<").append(propertyName).append(">");
                        buf.append("<![CDATA[").append(result).append("]]>");
                        buf.append("</").append(propertyName).append(">\n");
                    }
                }
            }

        } catch (Exception ignored) {
        }
        buf.append("</xml>");
        return buf.toString();
    }

    /**
     * xml转java对象，驼峰式转换为下划线大写方式命名的字符串
     *
     * @param xml  字符串
     * @param type 类
     * @param <T>  泛型转换
     * @return 对象
     */
    public static <T> T toObject(String xml, Class<T> type) {
        Map<String, String> map = parserXml(xml);
        return toObject(map, type);
    }

    /**
     * map转java对象
     *
     * @param map  map对象
     * @param type 类
     * @param <T>  泛型转换
     * @return java对象
     */
    public static <T> T toObject(Map<String, String> map, Class<T> type) {
        try {
            BeanInfo beanInfo = Introspector.getBeanInfo(type);
            PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
            T obj = type.newInstance();

            for (PropertyDescriptor descriptor : propertyDescriptors) {
                String propertyName = underlineConvertCamel(descriptor.getName());
                if (map.containsKey(propertyName)) {
                    Object value = map.get(propertyName);
                    Object[] args = new Object[1];
                    args[0] = value;
                    descriptor.getWriteMethod().invoke(obj, args);
                }
            }

            return obj;
        } catch (Exception ignored) {
        }

        return null;
    }

    /**
     * camel convert the under line
     *
     * @param param 参数
     * @return 转换后
     */
    public static String underlineConvertCamel(String param) {
		Pattern p = Pattern.compile("[A-Z]");
        if (param == null || param.equals("")) {
            return "";
        }
        StringBuilder builder = new StringBuilder(param);
        Matcher mc = p.matcher(param);
        int i = 0;
        while (mc.find()) {
            builder.replace(mc.start() + i, mc.end() + i, "_"
                    + mc.group().toLowerCase());
            i++;
        }

        if ('_' == builder.charAt(0)) {
            builder.deleteCharAt(0);
        }
        return builder.toString();
    }
}
