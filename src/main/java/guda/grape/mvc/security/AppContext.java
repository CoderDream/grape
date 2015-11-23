package guda.grape.mvc.security;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by foodoon on 2014/12/20.
 */
public class AppContext {

    private Map<String,Object> attr = new HashMap<String,Object>();

    public void putAttr(String key,Object val){
        attr.put(key,val);
    }

    public Object getAttr(String key){
        return attr.get(key);
    }


}
