package guda.grape.mvc.uribox.define;

import org.springframework.beans.factory.xml.NamespaceHandlerSupport;

/**
 * Created by well on 15/3/12.
 */
public class UriboxNamespaceHandlerSupport extends NamespaceHandlerSupport {
    @Override
    public void init() {
        registerBeanDefinitionParser("uris",
                new UriboxBeanDefinitionParser());
    }
}
