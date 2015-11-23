package guda.grape.mvc.session.http;

import com.alibaba.fastjson.JSON;
import guda.grape.mvc.session.MapSession;
import guda.grape.mvc.session.Session;
import org.springframework.core.annotation.Order;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.Collections;
import java.util.Enumeration;
import java.util.NoSuchElementException;
import java.util.Set;

/**
 * Created by well on 15/5/29.
 */
@Order(SessionRepositoryFilter.DEFAULT_ORDER)
public class SessionRepositoryFilter<S extends MapSession> extends OncePerRequestFilter {

    public static final int DEFAULT_ORDER = Integer.MIN_VALUE + 50;

    private HttpSessionStrategy httpSessionStrategy;


    public void setHttpSessionStrategy(HttpSessionStrategy httpSessionStrategy) {

        this.httpSessionStrategy = httpSessionStrategy;
    }

    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        SessionRepositoryRequestWrapper wrappedRequest = new SessionRepositoryRequestWrapper(request, response, super.getServletContext());
        SessionRepositoryResponseWrapper wrappedResponse = new SessionRepositoryResponseWrapper(wrappedRequest, response);
        HttpServletRequest strategyRequest = httpSessionStrategy.wrapRequest(wrappedRequest, wrappedResponse);
        HttpServletResponse strategyResponse = httpSessionStrategy.wrapResponse(wrappedRequest, wrappedResponse);
        try {
            filterChain.doFilter(strategyRequest, strategyResponse);
        } finally {
            wrappedRequest.commitSession();
        }
    }


    private final class SessionRepositoryResponseWrapper extends OnCommittedResponseWrapper {

        private final SessionRepositoryRequestWrapper request;


        public SessionRepositoryResponseWrapper(SessionRepositoryRequestWrapper request, HttpServletResponse response) {
            super(response);
            if (request == null) {
                throw new IllegalArgumentException("request cannot be null");
            }
            this.request = request;
        }

        @Override
        protected void onResponseCommitted() {
            request.commitSession();
        }
    }


    private final class SessionRepositoryRequestWrapper extends HttpServletRequestWrapper {
        private HttpSessionWrapper currentSession;

        private final HttpServletResponse response;
        private final HttpServletRequest request;
        private final ServletContext servletContext;

        private SessionRepositoryRequestWrapper(HttpServletRequest request, HttpServletResponse response, ServletContext servletContext) {
            super(request);
            this.request = request;
            this.response = response;
            this.servletContext = servletContext;
        }


        private void commitSession() {
            HttpSessionWrapper wrappedSession = currentSession;
            if (wrappedSession == null) {

            } else {
                MapSession session = wrappedSession.session;
                httpSessionStrategy.onNewSession(session, this, response);
            }
        }


        public String getRequestedSessionId() {
            return httpSessionStrategy.getSessionId(null, request, response);
        }

        @Override
        public HttpSession getSession(boolean create) {
            if (currentSession != null) {
                return currentSession;
            }

            String requestedSessionId = getRequestedSessionId();
            if (requestedSessionId != null) {
                MapSession session = JSON.parseObject(requestedSessionId, MapSession.class);
                if (session != null) {
                    currentSession = new HttpSessionWrapper(session, getServletContext());
                    currentSession.setNew(false);
                    return currentSession;
                }
            }
            if (!create) {
                return null;
            }
            MapSession session = new MapSession();
            currentSession = new HttpSessionWrapper(session, getServletContext());
            return currentSession;
        }

        public ServletContext getServletContext() {
            if (servletContext != null) {
                return servletContext;
            }
            // Servlet 3.0+
            //return super.getServletContext();
            return null;
        }

        @Override
        public HttpSession getSession() {
            return getSession(true);
        }


        private final class HttpSessionWrapper implements HttpSession {
            private final MapSession session;
            private final ServletContext servletContext;
            private boolean invalidated;
            private boolean old;

            public HttpSessionWrapper(MapSession session, ServletContext servletContext) {
                this.session = session;
                this.servletContext = servletContext;
            }


            @Override
            public long getCreationTime() {
                return System.currentTimeMillis();
            }

            public String getId() {
                return session.serialize();
            }

            @Override
            public long getLastAccessedTime() {
                return System.currentTimeMillis();
            }


            public ServletContext getServletContext() {
                return servletContext;
            }

            @Override
            public void setMaxInactiveInterval(int interval) {

            }

            @Override
            public int getMaxInactiveInterval() {
                return -1;
            }


            @SuppressWarnings("deprecation")
            public HttpSessionContext getSessionContext() {
                return NOOP_SESSION_CONTEXT;
            }

            public Object getAttribute(String name) {
                checkState();
                return session.getAttribute(name);
            }

            public Object getValue(String name) {
                return getAttribute(name);
            }

            public Enumeration<String> getAttributeNames() {
                checkState();
                return Collections.enumeration(session.attributeNames());
            }

            public String[] getValueNames() {
                checkState();
                Set<String> attrs = session.attributeNames();
                return attrs.toArray(new String[0]);
            }

            public void setAttribute(String name, Object value) {
                checkState();
                session.setAttribute(name, value);
            }

            public void putValue(String name, Object value) {
                setAttribute(name, value);
            }

            public void removeAttribute(String name) {
                checkState();
                session.removeAttribute(name);
            }

            public void removeValue(String name) {
                removeAttribute(name);
            }

            public void invalidate() {
                currentSession = null;
            }

            public void setNew(boolean isNew) {
                this.old = !isNew;
            }

            public boolean isNew() {
                checkState();
                return !old;
            }

            private void checkState() {
                if (invalidated) {
                    throw new IllegalStateException("The HttpSession has already be invalidated.");
                }
            }
        }
    }

    @SuppressWarnings("deprecation")
    private static final HttpSessionContext NOOP_SESSION_CONTEXT = new HttpSessionContext() {
        public HttpSession getSession(String sessionId) {
            return null;
        }

        public Enumeration<String> getIds() {
            return EMPTY_ENUMERATION;
        }
    };

    private static final Enumeration<String> EMPTY_ENUMERATION = new Enumeration<String>() {
        public boolean hasMoreElements() {
            return false;
        }

        public String nextElement() {
            throw new NoSuchElementException("a");
        }
    };

    static class MultiHttpSessionStrategyAdapter implements HttpSessionStrategy {
        private HttpSessionStrategy delegate;

        public MultiHttpSessionStrategyAdapter(HttpSessionStrategy delegate) {
            this.delegate = delegate;
        }


        @Override
        public String getSessionId(Session session, HttpServletRequest request, HttpServletResponse response) {
            return delegate.getSessionId(session, request, response);
        }

        public void onNewSession(Session session, HttpServletRequest request,
                                 HttpServletResponse response) {
            delegate.onNewSession(session, request, response);
        }

        public void onInvalidateSession(Session session, HttpServletRequest request,
                                        HttpServletResponse response) {
            delegate.onInvalidateSession(session, request, response);
        }

        public HttpServletRequest wrapRequest(HttpServletRequest request,
                                              HttpServletResponse response) {
            return request;
        }

        public HttpServletResponse wrapResponse(HttpServletRequest request,
                                                HttpServletResponse response) {
            return response;
        }
    }


}
