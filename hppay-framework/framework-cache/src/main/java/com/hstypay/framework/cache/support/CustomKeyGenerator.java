/**
 * 
 */
package com.hstypay.framework.cache.support;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.util.DigestUtils;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.*;

/**
 * @author Exception
 *
 */
public class CustomKeyGenerator implements KeyGenerator {

	@SuppressWarnings("unused")
	private static final Log logger = LogFactory.getLog(CustomKeyGenerator.class);

	@Override
	public Object generate(Object target, Method method, Object... params) {
		StringBuilder keyBuilder = new StringBuilder(16);
		
		//获取类名和函数名，都是带包路径的全名
		String className = target.getClass().getName();
		keyBuilder.append(className);
		keyBuilder.append(",");
		String methodName = method.toGenericString();
		keyBuilder.append(methodName);
		keyBuilder.append(",");
		
		//获取参数值列表
		genArgsString(keyBuilder, params);
		
		//生成md5
		String keySrc = keyBuilder.toString();
		String key = DigestUtils.md5DigestAsHex(keySrc.getBytes());
		
		//将参数计算hash值
		//int hashCode = genHashCode(params);
		int hashCode = keySrc.hashCode();
		
		
		//拼接前缀+md5+hash值作为key
		key = "SPRING_CACHE_KEY_" + key + "." + Integer.toHexString(hashCode);
				
		return key;
	}

	/**
	 * 将参数的值拼接为字串
	 * []表示数组,()表未对象,{}表示map
	 * @param sb
	 * @param args
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	private static StringBuilder genArgsString(StringBuilder sb, Object... args) {
		if (sb == null || args == null)
			return sb;
		
		boolean bFirst = true;
		sb.append("[");
		for (int i = 0; i < args.length; i++) {
			Object obj = args[i];
			
			//第一个参数不追加分隔符
			if (!bFirst)
				sb.append(",");
			else
				bFirst = false;
			
			//如果集合类型,则遍历集合元素
			if ((obj instanceof Collection)) {
				sb.append("[");
				boolean collFirst = true;
				for (Object item : (Collection) obj) {
					if (!collFirst)
						sb.append(",");
					else
						collFirst = false;
					
					sb.append(objectReflectionToString(item));
				}
				sb.append("]");
			} else if (obj instanceof Map) {
				//是map类型，遍历map，用key=value的形式表示map的内容
				Map mpObj = (Map) obj;
				Iterator it = mpObj.entrySet().iterator();
				sb.append("{");
				boolean mapFirst = true;
				while (it.hasNext()) {
					if (!mapFirst)
						sb.append(",");
					else
						mapFirst = false;
					
					Map.Entry entry = (Map.Entry) it.next();
					sb.append(objectReflectionToString(entry.getKey()));
					sb.append("=");
					sb.append(objectReflectionToString(entry.getValue()));
				}
				sb.append("}");
			} else if(obj != null && obj.getClass().isArray()) {
				//如果是数组类型，则遍历数组
				sb.append("[");
				boolean arryFirst = true;
				for (Object item : (Object[])obj) {
					if (!arryFirst)
						sb.append(",");
					else
						arryFirst = false;
					
					sb.append(objectReflectionToString(item));
				}
				sb.append("]");
			} else {
				sb.append(objectReflectionToString(obj));
			}
		}
		
		sb.append("]");
		
		return sb;
	}
	
	/**
	 * 将对象的属性值转为字串列表
	 * @param obj
	 * @return
	 */
	private static String objectReflectionToString(Object obj) {
		if (obj == null)
			return null;
		
		//如果基本数据类型则直接toString转换
		if ((obj instanceof CharSequence) || (obj instanceof Number)
				|| (obj instanceof Character) || (obj instanceof Boolean)
				|| (obj instanceof Date)) {
			return obj.toString();
		} else {
			//对对象使用appache的反映转换，前后加上(),表示对象
			return "("+ToStringBuilder.reflectionToString(obj,
					ToStringStyle.SIMPLE_STYLE)+")";
		}
	}
	
	/**
	 * 生成hash code
	 * @param args
	 * @return
	 */
	private static int genHashCode(Object... args) {
		if (args == null || args.length <= 0)
			return 0;
		
		System.out.println("isarray="+args.length);
		
		Object[] params = new Object[args.length];
		System.arraycopy(args, 0, params, 0, args.length);
		
		int hashCode = Arrays.deepHashCode(params);

		return hashCode;
	}
	
	//测试main
	@SuppressWarnings("unused")
	public static void main( String[] args ) {
		class Test {			
			private String listid;
			private String spid;
			
			public Test(String a1, String a2) {
				listid = a1;
				spid = a2;
			}
		}
		
		Test t1 = new Test("1111","44rr");
		
		Test[] ad = new Test[] {new Test("a1","dds"),new Test("a2","ee")};
		
		Map<String,String> mp = new HashMap<String, String>();
		mp.put("ts12", "d3we");
		mp.put("we12", "rrrd3we");
		
	
		StringBuilder sb = new StringBuilder();
		genArgsString(sb, t1, mp, ad);
		System.out.println(sb.toString());
		System.out.println(Integer.toHexString(genHashCode(sb, t1, mp, ad)));
		
		System.out.println(CustomKeyGenerator.class.getMethods()[1].toGenericString());
		
		class MyInput implements Serializable{
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;
			private String a;
			private String b;
			
			public String getA() {
				return a;
			}

			public void setA(String a) {
				this.a = a;
			}

			public String getB() {
				return b;
			}

			public void setB(String b) {
				this.b = b;
			}

			public MyInput() {
				// TODO Auto-generated constructor stub
			}

		}
		
		MyInput input = new MyInput();
		input.setA("b");
		input.setB("1");
		System.out.println(genHashCode(new Object[]{input}));
		System.out.println(genHashCode(input));
	}
}
