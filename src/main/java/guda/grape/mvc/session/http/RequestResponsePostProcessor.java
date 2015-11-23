package guda.grape.mvc.session.http;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by well on 15/5/29.
 */
public interface RequestResponsePostProcessor {


    HttpServletRequest wrapRequest(HttpServletRequest request,
                                   HttpServletResponse response);

    HttpServletResponse wrapResponse(HttpServletRequest request,
                                     HttpServletResponse response);
}