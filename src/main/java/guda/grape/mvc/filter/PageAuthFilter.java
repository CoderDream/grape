package guda.grape.mvc.filter;


import guda.grape.mvc.security.PageAuthChecker;
import org.springframework.beans.factory.InitializingBean;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by well on 15/10/15.
 */
public class PageAuthFilter implements Filter ,InitializingBean{

    private PageAuthChecker pageAuthChecker ;

    private PageAuthChecker.Callback callback;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        if (request instanceof HttpServletRequest) {
            boolean check = pageAuthChecker.check((HttpServletRequest) request, (HttpServletResponse) response);
            if(check){
                chain.doFilter(request, response);
            }
        }

    }

    @Override
    public void destroy() {

    }

    public void setCallback(PageAuthChecker.Callback callback) {
        this.callback = callback;
    }

    public void setPageAuthChecker(PageAuthChecker pageAuthChecker) {
        this.pageAuthChecker = pageAuthChecker;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        if(pageAuthChecker == null) {
            pageAuthChecker = new PageAuthChecker();
        }
        if(callback != null) {
            pageAuthChecker.setCallback(callback);
        }
    }
}
