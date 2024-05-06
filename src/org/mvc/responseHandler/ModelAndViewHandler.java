package org.mvc.responseHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Set;

/**
 * ModelAndView����Ӧ����<br/>
 * handle����ת�� ���ҽ�ModelAndView��map����ȫ����ֵ��request��<br/>
 * @isSupport �������ֱ���жϷ�������ֵ�Ƿ�ΪModelAndView<br/>
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
