package guda.grape.mvc.filter;




import guda.grape.mvc.security.AppContexHolder;
import guda.grape.mvc.security.AppContext;
import guda.grape.mvc.security.SecuritySessionConstants;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * Created by foodoon on 2014/12/21.
 */
public class AppContextFilter implements Filter {

    public void init(FilterConfig filterConfig) throws ServletException {

    }


    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        if (request instanceof HttpServletRequest) {
            HttpServletRequest req = (HttpServletRequest) request;
            AppContext appContex = (AppContext) req.getSession().getAttribute(SecuritySessionConstants.APP_CONTEXT);
            AppContexHolder.setContext(appContex);
        }
        chain.doFilter(request, response);
    }


    public void destroy() {

    }
}
