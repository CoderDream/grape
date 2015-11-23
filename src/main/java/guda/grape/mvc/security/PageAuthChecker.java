package guda.grape.mvc.security;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by well on 15/10/15.
 */
public class PageAuthChecker {

    private Callback<?> callback = new DefaultCallback();
    private PageAuthService pageAuthService = new PageAuthService();

    public boolean check(HttpServletRequest request,HttpServletResponse response) throws ServletException {
        Callback<Object> cb = (Callback<Object>) callback;
        RequestContext requestContext = new RequestContext();
        requestContext.httpServletRequest = request;
        requestContext.httpServletResponse = response;
        Object status = cb.onStart(requestContext);

        String userName = cb.getUserName(status);
        String[] roleNames = cb.getRoleNames(status);
        String requestUri  = request.getRequestURI();

        if (pageAuthService.isAllow(requestUri, userName, roleNames)) {
            cb.onAllow(status);
            return true;
        } else {
            cb.onDeny(status);
            return false;
        }
    }

    public void setCallback(Callback<?> callback) {
        this.callback = callback;
    }

    public interface Callback<T> {
        String getUserName(T status);

        String[] getRoleNames(T status);

        T onStart(RequestContext context) throws ServletException;

        void onAllow(T status) throws ServletException;

        void onDeny(T status) throws ServletException;
    }

    public class RequestContext{
        public HttpServletRequest httpServletRequest;
        public HttpServletResponse httpServletResponse;
    }

    private class DefaultCallback implements Callback<RequestContext> {
        public String getUserName(RequestContext status) {
            return null;
        }

        public String[] getRoleNames(RequestContext status) {
            return null;
        }

        public RequestContext onStart(RequestContext context) {
            return context;
        }

        public void onAllow(RequestContext status) throws ServletException {
        }

        public void onDeny(RequestContext status) {
        }
    }
}
