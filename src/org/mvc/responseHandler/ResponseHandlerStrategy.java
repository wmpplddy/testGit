package org.mvc.responseHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

public interface ResponseHandlerStrategy {
    void handle(Object result , Method targetMethod, HttpServletRequest request, HttpServletResponse response) throws Exception;
    boolean isSupport(Object result , Method targetMethod);
}
