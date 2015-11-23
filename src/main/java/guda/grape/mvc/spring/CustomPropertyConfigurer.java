package guda.grape.mvc.spring;

import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;

import java.io.IOException;
import java.util.Properties;

/**
 * Created by foodoon on 2014/7/28.
 */
public class CustomPropertyConfigurer extends PropertyPlaceholderConfigurer {

    private Properties appProperties;

    public Properties getAppProperties(){
        if(appProperties == null) {
            try {
                appProperties = super.mergeProperties();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return appProperties;
    }
}
