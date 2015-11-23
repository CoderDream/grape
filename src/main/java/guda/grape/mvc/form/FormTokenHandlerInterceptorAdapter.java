package guda.grape.mvc.form;

import org.springframework.util.StringUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class FormTokenHandlerInterceptorAdapter extends HandlerInterceptorAdapter {

    public static final String FORM_TOKEN_KEY = "_form_token";

    public static final String FORM_ERROR_VIEW = "/formError.htm";

    private String formErrorView = FORM_ERROR_VIEW;

    @Override
    public void postHandle(HttpServletRequest request,
                           HttpServletResponse response, Object handler,
                           ModelAndView modelAndView) throws Exception {
        if (handler instanceof HandlerMethod) {
            HandlerMethod m = (HandlerMethod) handler;
            if (m.getMethodAnnotation(Form.class) != null) {
                String token = request.getParameter(FORM_TOKEN_KEY);
                if (StringUtils.hasText(token)) {
                    Object sessionToken = request.getSession().getAttribute(
                            FORM_TOKEN_KEY);
                    if (sessionToken == null) {
                        response.sendRedirect(formErrorView);
                        return;
                    }
                    if (token.equals(String.valueOf(sessionToken))) {
                        request.getSession().removeAttribute(FORM_TOKEN_KEY);
                    } else {
                        response.sendRedirect(formErrorView);
                        return;
                    }
                } else {
                    response.sendRedirect(formErrorView);
                    return;
                }
            }
        }
        super.postHandle(request, response, handler, modelAndView);
    }

}
