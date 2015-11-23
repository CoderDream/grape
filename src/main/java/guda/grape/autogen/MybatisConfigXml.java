package guda.grape.autogen;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import java.util.Iterator;

/**
 * Created by foodoon on 2014/6/27.
 */
public class MybatisConfigXml {

    public static Document createDocument() {
        Document document = DocumentHelper.createDocument();
        return document;
    }

    public static Element createRoot(Document document) {
        // <!DOCTYPE sqlMapConfig PUBLIC "-//iBATIS.com//DTD SQL Map Config 2.0//EN"
        // "http://www.ibatis.com/dtd/sql-map-config-2.dtd">
        // document.addDocType("sqlMapConfig","-//iBATIS.com//DTD SQL Map Config 2.0//EN","http://www.ibatis.com/dtd/sql-map-config-2.dtd");
        document.addDocType("sqlMapConfig", "-//ibatis.apache.org//DTD SQL Map Config 2.0//EN",
                "http://ibatis.apache.org/dtd/sql-map-config-2.dtd");

        Element configuration = document.addElement("sqlMapConfig");

        return configuration;
    }

    public static Element getRoot(Document document) {
        Element mappers = document.getRootElement();
        return mappers;
    }

    public static Element getMapper(Document document) {
        Element e = document.getRootElement();
        return e;
    }

    public static Element appendVal(Element element, String val) {
        if (element == null) {
            return element;
        }
        Element property = element.addElement("sqlMap");
        property.addAttribute("resource", val);
        return property;
    }

    public static boolean hasConfig(Document document, String resource) {
        Element e = getMapper(document);
        Iterator iterator = e.elementIterator();
        while (iterator.hasNext()) {
            Element next = (Element) iterator.next();
            Attribute resource1 = next.attribute("resource");
            if (resource.equals(resource1.getValue())) {
                return true;
            }
        }
        return false;
    }
}
