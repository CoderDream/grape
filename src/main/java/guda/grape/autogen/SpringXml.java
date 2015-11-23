package guda.grape.autogen;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.Iterator;

/**
 * Created by foodoon on 2014/6/26.
 */
public class SpringXml {

    public static Document createDocument() {
        Document document = DocumentHelper.createDocument();
        return document;
    }

    public static Element createSpringXmlHeader(Document document) {
        Element beans = document.addElement("beans");
        beans.addAttribute("xmlns", "http://www.springframework.org/schema/beans");
        beans.addAttribute("xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance");
        beans.addAttribute("xmlns:p", "http://www.springframework.org/schema/p");
        beans.addAttribute("xmlns:context", "http://www.springframework.org/schema/context");
        beans.addAttribute("xsi:schemaLocation", "http://www.springframework.org/schema/context\n" +
                "       http://www.springframework.org/schema/context/spring-context-3.2.xsd\n" +
                "       http://www.springframework.org/schema/beans\n" +
                "       http://www.springframework.org/schema/beans/spring-beans-3.2.xsd");
        return beans;
    }

    public static Element getSpringRoot(Document document) {
       return document.getRootElement();
    }

    public static Element createBean(Element element, String beanId, String beanClass) {
        if (element == null) {
            return element;
        }
        Element bean = element.addElement("bean");
        bean.addAttribute("id", beanId);
        bean.addAttribute("class", beanClass);
        return bean;
    }

    public static Element appendVal(Element element, String name, String val) {
        if (element == null) {
            return element;
        }
        Element property = element.addElement("property");
        property.addAttribute("name", name);
        property.addAttribute("value", val);
        return property;
    }

    public static Element appendRef(Element element, String name, String ref) {
        if (element == null) {
            return element;
        }
        Element property = element.addElement("property");
        property.addAttribute("name", name);
        property.addAttribute("ref", ref);
        return property;
    }


    public static void write(String fileName, Document document) {
        try {
            File file = new File(fileName);
            file.getParentFile().mkdirs();
            Writer fileWriter = new FileWriter(fileName);
            OutputFormat format = OutputFormat.createPrettyPrint();
            format.setEncoding("UTF-8");
            format.setSuppressDeclaration(false);
            format.setIndent(true);
            format.setIndent(" ");
            format.setNewlines(true);
            XMLWriter xmlWriter = new XMLWriter(fileWriter,format);
            xmlWriter.write(document);   //写入文件�?
            xmlWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static boolean hasBean(String fullFileName, String beanId) {

        try {
            Document document = loadXML(fullFileName);
            Element beans = document.getRootElement();
            for (Iterator i = beans.elementIterator(); i.hasNext(); ) {
                Element bean = (Element) i.next();
                String val = bean.attributeValue(beanId);
                if (val != null) {
                    return true;
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean hasBean(Document document, String beanId) {
        try {
            Element beans = document.getRootElement();
            for (Iterator i = beans.elementIterator(); i.hasNext(); ) {
                Element bean = (Element) i.next();
                Attribute att = bean.attribute("id");
                if (att != null && beanId.equals(att.getValue())) {
                    return true;
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static Document loadXML(String fullFileName) {
        File inputXml = new File(fullFileName);
        SAXReader saxReader = new SAXReader();
        saxReader.setValidation(false);
        try {
            Document document = saxReader.read(inputXml);
            return document;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Element getSpringXmlRoot(String fullFileName) {
        try {
            Document document = loadXML(fullFileName);
            Element beans = document.getRootElement();
            return beans;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
