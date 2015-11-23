package guda.grape.mvc.session.http;


import guda.grape.mvc.session.util.BlowFish;
import org.springframework.core.annotation.Order;

import javax.servlet.ServletException;

/**
 * Created by well on 15/5/31.
 */
@Order(SessionRepositoryFilter.DEFAULT_ORDER)
public class SessionStoreFilter extends SessionRepositoryFilter {

    private String domain;
    private String checkCookieKey="J_C_01";
    private String encryptKey = "guda";
    private Integer maxInactiveInterval;
    private String cookieKey = "J_C_";


    public String getDomain() {
        return domain;
    }


    public void setDomain(String domain) {
        this.domain = domain;
    }


    public String getEncryptKey() {
        return encryptKey;
    }

    public void setEncryptKey(String encryptKey) {
        this.encryptKey = encryptKey;
    }

    public Integer getMaxInactiveInterval() {
        return maxInactiveInterval;
    }

    public void afterPropertiesSet() throws ServletException {
        super.initFilterBean();

        CookieHttpSessionStrategy cookieHttpSessionStrategy = new CookieHttpSessionStrategy();
        super.setHttpSessionStrategy(cookieHttpSessionStrategy);
        cookieHttpSessionStrategy.setDomain(domain);
        cookieHttpSessionStrategy.setCookieName(cookieKey);
        cookieHttpSessionStrategy.setCheckSumCookieName(checkCookieKey);
        cookieHttpSessionStrategy.setMaxInactiveInterval(maxInactiveInterval);
        cookieHttpSessionStrategy.setBlowFish(new BlowFish(encryptKey));
    }


    public void setCheckCookieKey(String checkCookieKey) {
        this.checkCookieKey = checkCookieKey;
    }

    public String getCheckCookieKey() {
        return checkCookieKey;
    }

    String getCookieKey() {
        return cookieKey;
    }

    public void setCookieKey(String cookieKey) {
        this.cookieKey = cookieKey;
    }

    public void setMaxInactiveInterval(Integer maxInactiveInterval) {
        this.maxInactiveInterval = maxInactiveInterval;
    }
}
