package guda.grape.mvc.session;

import com.alibaba.fastjson.JSON;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

/**
 * Created by well on 15/5/29.
 */
public class MapSession implements Session, Serializable {


    private Map<String, Object> sessionAttrs = new HashMap<String, Object>();

    private String id = UUID.randomUUID().toString();


    public MapSession() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public MapSession(MapSession session) {
        if (session == null) {
            throw new IllegalArgumentException("session cannot be null");
        }

        this.sessionAttrs = new HashMap<String, Object>(session.attributeNames().size());
        for (String attrName : session.attributeNames()) {
            Object attrValue = session.getAttribute(attrName);
            this.sessionAttrs.put(attrName, attrValue);
        }

    }


    public String serialize() {
        return JSON.toJSONString(this);
    }

    public Object getAttribute(String attributeName) {
        return sessionAttrs.get(attributeName);
    }

    public Set<String> attributeNames() {
        return sessionAttrs.keySet();
    }

    public void setAttribute(String attributeName, Object attributeValue) {
        if (attributeValue == null) {
            removeAttribute(attributeName);
        } else {
            sessionAttrs.put(attributeName, attributeValue);
        }
    }

    public void removeAttribute(String attributeName) {
        sessionAttrs.remove(attributeName);
    }


    public boolean equals(Object obj) {
        return obj instanceof Session && serialize().equals(((Session) obj).serialize());
    }

    public int hashCode() {
        return serialize().hashCode();
    }

    public Map<String, Object> getSessionAttrs() {
        return sessionAttrs;
    }

    private static final long serialVersionUID = 7160779239673823561L;

}
