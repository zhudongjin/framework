package com.hstypay.framework.web.view.xml;

import com.thoughtworks.xstream.core.util.QuickWriter;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.io.naming.NameCoder;
import com.thoughtworks.xstream.io.xml.PrettyPrintWriter;
import com.thoughtworks.xstream.io.xml.XmlFriendlyNameCoder;
import com.thoughtworks.xstream.io.xml.XppDriver;

import java.io.Writer;

/**
 * 添加<![CDATA[]]>
 *
 * @author vincent
 * @version 1.0 2016年9月21日 下午8:57:18
 */
public class CDATAXppDriver extends XppDriver {

    protected static String PREFIX_CDATA = "<![CDATA[";
    protected static String SUFFIX_CDATA = "]]>";

    public CDATAXppDriver() {
        super(new XmlFriendlyNameCoder());
    }

    /**
     * @since 1.4
     */
    public CDATAXppDriver(NameCoder nameCoder) {
        super(nameCoder);
    }

    @Override
    public HierarchicalStreamWriter createWriter(Writer out) {
        return new PrettyPrintWriter(out, getNameCoder()) {

            @Override
            protected void writeText(QuickWriter writer, String text) {
                writer.write(PREFIX_CDATA);
                writer.write(text);
                writer.write(SUFFIX_CDATA);
            }
        };
    }
}
