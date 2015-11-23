package guda.grape.mvc.session.http;


import guda.grape.mvc.session.Session;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by well on 15/5/29.
 */
public interface HttpSessionStrategy extends RequestResponsePostProcessor{

    String getSessionId(Session session, HttpServletRequest request, HttpServletResponse response);

    void onNewSession(Session session, HttpServletRequest request, HttpServletResponse response);

    void onInvalidateSession(Session session,HttpServletRequest request, HttpServletResponse response);
}
