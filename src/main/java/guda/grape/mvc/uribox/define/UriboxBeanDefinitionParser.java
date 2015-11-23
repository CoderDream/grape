package guda.grape.mvc.uribox.define;


import guda.grape.mvc.uribox.DefaultURIBox;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.support.BeanDefinitionReaderUtils;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.beans.factory.xml.BeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;


/**
 * Created by well on 15/3/12.
 */
public class UriboxBeanDefinitionParser implements BeanDefinitionParser {

    @Override
    public BeanDefinition parse(Element element, ParserContext context) {
        RootBeanDefinition def = new RootBeanDefinition();
        def.setBeanClass(DefaultURIBox.class);
        String name = element.getAttribute("name");
        String value = element.getAttribute("value");
        BeanDefinitionHolder idHolder = new BeanDefinitionHolder(def, name);
        BeanDefinitionReaderUtils.registerBeanDefinition(idHolder,
                context.getRegistry());
        MutablePropertyValues mpv = new MutablePropertyValues();
        mpv.add("originalUrl", value);
        def.setPropertyValues(mpv);
        return def;
    }
}
