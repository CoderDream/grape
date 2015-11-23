package guda.grape.mvc.session.http;


import guda.grape.mvc.session.Session;
import guda.grape.mvc.session.util.BlowFish;
import guda.grape.mvc.session.util.CookieCheck;
import guda.grape.mvc.session.util.ServerCookie;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.ServletRequest;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import java.util.*;

/**
 * Created by well on 15/5/29.
 */
public final class CookieHttpSessionStrategy implements HttpSessionStrategy {

    protected final Log logger = LogFactory.getLog(getClass());



    private String cookieName = "G_C_02";

    private String checkSumCookieName = "G_C_01";

    private BlowFish blowFish;

    private boolean isServlet3Plus = isServlet3();
    private String domain;

    private Integer maxInactiveInterval = -1;



    @Override
    public String getSessionId(Session session, HttpServletRequest request, HttpServletResponse response) {
        Cookie cookie = getCookie(request, cookieName);
        if(cookie == null){
            return null;
        }
        Cookie checkCookie = getCookie(request, checkSumCookieName);
        if(checkCookie == null){
            return null;
        }
        String value = cookie.getValue();
        if(blowFish!=null){
            value=  blowFish.decrypt(value);
        }
        if(!CookieCheck.mdCheck(value).equals(checkCookie.getValue())){
            logger.warn("cookie check invalid,"+ (value));
            return null;
        }
        return value;
    }

    public void onNewSession(Session session, HttpServletRequest request, HttpServletResponse response) {


        List<Cookie> cookieList = createSessionCookie(request, session);
        //int version, String name, String value, String path,
        //String domain, String comment, int maxAge, boolean isSecure, boolean isHttpOnly

        for(Cookie cookie:cookieList) {
            StringBuilder buf = new StringBuilder();
            ServerCookie.appendCookieValue(buf,
                    cookie.getVersion(),
                    cookie.getName(),
                    cookie.getValue(),
                    cookie.getPath(),
                    cookie.getDomain(),
                    cookie.getComment(),
                    cookie.getMaxAge(),
                    cookie.getSecure(),
                    true);
            response.addHeader("Set-Cookie", buf.toString());
        }


    }

    private List<Cookie> createSessionCookie(HttpServletRequest request,
                                       Session session) {

        List<Cookie> cookieList = new ArrayList<Cookie>();
        Cookie sessionCookie = new Cookie(cookieName, "");

        //sessionCookie.setHttpOnly(true);

        sessionCookie.setSecure(request.isSecure());
        sessionCookie.setPath(cookiePath(request));

        if (domain != null) {
            sessionCookie.setDomain(domain);
        }
        sessionCookie.setMaxAge(getMaxInactiveInterval());
        String sessionVal = session.serialize();
        if(blowFish!= null) {
            sessionCookie.setValue(blowFish.encrypt(sessionVal));
        }else{
            sessionCookie.setValue(sessionVal);
        }
        Cookie checkCookie = new Cookie(checkSumCookieName,"");
        checkCookie.setSecure(request.isSecure());
        checkCookie.setPath(cookiePath(request));
        checkCookie.setValue(CookieCheck.mdCheck(sessionVal));
        if (domain != null) {
            checkCookie.setDomain(domain);
        }
        checkCookie.setMaxAge(getMaxInactiveInterval());
        cookieList.add(sessionCookie);
        cookieList.add(checkCookie);
        return cookieList;
    }

    public void onInvalidateSession(Session session,HttpServletRequest request, HttpServletResponse response) {

        List<Cookie> cookieList = createSessionCookie(request, session);

        for(Cookie cookie:cookieList) {
            StringBuilder buf = new StringBuilder();
            ServerCookie.appendCookieValue(buf,
                    cookie.getVersion(),
                    cookie.getName(),
                    "",
                    cookie.getPath(),
                    cookie.getDomain(),
                    cookie.getComment(),
                    0,
                    cookie.getSecure(),
                    true);
            response.addHeader("Set-Cookie", buf.toString());
        }

    }



    public void setCookieName(String cookieName) {
        if (cookieName == null) {
            throw new IllegalArgumentException("cookieName cannot be null");
        }
        this.cookieName = cookieName;
    }


    private static Cookie getCookie(HttpServletRequest request, String name) {
        if (request == null) {
            throw new IllegalArgumentException("request cannot be null");
        }
        Cookie cookies[] = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (name.equals(cookie.getName())) {
                    return cookie;
                }
            }
        }
        return null;
    }

    private static String cookiePath(HttpServletRequest request) {
        return request.getContextPath() + "/";
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




    private boolean isServlet3() {
        try {
            ServletRequest.class.getMethod("startAsync");
            return true;
        } catch (NoSuchMethodException e) {
        }
        return false;
    }


    public void setCheckSumCookieName(String checkSumCookieName) {
        this.checkSumCookieName = checkSumCookieName;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public Integer getMaxInactiveInterval() {
        return maxInactiveInterval;
    }

    public void setMaxInactiveInterval(Integer maxInactiveInterval) {
        this.maxInactiveInterval = maxInactiveInterval;
    }

    public void setBlowFish(BlowFish blowFish) {
        this.blowFish = blowFish;
    }
}
