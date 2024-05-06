package org.mvc.responseHandler;

import org.mvc.annotations.RequestBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;




public class StringHandler implements ResponseHandlerStrategy{
    @Override
    public void handle(Object result, Method targetMethod, HttpServletRequest request, HttpServletResponse response) throws Exception {
        response.setContentType("text/html;charset=utf-8;");
        response.getWriter().write((String)result);
    }

    @Override
    public boolean isSupport(Object result, Method targetMethod) {
        return targetMethod.getAnnotation(RequestBody.class)!=null &&
                result instanceof String;
    }
}
