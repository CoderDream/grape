package guda.grape.mvc.spring;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;

/**
 * Created by foodoon on 2014/7/28.
 */
public class SpringBeanFactoryTool implements BeanFactoryAware{

    private static BeanFactory beanFactory;

    public static BeanFactory getBeanFactory(){
        return beanFactory;
    }

    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        SpringBeanFactoryTool.beanFactory = beanFactory;
    }
}
