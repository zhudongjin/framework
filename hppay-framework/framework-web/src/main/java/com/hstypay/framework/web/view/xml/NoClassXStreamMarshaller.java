package com.hstypay.framework.web.view.xml;

import com.thoughtworks.xstream.XStream;
import org.springframework.oxm.xstream.XStreamMarshaller;

/**
 * xml节点不显示所属class
 *
 * @author vincent
 * @version 1.0 2016年9月21日 下午8:58:14
 */
public class NoClassXStreamMarshaller extends XStreamMarshaller {

    @Override
    protected void customizeXStream(XStream xstream) {
        xstream.aliasSystemAttribute(null, "class");
    }
}
