package guda.grape.mvc.uribox;


import guda.grape.mvc.spring.CustomPropertyConfigurer;
import guda.grape.mvc.spring.PropertiesReplace;
import guda.grape.mvc.spring.SpringBeanFactoryTool;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class URIBoxManager {

    private String uriConfigPath = "spring/spring-uris.xml";

    public Map<String, URIBox> loadURIBox() throws ParserConfigurationException, SAXException,
            IOException {
        ClassLoader currentClassLoader = Thread.currentThread().getContextClassLoader();
        InputStream inputStream = currentClassLoader.getResourceAsStream(uriConfigPath);
        DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
        Document doc = docBuilder.parse(inputStream);
        NodeList nodeList = doc.getChildNodes().item(0).getChildNodes();

        Map<String, URIBox> map = new HashMap<String, URIBox>();
        filterNode(nodeList, map);
        return map;

    }

    public void filterNode(NodeList nodeList, Map<String, URIBox> map) {

        if (nodeList == null) {
            return;
        }
        for (int j = 0, lenJ = nodeList.getLength(); j < lenJ; ++j) {
            Node node = nodeList.item(j);
            if (node.hasAttributes()) {
                NamedNodeMap namedNodeMap = node.getAttributes();
                String name = namedNodeMap.getNamedItem("name").getNodeValue();
                String val = resolveVal(namedNodeMap.getNamedItem("value").getNodeValue());
                map.put(name, new DefaultURIBox(val));
            }
            filterNode(node.getChildNodes(), map);

        }

    }

    public String resolveVal(String strKey) {
        CustomPropertyConfigurer p = SpringBeanFactoryTool.getBeanFactory().getBean(CustomPropertyConfigurer.class);
        if (strKey.startsWith(PropertiesReplace.DEFAULT_PLACEHOLDER_PREFIX)) {
            String key = PropertiesReplace.resolveStringKey(strKey);
            return String.valueOf(p.getAppProperties().get(key));
        }
        return strKey;
    }


    public void setUriConfigPath(String uriConfigPath) {
        this.uriConfigPath = uriConfigPath;
    }

}
