/*
 * Copyright 2003-2005 ExcelUtils http://excelutils.sourceforge.net
 * Created on 2005-6-22
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
import com.hstypay.framework.web.view.excell.support.WorkbookUtils;
import org.apache.commons.beanutils.DynaBean;
import org.apache.commons.beanutils.DynaProperty;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.util.Region;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.StringTokenizer;

/**
 * <p>
 * <b>EachTag </b> is a class which parse the #each tag
 * </p>
 *
 *
 * @version $Revision: 1.13 $ $Date: 2005/12/03 05:50:49 $
 */
public class EachTag implements ITag {

    public static final String KEY_EACH = "#each";

    public int[] parseTag(Object context, HSSFSheet sheet, HSSFRow curRow, HSSFCell curCell) {
        String expr = "";
        String each = curCell.getStringCellValue();
        int eachBegin=each.indexOf("#each");
        StringTokenizer st = new StringTokenizer(each.substring(eachBegin), " ");
        String widthstr = "";
        String onstr = "";
        int pos = 0;
        while (st.hasMoreTokens()) {
            String str = st.nextToken();
            if (pos == 1) {
                expr = str;
            }
            if (pos == 2 && !"on".equals(str)) {
                widthstr = str;
            }
            if (pos == 3 && !"on".equals(str)) {
                onstr = str;
            }
            if (pos == 4) {
                onstr = str;
            }
            pos++;
        }

        int[] widths = new int[0];
        if (null != widthstr && !"".equals(widthstr)) {
            Object o = ExcelParser.parseStr(context, widthstr);
            if (null != o) {
                String[] s = o.toString().split(",");
                widths = new int[s.length];
                for (int i = 0; i < widths.length; i++) {
                    widths[i] = Integer.parseInt(s[i]);
                }
            }
        }

        Object obj = ExcelParser.parseExpr(context, expr);
        if (null == obj)
            return new int[] { 0, 0, 0 };
/*
    // by onstr get the property
    if (!"".equals(onstr)) {
    	obj = ExcelParser.parseExpr(context, onstr);
      if (null == obj)
        return new int[] { 0, 0, 0 };
    }
*/
        // iterator properties
        Iterator it = ExcelParser.getIterator(obj);
        if (null == it) {
            if (obj instanceof DynaBean) {
                it = ExcelParser.getIterator(ExcelParser.getBeanProperties(((DynaBean) obj).getDynaClass()));
            } else if (onstr.length()>0){
                it = ExcelParser.getIterator(obj);
            }else {
                it = ExcelParser.getIterator(ExcelParser.getBeanProperties(obj.getClass()));
            }
        }
        if (null == it) {
            return new int[] { 0, 0, 0 };
        }

        int index = 0;
        int arrayIndex = 0;
        int eachPos = curCell.getCellNum();
        String modelName = expr.substring(ExcelParser.VALUED_DELIM.length(), expr.length()
                - ExcelParser.VALUED_DELIM2.length());

        // restore the obj
        obj = ExcelParser.parseExpr(context, expr);

        String newExpr="";

        while (it.hasNext()) {
            Object o = it.next();
            String property = "";
            if (o instanceof Field) {
                property = ((Field) o).getName();
            } else if (o instanceof Map.Entry) {
                property = ((Map.Entry) o).getKey().toString();
            } else if (o instanceof DynaProperty) {
                property = ((DynaProperty) o).getName();
            } else {
                property = o.toString();
            }

            // test the object is array/list or other
            if (obj.getClass().isArray() || obj instanceof Collection) {
                property = modelName + "[" + arrayIndex++ + "]";
            } else {
                property = modelName + "." +  property;
            }

            Object value = ExcelParser.getValue(context, property);
            if (null == value)
                value = "";
            else if(onstr.length()>0)
            {
                property=property+"."+onstr;
                value=ExcelParser.getValue(context, property);
                if (null == value)
                    value = "";
            }

            if (ExcelUtils.isCanShowType(value)) {

                // get cell merge count
                int width = 1;
                if (index < widths.length) {
                    width = widths[index];
                } else if (1 == widths.length) {
                    width = widths[0];
                }

                if(width>0)
                {
                    // get row merged of the curCell
                    int rowMerged = 1;
                    for (int i = 0; i < sheet.getNumMergedRegions(); i++) {
                        Region r = sheet.getMergedRegionAt(i);
                        if (r.getRowFrom() == curRow.getRowNum() && r.getColumnFrom() == curCell.getCellNum()
                                && r.getColumnTo() == curCell.getCellNum()) {
                            rowMerged = r.getRowTo() - r.getRowFrom() + 1;
                            break;
                        }
                    }

                    HSSFCell cell = WorkbookUtils.getCell(curRow, eachPos);

                    // shift the after cell
                    if (index > 0) {
                        WorkbookUtils.shiftCell(sheet, curRow, cell, 1, rowMerged);
                    }
                    if (width > 1) {
                        HSSFCell nextCell = WorkbookUtils.getCell(curRow, eachPos + 1);
                        WorkbookUtils.shiftCell(sheet, curRow, nextCell, width - 1, rowMerged);
                    }

                    // copy the style of curCell
                    for (int rownum = curRow.getRowNum(); rownum < curRow.getRowNum() + rowMerged; rownum++) {
                        for (int i = 0; i < width; i++) {
                            HSSFRow r = WorkbookUtils.getRow(rownum, sheet);
                            HSSFCell c = WorkbookUtils.getCell(r, eachPos + i);
                            HSSFCell cc = WorkbookUtils.getCell(r, curCell.getCellNum());
                            c.setCellStyle(cc.getCellStyle());
                        }
                    }
                    //set column width
                    short currentCellNum=curCell.getCellNum();
                    for (int i = 0; i < width; i++) {
                        sheet.setColumnWidth((short)(eachPos+i), sheet.getColumnWidth(currentCellNum));

                    }
                    // merge cells
                    if (width > 1 || rowMerged > 1) {
                        sheet.addMergedRegion(new Region(curRow.getRowNum(), cell.getCellNum(), curRow.getRowNum() + rowMerged - 1,
                                (short) (cell.getCellNum() + width - 1)));
                    }
                    cell.setCellValue("${" + property + "}");
                    ExcelParser.parseCell(context, sheet, curRow, cell);
                    eachPos += width;
                }else
                {
                    if(newExpr.length()>0)
                        newExpr=newExpr+", ";
                    newExpr=newExpr+"${" + property + "}";
                }
                index++;
            }
        }
        if(1 == widths.length&&widths[0]==0)
        {
            curCell.setCellValue(each.substring(0,eachBegin)+newExpr);
            ExcelParser.parseCell(context, sheet, curRow, curCell);
        }
        return new int[] { 0, 0, 0 };
    }

    public String getTagName() {
        return KEY_EACH;
    }

    public boolean hasEndTag() {
        return false;
    }
}
