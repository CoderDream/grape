package guda.grape.mvc.session.http;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by well on 15/5/29.
 */
abstract class OncePerRequestFilter implements Filter {

    public static final String ALREADY_FILTERED_SUFFIX = ".FILTERED";

    private String alreadyFilteredAttributeName = getClass().getName().concat(ALREADY_FILTERED_SUFFIX);



    public final void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        if (!(request instanceof HttpServletRequest) || !(response instanceof HttpServletResponse)) {
            throw new ServletException("OncePerRequestFilter just supports HTTP requests");
        }
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        boolean hasAlreadyFilteredAttribute = request.getAttribute(alreadyFilteredAttributeName) != null;


        if (hasAlreadyFilteredAttribute) {

            // Proceed without invoking this filter...
            filterChain.doFilter(request, response);
        }
        else {
            // Do invoke this filter...
            request.setAttribute(alreadyFilteredAttributeName, Boolean.TRUE);
            try {
                doFilterInternal(httpRequest, httpResponse, filterChain);
            }
            finally {
                // Remove the "already filtered" request attribute for this request.
                request.removeAttribute(alreadyFilteredAttributeName);
            }
        }
    }



    protected abstract void doFilterInternal(
            HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException;

    public void init(FilterConfig config) {}

    public void destroy() {}
}
