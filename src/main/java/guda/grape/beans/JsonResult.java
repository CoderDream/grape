package guda.grape.beans;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by well on 14-11-28.
 */
public class JsonResult {

    private Map map = new ConcurrentHashMap();
    private String RESULT = "result";
    private String SUCCESS = "success";
    private String MSG = "msg";


    public static JsonResult getInstance() {
        return new JsonResult();
    }

    public JsonResult() {
        map.put(SUCCESS, true);
    }

    public void setErrMsg(String msg) {
        map.put(MSG, msg);
        map.put(SUCCESS, false);
    }

    public void setSuccess(boolean success) {
        map.put(SUCCESS, success);
    }


    public void setResult(Object result) {
        map.put(RESULT, result);
    }

    public void setData(String key, Object data) {
        map.put(key, data);
    }

    public Map getMap() {
        return map;
    }


}
