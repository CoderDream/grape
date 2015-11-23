package guda.grape.mvc.form;

import javax.servlet.http.HttpServletRequest;
import java.util.UUID;

public class FormTools {
	
	public static String newForm(HttpServletRequest req){
		String token = String.valueOf(UUID.randomUUID());
		req.getSession().setAttribute(FormTokenHandlerInterceptorAdapter.FORM_TOKEN_KEY, token);
		return token;
	}

}
