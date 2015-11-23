package guda.grape.mvc.velocity;

import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.context.Context;

import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by foodoon on 2014/6/28.
 */
public class Tile {


    private VelocityEngine velocityEngine;

    private Context velocityContext;

    private Map<String, Object> params = new HashMap<String, Object>();

    private String vmName;

    public Tile(VelocityEngine velocityEngine,Context velocityContext) {
        this.velocityEngine = velocityEngine;
        this.velocityContext = velocityContext;
    }

    public Tile load(String template) {
        vmName = template;
        return this;
    }

    public Tile param(String key, Object val) {
        params.put(key, val);
        return this;
    }

    public String toString() {
        VelocityContext context = new VelocityContext(params);

        if (velocityContext.containsKey(VelocityToolboxView.defaultServerName)) {
            context.put(VelocityToolboxView.defaultServerName, velocityContext.get(VelocityToolboxView.defaultServerName));
        }

        StringWriter w = new StringWriter();
        try {
            velocityEngine.mergeTemplate(vmName, "UTF-8", context, w);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return w.toString();

    }
}
