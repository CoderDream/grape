package guda.grape.mvc.filter;


import org.apache.commons.lang.StringEscapeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * Created by foodoon on 2015/2/13.
 */
public class XssFilter implements Filter {

    private static final Logger log = LoggerFactory.getLogger(XssFilter.class);

    private List<String> filterChainDefinitions;


    public void init(FilterConfig filterConfig) throws ServletException {

    }


    public void doFilter(ServletRequest request, ServletResponse response,
                         FilterChain chain) throws IOException, ServletException {
        String path = ((HttpServletRequest) request).getContextPath();
        String uri = ((HttpServletRequest) request).getRequestURI().replace(path, "");
        Map m = request.getParameterMap();
        if (matchUri(uri)) {
            try {
                m = this.clearRequestPra(request, new HashMap<String,Object>());
            } catch (Exception e) {
                log.info(e.toString());
            }
        }

        ParameterRequestWrapper wrapRequest = new ParameterRequestWrapper(((HttpServletRequest) request), m);
        chain.doFilter(wrapRequest, response);
    }

    private Map<String,Object> clearRequestPra(ServletRequest request, Map<String,Object> m) {
        Map params = request.getParameterMap();

        Set<String> keys = params.keySet();
        for (String key : keys) {
            Object value = params.get(key);
            if (value instanceof String[]) {
                String[] str = (String[]) value;
                int i = 0;
                for (String v : (String[]) value) {
                    v = this.scan(v);
                    str[i] = new String(v);
                    i++;
                }
                m.put(key, str);
            } else {
                m.put(key, value);
            }
        }

        return m;
    }

    private String scan(String content) {
        content = StringEscapeUtils.escapeHtml(content);
        content = StringEscapeUtils.escapeJavaScript(content);
        content = StringEscapeUtils.escapeSql(content);
        return content;
    }

    private boolean matchUri(String uri) {
        for (String pattern : filterChainDefinitions) {
            if (Pattern.matches(pattern, uri)) {
                return true;
            }

        }
        return false;
    }

    public void setFilterChainDefinitions(List<String> filterChainDefinitions) {
        this.filterChainDefinitions = filterChainDefinitions;
    }


    public void destroy() {

    }

}
