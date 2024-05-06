package org.mvc.responseHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

/**
 * �ض������� �жϷ�ʽ���Ƿ�ΪString �� startsWith " redirect: "
 */
public class RedirectHandler implements ResponseHandlerStrategy{
    @Override
    public void handle(Object result, Method targetMethod, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String val =(String)result;
        val = val.replace("redirect:", "");
        response.sendRedirect(val);
    }

    @Override
    public boolean isSupport(Object result, Method targetMethod) {
        return result instanceof String &&
                ((String) result).startsWith("redirect:");
    }
}
