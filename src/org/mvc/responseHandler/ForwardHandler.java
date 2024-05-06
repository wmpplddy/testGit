package org.mvc.responseHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

/**
 *
 */
public class ForwardHandler implements ResponseHandlerStrategy{

    @Override
    public void handle(Object result, Method targetMethod, HttpServletRequest request, HttpServletResponse response) throws Exception {
        request.getRequestDispatcher((String)result).forward(request,response);
    }

    @Override
    public boolean isSupport(Object result, Method targetMethod) {
        return result instanceof String;
    }
}
