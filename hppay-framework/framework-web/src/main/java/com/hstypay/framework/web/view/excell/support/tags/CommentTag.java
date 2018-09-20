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
import org.apache.commons.beanutils.DynaBean;
import org.apache.poi.hssf.usermodel.*;

/**
 * <p>
 * <b>ForeachTag </b> is a class which parse the #foreach tag
 * </p>
 *
 *
 * @version $Revision: 1.6 $ $Date: 2005/07/12 08:25:57 $
 */
public class CommentTag implements ITag {

    public static final String KEY_COMMENT = "#comment";

    public int[] parseTag(Object context, HSSFSheet sheet, HSSFRow curRow,
                          HSSFCell curCell) {
        DynaBean ctx = ExcelUtils.getContext();

        String cellstr = curCell.getStringCellValue();
        int commentBegin = cellstr.indexOf("#comment");
        String commentContent = cellstr.substring(
                commentBegin + "#comment".length()).trim();
        if (commentBegin > 0)
            cellstr = cellstr.substring(0, commentBegin);
        else
            cellstr = "";

        if (commentContent.length() > 0) {
            HSSFPatriarch patr = (HSSFPatriarch) ctx.get(KEY_COMMENT);
            if (patr == null) {
                patr = sheet.createDrawingPatriarch();
                ctx.set(KEY_COMMENT, patr);
            }
            HSSFComment comment = patr.createComment(new HSSFClientAnchor(0, 0,
                    0, 0, (short) 4, 2, (short) 6, 5));
            ITag tag = ExcelParser.getTagClass(commentContent);
            if (null != tag) {
                curCell.setCellValue(commentContent);
                int[] shift = tag.parseTag(context, sheet, curRow, curCell);
                commentContent = curCell.getStringCellValue();
            } else {
                commentContent = (String) ExcelParser.parseStr(context,
                        commentContent);
            }

            comment.setString(new HSSFRichTextString(commentContent));
            curCell.setCellComment(comment);
        }
        curCell.setCellValue(cellstr);
        ExcelParser.parseCell(context, sheet, curRow, curCell);
        return new int[] { 0, 0, 0 };
    }

    public String getTagName() {
        return KEY_COMMENT;
    }

    public boolean hasEndTag() {
        return false;
    }
}
