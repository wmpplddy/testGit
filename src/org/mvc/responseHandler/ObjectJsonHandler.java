package org.mvc.responseHandler;

import com.alibaba.fastjson.JSON;
import org.mvc.annotations.RequestBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

public class ObjectJsonHandler implements ResponseHandlerStrategy{

    @Override
    public void handle(Object result, Method targetMethod, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String jsonStr = JSON.toJSONString(result);
        response.setContentType("application/json;charset=utf-8");
        response.getWriter().write(jsonStr);
    }
    public boolean isSupport(Object result, Method targetMethod) {
        return targetMethod.getAnnotation(RequestBody.class)!=null;
    }
}
