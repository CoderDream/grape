package guda.grape.autogen.helper;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by foodoon on 2014/6/29.
 */
public class BeanHelper {

    public static Map<String,Object> getProperty(Object entityName) throws Exception {
        Map<String,Object> map = new HashMap<String,Object>();
        Class c = entityName.getClass();
        Field field[] = c.getDeclaredFields();
        for (Field f : field) {
            Object v = invokeMethod(entityName, f.getName(), null);
            if(v!=null){
                map.put(f.getName(),v);
            }
        }
        return map;
    }


    private static Object invokeMethod(Object owner, String methodName,
                                       Object[] args) throws Exception {
        Class ownerClass = owner.getClass();
        methodName = methodName.substring(0, 1).toUpperCase()
                + methodName.substring(1);
        Method method = null;
        try {
            method = ownerClass.getMethod("get" + methodName);
        } catch (SecurityException e) {
        } catch (NoSuchMethodException e) {
            return null;
        }
        return method.invoke(owner);
    }

}
