package guda.grape.autogen.helper;

import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.app.VelocityEngine;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

/**
 * Created by foodoon on 2014/6/28.
 */
public class VelocityHelper {

    private static VelocityEngine velocityEngine;

    public static String render(String vmName, Map<String, Object> params) throws Exception{


        VelocityContext context = new VelocityContext();
        Set<Map.Entry<String, Object>> set = params.entrySet();
        Iterator<Map.Entry<String, Object>> it = set.iterator();
        while (it.hasNext()) {
            Map.Entry<String, Object> en = it.next();
            context.put(en.getKey(), en.getValue());
        }
        StringWriter writer = new StringWriter();
        if(velocityEngine == null){
            afterPropertiesSet();
        }
        InputStreamReader inputStreamReader = null;
        InputStream resourceAsStream = null;
        try {

             resourceAsStream = VelocityHelper.class.getClassLoader().getResourceAsStream("vm/" + vmName);
            inputStreamReader = new InputStreamReader(resourceAsStream);
            velocityEngine.evaluate(context, writer, "", inputStreamReader);
        }finally{
            if(inputStreamReader!=null) {
                inputStreamReader.close();
            }
            if(resourceAsStream!=null) {
                resourceAsStream.close();
            }
        }
        return writer.toString();
    }



    public static void afterPropertiesSet() throws Exception {
        Properties props = new Properties();
        props.setProperty(Velocity.INPUT_ENCODING, "UTF-8");
        props.setProperty(Velocity.RESOURCE_LOADER, "class");
        props.setProperty("class.resource.loader.class",
                "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
        velocityEngine  = new VelocityEngine(props);
    }
}
