package com.hstypay.sandbox.support;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.StringUtils;

/**
 * ClassName:MapUtil
 * Function: map帮助类
 * Date:     2017年7月4日 下午2:40:09
 * @author   hansong
 */
public class MapUtil {

	/**
     * convertMapType:转换map类型
     *
     * @author hansong
     * @param map
     * @return
     */
    public static Map<String,String> convertMapType(Map<String,Object> map){
    	Map<String,String> resultMap = new HashMap<String,String>();
    	if(map == null) {
    		return resultMap;
    	}
    	
    	for (Entry<String, Object> entry : map.entrySet()) {
    		if(entry.getValue() != null && StringUtils.isNotBlank(entry.getValue().toString())){
    			resultMap.put(entry.getKey(), entry.getValue().toString());
    		}
		}
    	
    	return resultMap;
    }
}

