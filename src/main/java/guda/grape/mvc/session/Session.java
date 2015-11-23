package guda.grape.mvc.session;

import java.util.Set;

/**
 * Created by well on 15/5/29.
 */
public interface Session {


    String serialize();


    <T> T getAttribute(String attributeName);

    Set<String> attributeNames();


    void setAttribute(String attributeName, Object attributeValue);


    void removeAttribute(String attributeName);
}
