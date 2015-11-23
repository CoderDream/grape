package guda.grape.mvc.filter;

import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.*;
import java.io.IOException;
import java.util.List;


public class FilterChainProxy extends GenericFilterBean {


    public void setFilterChains(List<Filter> filterChains) {
        this.filterChains = filterChains;
    }

    private List<Filter> filterChains;

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException,
            ServletException {
        if (filterChains == null || filterChains.size() == 0) {
            chain.doFilter(request, response);
            return;
        }
        VirtualFilterChain vfc = new VirtualFilterChain(chain, filterChains);
        vfc.doFilter(request, response);
    }

    private static class VirtualFilterChain implements FilterChain {
        private final FilterChain originalChain;
        private final List<Filter> additionalFilters;
        private final int size;
        private int currentPosition = 0;

        private VirtualFilterChain(FilterChain chain, List<Filter> additionalFilters) {
            this.originalChain = chain;
            this.additionalFilters = additionalFilters;
            this.size = additionalFilters.size();
        }

        public void doFilter(ServletRequest request, ServletResponse response) throws IOException,
                ServletException {
            if (currentPosition == size) {
                originalChain.doFilter(request, response);
            } else {
                currentPosition++;
                Filter nextFilter = additionalFilters.get(currentPosition - 1);
                nextFilter.doFilter(request, response, this);
            }
        }
    }

}
