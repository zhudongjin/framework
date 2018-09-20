/*
 * Copyright 2003-2005 try2it.com.
 * Created on 2005-7-5
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.hstypay.framework.web.view.excell.support.tags;

import com.hstypay.framework.web.view.excell.support.ExcelParser;
import com.hstypay.framework.web.view.excell.support.ExcelUtils;
import com.hstypay.sandbox.constant.Constant;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;

import java.lang.reflect.Method;
import java.util.StringTokenizer;

/**
 * <p>
 * <b>CallTag</b> is a class which parse the #call tag
 * </p>
 *
 * @version $Revision: 1.1 $ $Date: 2005/07/06 01:43:39 $
 */
public class CallTag implements ITag {

    public static final String KEY_CALL = "#call";

    @SuppressWarnings("deprecation")
    public int[] parseTag(Object context, HSSFSheet sheet, HSSFRow curRow, HSSFCell curCell) {

        String cellstr = curCell.getStringCellValue();
        if (null == cellstr || "".equals(cellstr)) {
            return new int[] { 0, 0, 0 };
        }

        cellstr = cellstr.substring(KEY_CALL.length()).trim();
        String serviceName = cellstr.substring(0, cellstr.indexOf('.'));
        String methodName = cellstr.substring(cellstr.indexOf('.')+1, cellstr.indexOf('('));
        String paramStr = cellstr.substring(cellstr.indexOf('(')+1, cellstr.lastIndexOf(')'));
        String propertyName = cellstr.substring(cellstr.lastIndexOf(')')+1);

        Object[] params = new Object[0];
        Class[] types = new Class[0];
        boolean hasNullPrams = false;
        // prepare params
        if (!"".equals(paramStr) && null != paramStr) {
            StringTokenizer st = new StringTokenizer(paramStr, ",");
            params = new Object[st.countTokens()];
            types = new Class[st.countTokens()];
            int index = 0;
            while (st.hasMoreTokens()) {
                String param = st.nextToken().trim();
                // get param type & value
                types[index] = getParamType(param);
                if ("java.lang.Object".equals(types[index].getName())) {
                    params[index] = ExcelParser.parseStr(context, param);
                    //如果参数为null，则标记参数为null，后面不再调用相应函数
                    //直接用空字串填充，因此参数为null，无法知道参数的具体类型
                    if (params[index] == null) {
                        hasNullPrams = true;
                        break;
                    }

                    types[index] = params[index].getClass();
                } else if ("boolean".equals(types[index].getName())){
                    params[index] = Boolean.valueOf(param);
                } else if ("int".equals(types[index].getName())) {
                    params[index] = Integer.valueOf(param);
                } else if ("double".equals(types[index].getName())) {
                    params[index] = Double.valueOf(param);
                }	else if ("java.lang.String".equals(types[index].getName())){
                    params[index] = param.substring(1, param.length()-1);
                }
                index++;
            }
        }

        //如果参数为null，则标记参数为null，后面不再调用相应函数
        //直接用空字串填充，因此参数为null，无法知道参数的具体类型
        if (hasNullPrams) {
            try {
                Object result = Constant.EMPTY;
                // put the result to context
                ExcelUtils.addValue(context, serviceName + methodName, result);
                curCell.setCellValue("${" + serviceName + methodName + propertyName + "}");
                ExcelParser.parseCell(context, sheet, curRow, curCell);
            } catch (Exception e) {

            }
            return new int[] { 0, 0, 0 };
        }

        // get the service
        Object service = ExcelParser.getValue(context, serviceName);
        if (null == service) {
            return new int[] { 0, 0, 0 };
        }

        // get the method
        Method method = findMethod(service, methodName, types);
        if (null == method) {
            return new int[] { 0, 0, 0 };
        }

        // invoke method
        try {
            Object result = method.invoke(service, params);
            // put the result to context
            ExcelUtils.addValue(context, serviceName+methodName, result);
            curCell.setCellValue("${"+serviceName+methodName+propertyName+"}");
            ExcelParser.parseCell(context, sheet, curRow, curCell);
        } catch (Exception e) {

        }

        return new int[] { 0, 0, 0 };
    }

    public boolean hasEndTag() {
        return false;
    }

    public String getTagName() {
        return KEY_CALL;
    }

    private Method findMethod(Object service, String methodName, Class[] types) {
        Class clazz;
        if (service instanceof Class) {
            clazz = (Class) service;
        } else {
            clazz = service.getClass();
        }

        Method method = null;
        while (clazz != null) {
            try {
                method = clazz.getDeclaredMethod(methodName, types);
            } catch (Exception e) {
                //no such method exception
            }

            if (method != null)
                return method;

            clazz = clazz.getSuperclass();
        }

        return method;
    }

    private Class getParamType(String param) {
        if (param.startsWith("\"") && param.endsWith("\"")) {
            return String.class;
        } else if (param.indexOf(ExcelParser.VALUED_DELIM) >=0) {
            return Object.class;
        } else if (param.equals("true") || param.equals("false")) {
            return boolean.class;
        } else if (param.indexOf('.') >= 0) {
            return double.class;
        } else {
            return int.class;
        }
    }
}
