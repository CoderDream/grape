package guda.grape.mvc.velocity;


import guda.grape.mvc.uribox.DefaultURIBox;
import guda.grape.mvc.uribox.URIBox;
import guda.grape.mvc.uribox.URIBoxManager;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.context.Context;
import org.apache.velocity.tools.Scope;
import org.apache.velocity.tools.ToolManager;
import org.apache.velocity.tools.config.FileFactoryConfiguration;
import org.apache.velocity.tools.config.XmlFactoryConfiguration;
import org.apache.velocity.tools.view.ViewToolContext;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.xml.ResourceEntityResolver;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.web.servlet.view.velocity.VelocityLayoutView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.Map;

public class VelocityToolboxView extends VelocityLayoutView implements BeanDefinitionRegistryPostProcessor {

    public static final String defaultUrl        = "http://localhost";

    public static final String defaultServerName = "homeServer";

    private DefaultURIBox defaultBox        = null;
    private ApplicationContext applicationContext;

    @Override
    protected Context createVelocityContext(Map<String, Object> model, HttpServletRequest request,
                                            HttpServletResponse response) throws Exception {

        ViewToolContext ctx;

        ctx = new ViewToolContext(getVelocityEngine(), request, response, getServletContext());

        ctx.putAll(model);

        if (this.getToolboxConfigLocation() != null) {
            ToolManager tm = new ToolManager();
            tm.setVelocityEngine(getVelocityEngine());
            ClassLoader currentClassLoader = Thread.currentThread().getContextClassLoader();

            InputStream input = currentClassLoader.getResourceAsStream(getToolboxConfigLocation());
            FileFactoryConfiguration config = new XmlFactoryConfiguration(false);
            config.read(input);
            tm.configure(config);
            if (tm.getToolboxFactory().hasTools(Scope.REQUEST)) {
                ctx.addToolbox(tm.getToolboxFactory().createToolbox(Scope.REQUEST));
            }
            if (tm.getToolboxFactory().hasTools(Scope.APPLICATION)) {
                ctx.addToolbox(tm.getToolboxFactory().createToolbox(Scope.APPLICATION));
            }
            if (tm.getToolboxFactory().hasTools(Scope.SESSION)) {
                ctx.addToolbox(tm.getToolboxFactory().createToolbox(Scope.SESSION));
            }
        }
        return ctx;
    }

    /**
     * @see org.springframework.web.servlet.view.velocity.VelocityView#exposeHelpers(org.apache.velocity.context.Context, HttpServletRequest)
     */
    @Override
    protected void exposeHelpers(Context velocityContext, HttpServletRequest request)
            throws Exception {
        super.exposeHelpers(velocityContext, request);
        URIBoxManager uriBoxManager = new URIBoxManager();
        Map<String, URIBox> box = uriBoxManager.loadURIBox();
        Iterator<String> it = box.keySet().iterator();
        velocityContext.put(defaultServerName, getFullContextPath(request));
        while (it.hasNext()) {
            String key = it.next();
            URIBox uri = box.get(key);
            if (uri.render().equals(defaultUrl)) {
                velocityContext.put(key, getFullContextPath(request));
            } else {
                velocityContext.put(key, uri);
            }
        }
        VelocityEngine velocityEngine = super.getVelocityEngine();
        velocityContext.put("tile",new Tile(velocityEngine,velocityContext));

    }

    private DefaultURIBox getFullContextPath(HttpServletRequest request) {
        if (defaultBox == null) {
            StringBuilder buf = new StringBuilder();
            buf.append(request.getScheme()).append("://").append(request.getServerName());
            if (request.getServerPort() != 80) {
                buf.append(":").append(request.getServerPort());
            }
            buf.append(request.getContextPath());
            defaultBox = new DefaultURIBox(buf.toString());
        }
        return defaultBox;
    }

    public static String getFullContextURL(HttpServletRequest request) {
        String url = request.getRequestURL().toString();
        String path = request.getServletPath();
        return url.substring(0, url.indexOf(path));

    }

    public void registerBean(BeanDefinitionRegistry registry) throws Exception{

        URIBoxManager uriBoxManager = new URIBoxManager();
        Map<String, URIBox> box = uriBoxManager.loadURIBox();
        Iterator<Map.Entry<String, URIBox>> iterator = box.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, URIBox> next = iterator.next();
            BeanDefinitionBuilder beanDefinitionBuilder =  BeanDefinitionBuilder.rootBeanDefinition(DefaultURIBox.class);
            beanDefinitionBuilder.addConstructorArgValue(next.getValue());
            registry.registerBeanDefinition(next.getKey(), beanDefinitionBuilder.getBeanDefinition());
            logger.info("uribox register:" + next.getKey() + ",define:" + ReflectionToStringBuilder.toString(next.getValue()));
        }

    }








    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
        try {
            registerBean(registry);
        } catch (Exception e) {
            throw new BeanCreationException("");
        }
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {

    }
}
