package guda.grape.mvc.session.http;


import guda.grape.mvc.session.Session;
import org.springframework.util.Assert;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

/**
 * Created by well on 15/5/29.
 */
public class HeaderHttpSessionStrategy implements HttpSessionStrategy {
    private String headerName = "x-auth-token";

    public String getRequestedSessionId(HttpServletRequest request) {
        return request.getHeader(headerName);
    }

    @Override
    public String getSessionId(Session session, HttpServletRequest request, HttpServletResponse response) {
        return request.getHeader(headerName);
    }

    public void onNewSession(Session session, HttpServletRequest request, HttpServletResponse response) {
        response.setHeader(headerName, session.serialize());
    }

    @Override
    public void onInvalidateSession(Session session, HttpServletRequest request, HttpServletResponse response) {
        response.setHeader(headerName, "");
    }

    public void onInvalidateSession(HttpServletRequest request, HttpServletResponse response) {
        response.setHeader(headerName, "");
    }


    public void setHeaderName(String headerName) {
        Assert.notNull(headerName, "headerName cannot be null");
        this.headerName = headerName;
    }

    public HttpServletRequest wrapRequest(HttpServletRequest request, HttpServletResponse response) {
        return request;
    }

    public HttpServletResponse wrapResponse(HttpServletRequest request, HttpServletResponse response) {
        return new MultiSessionHttpServletResponse(response, request);
    }

    class MultiSessionHttpServletResponse extends HttpServletResponseWrapper {
        private final HttpServletRequest request;

        public MultiSessionHttpServletResponse(HttpServletResponse response, HttpServletRequest request) {
            super(response);
            this.request = request;
        }

        @Override
        public String encodeRedirectURL(String url) {
            url = super.encodeRedirectURL(url);
            return (url);
        }

        @Override
        public String encodeURL(String url) {
            url = super.encodeURL(url);
            return  url;
        }
    }

}
