package org.mvc.responseHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Set;

/**
 * ModelAndView的响应处理<br/>
 * handle处理转发 并且将ModelAndView的map参数全部赋值到request里<br/>
 * @isSupport 这个方法直接判断方法返回值是否为ModelAndView<br/>
 */
public class ModelAndViewHandler implements ResponseHandlerStrategy{

    @Override
    public void handle(Object result, Method targetMethod, HttpServletRequest request, HttpServletResponse response) throws Exception {
        ModelAndView view = (ModelAndView) result;
        String viewName = view.getViewName();
        HashMap<String, Object> map = view.getReqAttributes();
        Set<String> keys = map.keySet();
        for (String key:keys) {
            request.setAttribute(key,map.get(key));
        }
        request.getRequestDispatcher(viewName).forward(request,response);
    }

    @Override
    public boolean isSupport(Object result, Method targetMethod) {
        return result instanceof ModelAndView ;
    }
}
